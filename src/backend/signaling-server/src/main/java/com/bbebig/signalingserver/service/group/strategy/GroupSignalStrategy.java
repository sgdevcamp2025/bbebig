package com.bbebig.signalingserver.service.group.strategy;

import com.bbebig.signalingserver.domain.SignalMessage;

public interface GroupSignalStrategy {
    void processGroupSignal(SignalMessage message, String sessionId);
}
