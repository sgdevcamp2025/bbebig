package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.CandidateDataModel
import kotlinx.serialization.SerialName

data class CandidateDomainModel(
    val candidate: String,
    val sdpMid: String,
    val sdpMLineIndex: Int,
)

fun CandidateDataModel.toDomainModel(): CandidateDomainModel =
    CandidateDomainModel(
        candidate = candidate,
        sdpMid = sdpMid,
        sdpMLineIndex = sdpMLineIndex,
    )

fun CandidateDomainModel.toDataModel(): CandidateDataModel =
    CandidateDataModel(
        candidate = candidate,
        sdpMid = sdpMid,
        sdpMLineIndex = sdpMLineIndex,
    )
