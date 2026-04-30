package cit.edu.dacumos.splitease.login

import android.content.Context
import android.content.SharedPreferences

class LoginPresenter(
    private val view: LoginContract.View,
    private val context: Context
) : LoginContract.Presenter {

    private val prefs: SharedPreferences = context.getSharedPreferences("SplitEasePrefs", Context.MODE_PRIVATE)

    override fun onSignInClicked(email: String, password: String) {
        if (email.isEmpty()) {
            view.showEmailError("Please enter your email")
            return
        }
        if (!email.endsWith("@gmail.com")) {
            view.showEmailError("Please enter a valid Gmail address")
            return
        }
        if (password.isEmpty()) {
            view.showPasswordError("Please enter your password")
            return
        }
        if (password.length < 8) {
            view.showPasswordError("Password must be at least 8 characters")
            return
        }

        // Mock Authentication
        prefs.edit()
            .putBoolean("isLoggedIn", true)
            .putString("userEmail", email)
            .putString("userName", email.substringBefore("@"))
            .apply()

        view.navigateToHome()
    }

    override fun onGoogleSignInClicked() {
        prefs.edit()
            .putBoolean("isLoggedIn", true)
            .putString("userEmail", "google@gmail.com")
            .putString("userName", "Google User")
            .apply()
        view.navigateToHome()
    }

    override fun onCreateAccountClicked() {
        view.navigateToRegister()
    }

    override fun checkLoginStatus() {
        if (prefs.getBoolean("isLoggedIn", false)) {
            view.navigateToHome()
        }
    }
}
