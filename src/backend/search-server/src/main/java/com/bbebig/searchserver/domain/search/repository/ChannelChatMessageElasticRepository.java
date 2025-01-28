package com.bbebig.searchserver.domain.search.repository;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelChatMessageElasticRepository extends ElasticsearchRepository<ChannelChatMessageElastic, String> {
}
