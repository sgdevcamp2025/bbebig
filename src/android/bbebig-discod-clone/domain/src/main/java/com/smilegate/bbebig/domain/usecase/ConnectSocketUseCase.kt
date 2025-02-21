//package com.smilegate.bbebig.domain.usecase
//
//import com.smilegate.bbebig.data.repository.WebSocketRepository
//import dagger.Reusable
//import kotlinx.serialization.json.Json
//import org.hildan.krossbow.stomp.StompSession
//import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
//import org.hildan.krossbow.stomp.headers.StompSendHeaders
//import org.hildan.krossbow.stomp.use
//import javax.inject.Inject
//
//@Reusable
//class ConnectSocketUseCase @Inject constructor(
//    private val webSocketRepository: WebSocketRepository,
//) {
//    suspend operator fun invoke(): Result<StompSession> {
//        return runCatching { webSocketRepository.connect() }
//    }
//}