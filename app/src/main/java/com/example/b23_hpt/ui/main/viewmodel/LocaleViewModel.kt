package com.example.b23_hpt.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.b23_hpt.core.apis.RetrofitInstance
import com.example.b23_hpt.core.model.req.Credential
import com.example.b23_hpt.core.model.res.cloud.Locale
import com.example.b23_hpt.core.model.res.cloud.Login
import com.example.b23_hpt.ui.main.repository.LocaleDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocaleViewModel(private val repository: LocaleDataRepository) : ViewModel() {
    // Locale
    // 외부에서는 값을 변경하지 못하게 선언하려면 MutableLiveData를 사용
    private val _localeDataSource: MutableLiveData<List<Locale>?> = MutableLiveData()
    val localeDataSource: LiveData<List<Locale>?> = _localeDataSource

    fun getLocaleData() {
        CoroutineScope(Dispatchers.IO).launch {
            // 별도 set 함수를 제공해서 해당 함수에서 LiveData의 값을 수정하도록 한다
            _localeDataSource.postValue(repository.getLocalData())
            // 메인 쓰레드인경우 setValue
            // 다른 쓰레드인경우 postValue
        }
    }

    // Login
    private val _loginDataSource: MutableLiveData<Login?> = MutableLiveData()
    val loginDataSource: LiveData<Login?> = _loginDataSource
    fun doLogin(credential: Credential?, mieleLocale: MieleLocale?) {
        CoroutineScope(Dispatchers.IO).launch {
            // 2022.04.14 lang 전달 필요시 Retrofit 매번 생성 하도록 (현재 안되고 있음)
//            if (mieleLocale != null) {
//                var lang: String = mieleLocale.code
//                lang = lang.replace("_".toRegex(), "-")
//                RetrofitInstance.changeApiBaseUrl(mieleLocale.endpointRest, lang)
//            }
            // enqueue 메서드를 이용하여 요청 처리
            repository.doLogin(credential).enqueue(object :
                Callback<Login?> {
                // callback 구현
                override fun onResponse(call: Call<Login?>, response: Response<Login?>) {
                    if (response.isSuccessful) {
                        val appToken = response.body()?.appToken
                        Log.d("", "token : $appToken")
                        _loginDataSource.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<Login?>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}

/**
 * 커스텀 생성자가 있는 뷰모델 :  뷰모델 팩토리를 사용해서 초기화
 */
class LocaleViewModelFactory(private val repository: LocaleDataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocaleViewModel(repository) as T
    }
}
