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
	public Page<ChannelChatMessageElastic> searchChannelMessagesByOptions(
			ServerChannelChatSearchRequestDto requestDto, Long serverId) {
		PageRequest pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

		if (requestDto.getOptions() == null || requestDto.getOptions().isEmpty()) {
			throw new IllegalArgumentException("At least one search option must be provided.");
		}

		try {
			SearchRequest searchRequest = createSearchRequest(
					CHANNEL_CHAT_INDEX,
					requestDto.getKeyword(),
					requestDto.getSenderId(),
					requestDto.getDate(),
					requestDto.getOptions(),
					"serverId",
					serverId
			);

			SearchResponse<ChannelChatMessageElastic> response = elasticsearchClient.search(searchRequest, ChannelChatMessageElastic.class);

			List<ChannelChatMessageElastic> results = new ArrayList<>();
			for (Hit<ChannelChatMessageElastic> hit : response.hits().hits()) {
				results.add(hit.source());
			}

			return new PageImpl<>(results, pageable, response.hits().total().value());

		} catch (IOException e) {
			log.error("Elasticsearch 검색 중 오류 발생", e);
			return Page.empty();
		}
	}

	/**
	 * DM 채팅 검색 (검색 옵션 조합 가능)
	 */
	public Page<DmChatMessageElastic> searchDmMessagesByOptions(
			DmChatSearchRequestDto requestDto, Long channelId) {
		PageRequest pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

		if (requestDto.getOptions() == null || requestDto.getOptions().isEmpty()) {
			throw new IllegalArgumentException("At least one search option must be provided.");
		}

		try {
			SearchRequest searchRequest = createSearchRequest(
					DM_CHAT_INDEX,
					requestDto.getKeyword(),
					requestDto.getSenderId(),
					requestDto.getDate(),
					requestDto.getOptions(),
					"channelId",
					channelId
			);

			SearchResponse<DmChatMessageElastic> response = elasticsearchClient.search(searchRequest, DmChatMessageElastic.class);

			List<DmChatMessageElastic> results = new ArrayList<>();
			for (Hit<DmChatMessageElastic> hit : response.hits().hits()) {
				results.add(hit.source());
			}

			return new PageImpl<>(results, pageable, response.hits().total().value());

		} catch (IOException e) {
			log.error("Elasticsearch 검색 중 오류 발생", e);
			return Page.empty();
		}
	}

	/**
	 * 검색 요청 생성 (Elasticsearch Java API Client 사용)
	 */
	private SearchRequest createSearchRequest(String index, String keyword, Long senderId, LocalDateTime date,
											  List<SearchOption> options, String idField, Long idValue) throws IOException {
		List<Query> filterQueries = new ArrayList<>();

		// 키워드 검색 (CONTENT) - 선택적 조건
		if (options.contains(SearchOption.CONTENT) && keyword != null && !keyword.isEmpty()) {
			filterQueries.add(Query.of(q -> q.match(m -> m.field("content").query(keyword))));
		}

		// 보낸 사람 검색 (AUTHOR) - 선택적 조건
		if (options.contains(SearchOption.AUTHOR) && senderId != null) {
			filterQueries.add(Query.of(q -> q.term(t -> t.field("sendMemberId").value(senderId))));
		}

		// 날짜 검색 (BEFORE, AFTER, EXACT, 특정 기간) - 선택적 조건
		if (date != null) {
			if (options.contains(SearchOption.BEFORE)) {
				filterQueries.add(Query.of(q -> q.range(r -> r.field("createdAt").lte(JsonData.of(date)))));
			}

			if (options.contains(SearchOption.AFTER)) {
				filterQueries.add(Query.of(q -> q.range(r -> r.field("createdAt").gte(JsonData.of(date)))));
			}

			if (options.contains(SearchOption.EXACT)) {
				filterQueries.add(Query.of(q ->
						q.range(r ->
								r.field("createdAt")
										.gte(JsonData.of(date.withHour(0).withMinute(0).withSecond(0)))
										.lte(JsonData.of(date.withHour(23).withMinute(59).withSecond(59)))
						)
				));
			}

			if (options.contains(SearchOption.BEFORE) && options.contains(SearchOption.AFTER)) {
				filterQueries.add(Query.of(q -> q.range(r -> r.field("createdAt")
						.gte(JsonData.of(date.minusDays(1)))
						.lte(JsonData.of(date.plusDays(1))))));
			}
		}

		// ID 필터링 (채널 ID 또는 서버 ID) - 선택적 조건
		if (idValue != null) {
			filterQueries.add(Query.of(q -> q.term(t -> t.field(idField).value(idValue))));
		}

		// 모든 조건이 없을 경우 예외 처리
		if (filterQueries.isEmpty()) {
			throw new IllegalArgumentException("At least one search option must be provided.");
		}

		return SearchRequest.of(s -> s
				.index(index)
				.query(q -> q.bool(b -> b
						.filter(filterQueries)
				)));
	}



}
