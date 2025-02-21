package com.smilegate.bbebig.presentation.ui.livechat.model

import com.smilegate.bbebig.domain.model.CandidateDomainModel

data class Candidate(
    val sdpMid: String,
    val sdpMLineIndex: Int,
    val candidate: String,
)

fun CandidateDomainModel.toUiModel(): Candidate = Candidate(
    sdpMid = sdpMid,
    sdpMLineIndex = sdpMLineIndex,
    candidate = candidate,
)

fun Candidate.toDomainModel(): CandidateDomainModel = CandidateDomainModel(
    sdpMid = sdpMid,
    sdpMLineIndex = sdpMLineIndex,
    candidate = candidate,
)