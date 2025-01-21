package com.smilegate.bbebig.presentation.ui.signup.age.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.smilegate.bbebig.presentation.ui.signup.SignUpViewModel
import com.smilegate.bbebig.presentation.ui.signup.age.AgeScreen
import com.smilegate.bbebig.presentation.ui.signup.age.InputAgeViewModel
import com.smilegate.bbebig.presentation.ui.signup.age.mvi.InputAgeIntent
import com.smilegate.bbebig.presentation.ui.signup.age.mvi.InputAgeSideEffect
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpIntent
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpSideEffect

@Composable
fun AgeRoute(
    navBackStackEntry: NavBackStackEntry,
    navigateToHome: () -> Unit,
    onBackClick: () -> Unit,
    sharedViewModel: SignUpViewModel = hiltViewModel(navBackStackEntry),
    viewModel: InputAgeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    AgeScreen(
        onBackClick = { viewModel.handleIntent(InputAgeIntent.ClickBack) },
        onClickConfirm = {
            viewModel.handleIntent(InputAgeIntent.ClickMakeAccount(it))
        },
    )

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is InputAgeSideEffect.NavigateToBack -> {
                    onBackClick()
                }

                InputAgeSideEffect.NavigateToHome -> {
                    sharedViewModel.handleIntent(SignUpIntent.ConfirmBirth(uiState.birth))
                }
            }
        }
    }

    LaunchedEffect(true) {
        Log.d("AgeRoute", "LaunchedEffect: ${sharedViewModel.uiState.value}")
        sharedViewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.NavigateToHome -> {
                    // navigateToHome()
                    Log.d("AgeRoute", "NavigateToHome: ${sharedViewModel.uiState.value}")
                }

                is SignUpSideEffect.ShowErrorToast -> {
                    Toast.makeText(
                        context,
                        sideEffect.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }
}
