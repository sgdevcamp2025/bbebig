package com.smilegate.bbebig.presentation.ui.livechat

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smilegate.bbebig.data.datasource.WebRTCDataSource
import com.smilegate.bbebig.data.datasource.WebSocketDataSource
import com.smilegate.bbebig.data.model.SDP
import com.smilegate.bbebig.data.model.SignalMessageDataModel
import com.smilegate.bbebig.data.webrtc.StreamConnection
import com.smilegate.bbebig.domain.usecase.ReceiveChannelGroupMessageUseCase
import com.smilegate.bbebig.domain.usecase.ReceiveUserGroupMessageUseCase
import com.smilegate.bbebig.domain.usecase.SendGroupChannelStateUseCase
import com.smilegate.bbebig.presentation.ui.livechat.model.SignalMessage
import com.smilegate.bbebig.presentation.ui.livechat.model.SignalMessageType
import com.smilegate.bbebig.presentation.ui.livechat.model.toDomainModel
import com.smilegate.bbebig.presentation.webrtc.audio.AudioHandler
import com.smilegate.bbebig.presentation.webrtc.audio.AudioSwitchHandler
import com.smilegate.bbebig.presentation.webrtc.peer.StreamPeerConnection
import com.smilegate.bbebig.presentation.webrtc.peer.StreamPeerConnectionFactory
import com.smilegate.bbebig.presentation.webrtc.peer.StreamPeerType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerationAndroid
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStreamTrack
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.RTCConfiguration
import org.webrtc.PeerConnectionFactory
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoCapturer
import org.webrtc.VideoTrack
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LiveChatViewModel @Inject constructor(
    //  private val connectSocketUseCase: ConnectSocketUseCase,
    private val sendGroupChannelStateUseCase: SendGroupChannelStateUseCase,
    private val receiveUserGroupMessageUseCase: ReceiveUserGroupMessageUseCase,
    private val receiveChannelGroupMessageUseCase: ReceiveChannelGroupMessageUseCase,
    private val dataSource: WebSocketDataSource,

    @ApplicationContext private val context: Context,
) : ViewModel() {


    init {
        viewModelScope.launch {
            dataSource.connect()
            dataSource.receiveUserGroupMessage("1").collect {
                Timber.d("receiveUserGroupMessage: $it")
            }
        }
    }


    fun sendSdp(sdp: SessionDescription) {
        viewModelScope.launch {
            dataSource.offerSdp(
                SignalMessageDataModel(
                    messageType = SignalMessageType.OFFER.name,
                    channelId = "11",
                    receiverId = "",
                    senderId = "2",
                    sdp = SDP(sdp = sdp.description),
                    candidate = null,
                ),
            )
        }
    }

    fun sendIceCandidate(iceCandidate: IceCandidate) {
//        val signalMessage = SignalMessage(
//            type = SignalMessageType.ICE,
//            data = iceCandidate,
//        )
//        sendSignalMessage(signalMessage)
    }
}