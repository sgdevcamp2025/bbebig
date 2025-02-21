package com.smilegate.bbebig.data.datasource

import android.util.Log
import com.smilegate.bbebig.data.di.qulifier.StompWebSocketClient
import com.smilegate.bbebig.data.di.qulifier.WebSocketBaseUrl
import com.smilegate.bbebig.data.di.qulifier.WebSocketReceiveGroupPrefix
import com.smilegate.bbebig.data.di.qulifier.WebSocketSendGroupPrefix
import com.smilegate.bbebig.data.model.SignalMessageDataModel
import com.smilegate.bbebig.data.utils.UrlUtil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.use
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketDataSourceImpl @Inject constructor(
    private val json: Json,
    @StompWebSocketClient private val stompClient: StompClient,
    @WebSocketBaseUrl private val webSocketUrl: String,
    @WebSocketSendGroupPrefix private val sendGroupPrefix: String,
    @WebSocketReceiveGroupPrefix private val receiveGroupPrefix: String,
) : WebSocketDataSource {

    override lateinit var stompSession: StompSession

    override suspend fun connect() {
        stompSession = stompClient.connect(webSocketUrl)
        stompSession
            .withJsonConversions(json)
            .convertAndSend(
                headers = StompSendHeaders(destination = sendGroupPrefix),
                body = SignalMessageDataModel(
                    messageType = "JOIN_CHANNEL",
                    channelId = "0",
                    senderId = "1",
                    null, null, null,
                ),
                serializer = SignalMessageDataModel.serializer().nullable,
            )

        coroutineScope {
            launch {
                stompSession.withJsonConversions(json).subscribe(
                    destination = UrlUtil.parseUserUrl(
                        userId = "1",
                    ),
                    deserializer = SignalMessageDataModel.serializer(),
                ).collect {
                    Timber.d("SingalTest : $it")
                }
            }

            launch {
                stompSession.withJsonConversions(json).subscribe(
                    destination = UrlUtil.parseChannelUrl(
                        baseUrl = receiveGroupPrefix,
                        channelId = "0",
                    ),
                    deserializer = SignalMessageDataModel.serializer(),
                ).collect {
                    Timber.d("SingalTest : $it")
                }
            }
        }
    }


    override suspend fun sendChannelStateGroupMessage(signalMessage: SignalMessageDataModel) {
            stompSession
                .withJsonConversions(json)
                .convertAndSend(
                    headers = StompSendHeaders(destination = sendGroupPrefix),
                    body = signalMessage,
                    serializer = SignalMessageDataModel.serializer().nullable,
                )

    }

    override suspend fun offerSdp(signalMessage: SignalMessageDataModel) {
        stompSession.withJsonConversions(json)
                .convertAndSend(
                    headers = StompSendHeaders(destination = sendGroupPrefix),
                    body = signalMessage,
                    serializer = SignalMessageDataModel.serializer(),
                )


    }

    override suspend fun offerIceCandidate(signalMessage: SignalMessageDataModel) {
    }

    override fun receiveChannelGroupMessage(channelId: String): Flow<SignalMessageDataModel> {
        return flow {
            stompSession
                    .withJsonConversions(json)
                    .subscribe(
                        destination = UrlUtil.parseChannelUrl(
                            baseUrl = receiveGroupPrefix,
                            channelId = channelId,
                        ),
                        deserializer = SignalMessageDataModel.serializer(),
                    )
                    .map {
                        emit(it)
                    }

        }
    }

    override fun receiveUserGroupMessage(userId: String): Flow<SignalMessageDataModel> {
        return flow {
            stompSession.use { session ->
                session
                    .withJsonConversions(json)
                    .subscribe(
                        destination = UrlUtil.parseUserUrl(userId = "1"),
                        deserializer = SignalMessageDataModel.serializer(),
                    )
                    .map {
                        emit(it)
                    }
            }
        }
    }
}