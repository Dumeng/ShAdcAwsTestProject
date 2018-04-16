package com.example.vincent.shadcmobiletest.amazonaws.mobile.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.auth.core.IdentityProvider
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration
import com.amazonaws.mobile.auth.ui.SignInActivity
import com.example.vincent.shadcmobiletest.R
import com.example.vincent.shadcmobiletest.amazonaws.mobile.base.BaseActivity


/**
 * Created by vincent on 30/03/2018.
 */

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        TAG = SplashActivity::class.java.simpleName
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        IdentityManager.getDefaultIdentityManager().resumeSession(this,
                StartupAuthResultHandler { authResults ->
                    if (authResults.isUserSignedIn) {
                        val provider = IdentityManager.getDefaultIdentityManager()

                        // If the user was  signed in previously with a provider,
                        // indicate that to them with a toast.
                        Toast.makeText(
                                this@SplashActivity, String.format("Signed in with %s",
                                provider.cachedUserID), Toast.LENGTH_LONG).show()
                        goMain(this@SplashActivity)
                        return@StartupAuthResultHandler

                    } else {
                        // Either the user has never signed in with a provider before
                        // or refresh failed with a previously signed in provider.

                        // Optionally, you may want to check if refresh
                        // failed for the previously signed in provider.

                        val errors = authResults.errorDetails

                        if (errors.didErrorOccurRefreshingProvider()) {
                            val providerAuthException = errors.providerRefreshException

                            // Credentials for previously signed-in provider could not be refreshed
                            // The identity provider name is available here using:
                            //     providerAuthException.getProvider().getDisplayName()

                        }

                        doSignIn(IdentityManager.getDefaultIdentityManager())
                        return@StartupAuthResultHandler
                    }
                }, 1000)
//
    }

    fun doSignIn(identityManager: IdentityManager) {

        identityManager.login(
                this@SplashActivity, object : DefaultSignInResultHandler() {

            override fun onSuccess(activity: Activity, identityProvider: IdentityProvider?) {
                if (identityProvider != null) {

                    // Sign-in succeeded
                    // The identity provider name is available here using:
                    //     identityProvider.getDisplayName()

                }

                // On Success of SignIn go to your startup activity
                activity.startActivity(Intent(activity, MainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }

            override fun onCancel(activity: Activity): Boolean {
                // Return false to prevent the user from dismissing
                // the sign in screen by pressing back button.
                // Return true to allow this.

                return false
            }
        })

        val config = AuthUIConfiguration.Builder()
                .userPools(true)  // true? show the Email and Password UI
//                .signInButton(FacebookButton::class.java) // Show Facebook button
//                .signInButton(GoogleButton::class.java) // Show Google button
                .logoResId(R.drawable.jnjlog) // Change the logo
//                .backgroundColor(Color.BLUE) // Change the backgroundColor
                .isBackgroundColorFullScreen(false) // Full screen backgroundColor the backgroundColor full screenff
                .fontFamily("sans-serif-light") // Apply sans-serif-light as the global font
                .canCancel(true)
                .build()

        val context = this@SplashActivity
        SignInActivity.startSignInActivity(context, config)
        this@SplashActivity.finish()
    }

    fun goMain(callingActivity: Activity) {
        callingActivity.startActivity(Intent(callingActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        callingActivity.finish()
    }
}

