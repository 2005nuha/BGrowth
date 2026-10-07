package com.example.bgrowth.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bgrowth.BGrowthApp
import com.example.bgrowth.data.repository.BusinessRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SplashViewModel(
    private val businessRepository: BusinessRepository =
        BusinessRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SplashUiState())

    val uiState: StateFlow<SplashUiState> =
        _uiState.asStateFlow()

    init {
        restoreSession()
    }

    private fun restoreSession() {

        viewModelScope.launch {

            try {

                /*
                 * نحافظ على ظهور Splash
                 * لمدة قصيرة بدل الانتقال الفوري.
                 */
                delay(SPLASH_DURATION_MILLIS)

                val sessionManager =
                    BGrowthApp.instance
                        .sessionManager

                /*
                 * لا يوجد access + refresh محفوظان.
                 *
                 * المستخدم غير مسجل دخول.
                 */
                if (!sessionManager.hasSession()) {

                    navigateTo(
                        SplashDestination.ONBOARDING
                    )

                    return@launch
                }

                /*
                 * يوجد Session محفوظ.
                 *
                 * لا نعتمد فقط على وجود Tokens محليًا،
                 * بل نجرب Protected API.
                 *
                 * إذا Access Token منتهي،
                 * Retrofit Authenticator سيحاول
                 * استخدام Refresh Token تلقائيًا.
                 */
                try {

                    businessRepository.getBusiness()

                    /*
                     * 200
                     *
                     * Session صالحة
                     * والمستخدم لديه Business.
                     */
                    navigateTo(
                        SplashDestination.DASHBOARD
                    )

                } catch (
                    e: HttpException
                ) {

                    when (e.code()) {

                        /*
                         * Session صالحة،
                         * لكن المستخدم لم ينشئ Business.
                         */
                        404 -> {

                            navigateTo(
                                SplashDestination.BUSINESS_SETUP
                            )
                        }

                        /*
                         * لم نستطع استعادة Session.
                         *
                         * نمسح Tokens القديمة
                         * ونرجع إلى البداية.
                         */
                        401 -> {

                            sessionManager.clearSession()

                            navigateTo(
                                SplashDestination.ONBOARDING
                            )
                        }

                        /*
                         * أي Server/API error آخر
                         * لا يعني أن المستخدم لا يملك Business.
                         *
                         * حاليًا نعيده للبداية بدل
                         * إدخاله إلى Dashboard ببيانات غير مؤكدة.
                         */
                        else -> {

                            navigateTo(
                                SplashDestination.ONBOARDING
                            )
                        }
                    }
                }

            } catch (
                e: CancellationException
            ) {

                throw e

            } catch (
                e: Exception
            ) {

                /*
                 * لا نمسح Session هنا.
                 *
                 * قد يكون السبب فقط انقطاع الإنترنت،
                 * وليس أن Tokens غير صالحة.
                 */
                navigateTo(
                    SplashDestination.ONBOARDING
                )
            }
        }
    }

    private fun navigateTo(
        destination: SplashDestination
    ) {

        _uiState.update {
            it.copy(
                isLoading = false,
                destination = destination
            )
        }
    }

    private companion object {

        const val SPLASH_DURATION_MILLIS =
            1_750L
    }
}