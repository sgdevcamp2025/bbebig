package com.bbebig.searchserver.domain.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
import com.bbebig.searchserver.domain.search.dto.SearchOption;
import com.bbebig.searchserver.domain.search.dto.SearchRequestDto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

	private final ElasticsearchClient elasticsearchClient;

	private static final String CHANNEL_CHAT_INDEX = "channel_chat_messages";
	private static final String DM_CHAT_INDEX = "dm_chat_messages";

	/**
	 * 서버 기반 채널 채팅 검색 (검색 옵션 조합 가능)
	 */
	public Page<ChannelChatMessageElastic> searchChannelMessagesByOptions(SearchMessageRequestDto requestDto, Long serverId) {
		PageRequest pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

		checkOptionValid(requestDto.getOptions());

		try {
			SearchRequest searchRequest = createSearchRequest(CHANNEL_CHAT_INDEX, requestDto, "serverId", serverId);
			return executeSearch(searchRequest, pageable, ChannelChatMessageElastic.class);
		} catch (IOException e) {
			log.error("Elasticsearch 검색 중 오류 발생", e);
			return Page.empty();
		}
	}

	/**
	 * DM 채팅 검색 (검색 옵션 조합 가능)
	 */
	public Page<DmChatMessageElastic> searchDmMessagesByOptions(SearchMessageRequestDto requestDto, Long channelId) {
		PageRequest pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

		checkOptionValid(requestDto.getOptions());

		try {
			SearchRequest searchRequest = createSearchRequest(DM_CHAT_INDEX, requestDto, "channelId", channelId);
			return executeSearch(searchRequest, pageable, DmChatMessageElastic.class);
		} catch (IOException e) {
			log.error("Elasticsearch 검색 중 오류 발생", e);
			return Page.empty();
		}
	}

	/**
	 * 검색 요청 생성 (Elasticsearch Java API Client 사용)
	 */
	private SearchRequest createSearchRequest(String index, SearchMessageRequestDto requestDto, String idField, Long idValue) {
		List<Query> filterQueries = new ArrayList<>();

		applySearchOptions(filterQueries, requestDto);

		// ID 필터링 (채널 ID 또는 서버 ID) - 필수 조건
		if (idValue != null) {
			filterQueries.add(Query.of(q -> q.term(t -> t.field(idField).value(idValue))));
		}

		return SearchRequest.of(s -> s
				.index(index)
				.query(q -> q.bool(b -> b.filter(filterQueries))));
	}

	/**
	 * 채널 검색 옵션 적용 (키워드, 날짜, 보낸 사람)
	 */
	private void applySearchOptions(List<Query> filterQueries, SearchMessageRequestDto requestDto) {
		String keyword = requestDto.getKeyword();
		Long senderId = requestDto.getSenderId();
		List<SearchOption> options = requestDto.getOptions();

		if (options.contains(SearchOption.CONTENT) && keyword != null && !keyword.isEmpty()) {
			filterQueries.add(Query.of(q ->
					q.match(m -> m.field("content").query(keyword))
			));
		}

		if (options.contains(SearchOption.AUTHOR) && senderId != null) {
			filterQueries.add(Query.of(q ->
					q.term(t -> t.field("sendMemberId").value(senderId))
			));
		}

		if (options.contains(SearchOption.BEFORE) && requestDto.getBeforeDate() != null) {
			filterQueries.add(Query.of(q ->
					q.range(r -> r.field("createdAt").lte(JsonData.of(requestDto.getBeforeDate())))
			));
		}

		if (options.contains(SearchOption.AFTER) && requestDto.getAfterDate() != null) {
			filterQueries.add(Query.of(q ->
					q.range(r -> r.field("createdAt").gte(JsonData.of(requestDto.getAfterDate())))
			));
		}

		if (options.contains(SearchOption.EXACT) && requestDto.getExactDate() != null) {
			filterQueries.add(Query.of(q ->
					q.range(r -> r.field("createdAt")
							.gte(JsonData.of(requestDto.getExactDate().withHour(0).withMinute(0).withSecond(0)))
							.lte(JsonData.of(requestDto.getExactDate().withHour(23).withMinute(59).withSecond(59)))
					)
			));
		}
	}

	private <T> Page<T> executeSearch(SearchRequest searchRequest, PageRequest pageable, Class<T> clazz) throws IOException {
		SearchResponse<T> response = elasticsearchClient.search(searchRequest, clazz);

		List<T> results = new ArrayList<>();
		for (Hit<T> hit : response.hits().hits()) {
			results.add(hit.source());
		}

		return new PageImpl<>(results, pageable, response.hits().total().value());
	}

	private void checkOptionValid(List<SearchOption> options) {
		if (options == null || options.isEmpty()) {
			throw new IllegalArgumentException("At least one search option must be provided.");
		}
	}
}
