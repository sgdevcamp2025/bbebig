package com.smilegate.bbebig.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilegate.bbebig.domain.usecase.GetServerListUseCase
import com.smilegate.bbebig.domain.usecase.SendGroupChannelStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getServerListUseCase: GetServerListUseCase,
) : ViewModel() {
    // TODO: 서버 목록 가져오기 로직 추가
}
