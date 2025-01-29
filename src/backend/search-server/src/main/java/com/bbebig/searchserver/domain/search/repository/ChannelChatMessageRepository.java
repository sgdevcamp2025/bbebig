package com.bbebig.searchserver.domain.search.repository;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelChatMessageRepository extends MongoRepository<ChannelChatMessage, String> {

	Optional<ChannelChatMessage> findById(Long id);
}
