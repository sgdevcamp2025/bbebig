package com.smilegate.bbebig.presentation.ui.login.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smilegate.bbebig.presentation.ui.login.LoginScreen
import com.smilegate.bbebig.presentation.ui.login.LoginViewModel
import com.smilegate.bbebig.presentation.ui.login.model.UserAccount
import com.smilegate.bbebig.presentation.ui.login.mvi.LoginIntent
import com.smilegate.bbebig.presentation.ui.login.mvi.LoginSideEffect

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LoginScreen(
        uiState = uiState,
        modifier = modifier,
        onClickLoginConfirm = { email, password ->
            Log.d("LoginRoute", "onClickLoginConfirm: $email, $password")
            viewModel.handleIntent(LoginIntent.ClickLoginConfirm(UserAccount(email, password)))
        },
        onBackClick = { viewModel.handleIntent(LoginIntent.ClickBack) },
    )

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.NavigateToHome -> navigateToHome()
                is LoginSideEffect.NavigateToBack -> onBackClick()
                is LoginSideEffect.ShowLoginFailToast -> Toast.makeText(
                    context,
                    "Login Fail",
                    Toast.LENGTH_SHORT,
                ).show()

                LoginSideEffect.ShowLoginInfoSaveFailToast -> Toast.makeText(
                    context,
                    "Login Info Save Fail",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}
