package com.bbebig.searchserver.domain.search.repository;

import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmChatMessageElasticRepository extends ElasticsearchRepository<DmChatMessageElastic, String> {
}
