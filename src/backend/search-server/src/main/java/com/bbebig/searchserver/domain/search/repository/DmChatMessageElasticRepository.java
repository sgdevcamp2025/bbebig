package com.bbebig.searchserver.domain.search.repository;

import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DmChatMessageElasticRepository extends ElasticsearchRepository<DmChatMessageElastic, Long> {

	Page<DmChatMessageElastic> findByContentContaining(String keyword, Pageable pageable);

	Page<DmChatMessageElastic> findByContentContainingAndChannelId(
			String keyword, Long channelId, Pageable pageable);

	Page<DmChatMessageElastic> findByCreatedAtBetweenAndChannelId(LocalDateTime start, LocalDateTime end, Long channelId, Pageable pageable);

	Page<DmChatMessageElastic> findByContentContainingAndChannelIdAndCreatedAtBetween(
			String keyword, Long channelId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
