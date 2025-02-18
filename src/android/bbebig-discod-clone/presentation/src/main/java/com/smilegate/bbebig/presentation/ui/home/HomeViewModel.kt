package com.smilegate.bbebig.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.smilegate.bbebig.domain.usecase.GetServerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getServerListUseCase: GetServerListUseCase,
) : ViewModel() {
    // TODO: 서버 목록 가져오기 로직 추가
}
