package com.bbebig.searchserver.domain.search.repository;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ChannelChatMessageElasticRepository extends ElasticsearchRepository<ChannelChatMessageElastic, Long> {

	Page<ChannelChatMessageElastic> findByContentContainingAndServerId(
			String keyword, Long serverId, Pageable pageable);

}
