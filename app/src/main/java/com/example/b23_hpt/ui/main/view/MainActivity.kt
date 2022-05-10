package com.example.b23_hpt.ui.main.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.b23_hpt.core.apis.RetrofitInstance
import com.example.b23_hpt.core.extension.logd
import com.example.b23_hpt.core.helper.CacheHelper
import com.example.b23_hpt.core.model.req.Credential
import com.example.b23_hpt.core.model.res.cloud.Locale
import com.example.b23_hpt.databinding.ActivityMainBinding
import com.example.b23_hpt.ui.main.repository.LocaleDataRepository
import com.example.b23_hpt.ui.main.viewmodel.LocaleViewModel
import com.example.b23_hpt.ui.main.viewmodel.LocaleViewModelFactory
import com.example.b23_hpt.ui.main.viewmodel.MieleLocale
import com.example.b23_hpt.ui.pairing.view.PairingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var localeViewModel: LocaleViewModel
    private lateinit var binding: ActivityMainBinding

    private var mieleLocaleList = mutableListOf<MieleLocale>()
    /******************************************************
     * Life cycle
     ******************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    /******************************************************
     * Init
     ******************************************************/
    private fun initView() {
        binding.buttonLocal.setOnClickListener {
            localeViewModel.getLocaleData()
        }

        binding.buttonLogin.setOnClickListener {
            // 이메일과 패스워드 정보를 읽어 옴
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Credential 정보 생성
            val credential = Credential(email, password)

//            val parts: List<String>? = CacheHelper.getInstance().getMieleLocale()?.code?.split("_")
//            val localeInfo = java.util.Locale(parts?.get(0) ?: "", parts?.get(1) ?: "")
            localeViewModel.doLogin(credential, CacheHelper.getInstance().getMieleLocale())
        }

        val pairingAcitivity: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            logd("Pairing Result")
        }
        binding.buttonMovePairing.setOnClickListener {
            val intent = Intent(this, PairingActivity::class.java)
            pairingAcitivity.launch(intent)
        }
    }

    private fun initViewModel() {
        val repository = LocaleDataRepository(RetrofitInstance.api)

        // ViewModel 셋팅
        localeViewModel = ViewModelProvider(this, LocaleViewModelFactory(repository)).get(LocaleViewModel::class.java)

        // 데이터의 변경이 발생 하면 변경된 데이터 셋팅
        localeViewModel.localeDataSource.observe(this) {
            println(it)
            if (it != null) {
                for (item in it) {
                    // active 아닌 지역은 저장하지 않음.
                    if (!item.isActive) {
                        continue
                    }
                    val country = item.countries?.get(0)?.iso
                    if (item.contact?.languages != null) {
                        for (lang in item.contact?.languages!!) {
                            val code = lang.iso + "_" + country
                            val dnsNames: List<Locale.MCSConfigItem>? = item.mcsConfig?.dnsNames
                            var endPointRest = ""
                            var endPointWebsocket = ""
                            if (dnsNames != null) {
                                for (dnsName in dnsNames) {
                                    // 클라우드 서버 주소 저장
                                    if (dnsName.type.equals("rest")) {
                                        endPointRest = dnsName.host ?: ""
                                    }

                                    // websocket 주소 저장
                                    if (dnsName.type.equals("websocket")) {
                                        endPointWebsocket = dnsName.host ?: ""
                                    }

                                }
                            }

                            // 밀레 지역 정보를 리스트 형식으로 저장
                            logd("code : $code   endPointRest : $endPointRest   endPointWebsocket : $endPointWebsocket")
                            val mieleLocale = MieleLocale(code, endPointRest, endPointWebsocket)
                            mieleLocaleList.add(mieleLocale)

                            // 2022.04.13 TEST 한국 주소를 찾아서 로그인 시 해당 주소로 전송하도록 설정
                            if (code == "ko_KR") {
                                CacheHelper.getInstance().setMieleLocale(mieleLocale)
                            }
                        }
                    }
                }
            }
        }
    }
}
