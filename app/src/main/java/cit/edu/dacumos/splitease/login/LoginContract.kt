package cit.edu.dacumos.splitease.login

interface LoginContract {
    interface View {
        fun showEmailError(message: String)
        fun showPasswordError(message: String)
        fun navigateToHome()
        fun navigateToRegister()
        fun showMessage(message: String)
    }

    interface Presenter {
        fun onSignInClicked(email: String, password: String)
        fun onGoogleSignInClicked()
        fun onCreateAccountClicked()
        fun checkLoginStatus()
    }
}
