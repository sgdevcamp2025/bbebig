package com.bbebig.searchserver.domain.search.repository;

import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmChatMessageRepository extends MongoRepository<DmChatMessage, String> {
}
