package com.bbebig.searchserver.domain.history.repository;

import com.bbebig.searchserver.domain.history.domain.ChannelChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelChatMessageRepository extends MongoRepository<ChannelChatMessage, String> {

	Optional<ChannelChatMessage> findById(Long id);

	// Using 'id < messageId'
	@Query(value = "{ 'channelId': ?0, 'isDeleted': false, 'id': { $lt: ?1 } }",
			sort = "{ 'sequence': -1 }")
	List<ChannelChatMessage> findByChannelIdAndIdLessThan(
			Long channelId,
			Long messageId,
			Pageable pageable
	);

	// Using 'sequence < sequence'
	@Query(value = "{ 'channelId': ?0, 'isDeleted': false, 'sequence': { $lt: ?1 } }",
			sort = "{ 'sequence': -1 }")
	List<ChannelChatMessage> findPreviousMessages(
			Long channelId,
			Long sequence,
			Pageable pageable
	);

	List<ChannelChatMessage> findByChannelId(Long channelId, Pageable pageable);

	Optional<ChannelChatMessage> findTopByChannelIdAndDeletedFalseOrderByIdDesc(Long channelId);
}
