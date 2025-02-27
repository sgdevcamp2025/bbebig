package com.smilegate.bbebig.data.datasource

import android.util.Log
import com.smilegate.bbebig.data.di.qulifier.ChatWebSocketUrl
import com.smilegate.bbebig.data.di.qulifier.StompWebSocketClient
import com.smilegate.bbebig.data.model.ChatDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketDataSourceImpl @Inject constructor(
    private val json: Json,
    @ChatWebSocketUrl private val chatWebSocketUrl: String,
    @StompWebSocketClient private val stompClient: StompClient,
    private val localDataSource: LocalDataSource,

) : WebSocketDataSource {

    @Singleton
    override lateinit var stompSession: StompSession

    override suspend fun connect(
        memberId: Long,
        roomType: String,
        channelId: Long,
        serverId: Long,
    ) {
        stompSession = stompClient.connect(
            url = chatWebSocketUrl,
            customStompConnectHeaders =
            mapOf(
                "accept-version" to "1.3,1.2,1.1,1.0",
                "Platform" to "Android",
                "MemberId" to "$memberId",
                "RoomType" to roomType,
                "Channel-Id" to "$channelId",
                "Server-Id" to "$serverId",
                "heart-beat" to "10000",
            ),
        )
    }

    override suspend fun disconnect() {
    }

    override suspend fun sendMessage(memberId: Long, chatDataModel: ChatDataModel) {
        Log.d("test2323", "send! $chatDataModel")
        stompSession
            .withJsonConversions(json)
            .convertAndSend(
                headers = StompSendHeaders(
                    destination = "/pub/channel/message",
                    customHeaders = mapOf(
                        "MemberId" to "$memberId",
                        "content-type" to "application/json",
                    ),
                ),
                body = chatDataModel,
                serializer = ChatDataModel.serializer().nullable,
            )
    }

    override suspend fun receiveMessage(
        serverId: Long,
        memberId: Long,
    ): Flow<ChatDataModel> {
        val flow = stompSession
            .withJsonConversions(json)
            .subscribe(
                headers = StompSubscribeHeaders(
                    destination = "/topic/server/$serverId",
                    customHeaders = mapOf(
                        "id" to "chat-{$memberId}",
                        "MemberId" to "$memberId",
                    ),
                ),
                deserializer = ChatDataModel.serializer(),
            )
        return flow
    }
}
