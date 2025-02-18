package com.bbebig.signalingserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignalMessage {

    private MessageType messageType;
    private String channelId;           // 체날 ID
    private String senderId;            // 보낸 사람 ID
    private String receiverId;          // 받는 사람 ID
    private Object sdp;                 // SDP
    private Object candidate;           // ICE candidate
}
