//package com.smilegate.bbebig.data.repository
//
//import com.smilegate.bbebig.data.di.qulifier.StompWebSocketClient
//import com.smilegate.bbebig.data.di.qulifier.WebSocketBaseUrl
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.Json
//import org.hildan.krossbow.stomp.StompClient
//import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
//import org.hildan.krossbow.stomp.headers.StompSendHeaders
//import org.hildan.krossbow.stomp.use
//import javax.inject.Inject
//
//class SocketRepository @Inject constructor(
//    @StompWebSocketClient private val stompClient: StompClient,
//    @WebSocketBaseUrl private val webSocketUrl: String,
//    private val json: Json
//) {
//    @OptIn(ExperimentalSerializationApi::class)
//    suspend fun connectWebSocket() {
//        stompClient.connect(webSocketUrl).use { session ->
//            session.withJsonConversions(json)
//                .convertAndSend(
//                    headers = StompSendHeaders(destination = "/pub/stream/group"),
//                    body = SendMessage("JOIN_CHANNEL", "group", "user1"),
//                    serializer = SendMessage.serializer(),
//                )
//        }
//    }
//}
//
//@Serializable
//data class SendMessage(
//    @SerialName("messageType")
//    val messageType: String,
//    @SerialName("channelId")
//    val channelId: String,
//    @SerialName("message")
//    val senderId: String,
//)