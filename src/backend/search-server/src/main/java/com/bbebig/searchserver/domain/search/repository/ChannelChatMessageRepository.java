package com.bbebig.searchserver.domain.search.repository;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelChatMessageRepository extends MongoRepository<ChannelChatMessage, String> {
}
