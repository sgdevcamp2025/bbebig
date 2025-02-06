package com.bbebig.searchserver.domain.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
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
import java.util.Optional;

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
			SearchRequest searchRequest = buildSearchRequest(
					CHANNEL_CHAT_INDEX,
					buildFilterQueries(requestDto, "serverId", serverId)
			);
			return executeSearch(searchRequest, pageable, ChannelChatMessageElastic.class);
		} catch (IOException e) {
			log.error("Elasticsearch 검색 중 오류 발생 (Channel)", e);
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
			SearchRequest searchRequest = buildSearchRequest(
					DM_CHAT_INDEX,
					buildFilterQueries(requestDto, "channelId", channelId)
			);
			return executeSearch(searchRequest, pageable, DmChatMessageElastic.class);
		} catch (IOException e) {
			log.error("Elasticsearch 검색 중 오류 발생 (DM)", e);
			return Page.empty();
		}
	}

	/**
	 * 검색 옵션들을 분석하여 필터용 Query 리스트 생성
	 */
	private List<Query> buildFilterQueries(SearchMessageRequestDto requestDto, String idField, Long idValue) {
		List<Query> filterQueries = new ArrayList<>();

		// [1] ID 필터링(서버ID, 채널ID 등)
		if (idValue != null) {
			Query idQuery = new Query.Builder()
					.term(t -> t
							.field(idField)
							.value(idValue)
					)
					.build();
			filterQueries.add(idQuery);
		}

		// [2] 옵션별 필터 쌓기
		List<SearchOption> options = requestDto.getOptions();
		String keyword = requestDto.getKeyword();
		Long senderId = requestDto.getSenderId();

		// 내용 검색 (CONTENT)
		if (options.contains(SearchOption.CONTENT) && keyword != null && !keyword.isEmpty()) {
			Query contentQuery = new Query.Builder()
					.match(m -> m
							.field("content")
							.query(keyword)
					)
					.build();
			filterQueries.add(contentQuery);
		}

		// 작성자 검색 (AUTHOR)
		if (options.contains(SearchOption.AUTHOR) && senderId != null) {
			Query authorQuery = new Query.Builder()
					.term(t -> t
							.field("sendMemberId")
							.value(senderId)
					)
					.build();
			filterQueries.add(authorQuery);
		}

		// 특정 날짜 이전 (BEFORE) → untyped .to(...)
		if (options.contains(SearchOption.BEFORE) && requestDto.getBeforeDate() != null) {
			String beforeStr = localDateTimeToString(requestDto.getBeforeDate());

			RangeQuery beforeRange = RangeQuery.of(rq ->
					rq.untyped(ut -> ut
							.field("createdAt")
							.to(JsonData.fromJson(beforeStr))   // => createdAt <= beforeStr
					)
			);

			Query beforeQuery = Query.of(qb -> qb.range(beforeRange));
			filterQueries.add(beforeQuery);
		}

		// 특정 날짜 이후 (AFTER) → untyped .from(...)
		if (options.contains(SearchOption.AFTER) && requestDto.getAfterDate() != null) {
			String afterStr = localDateTimeToString(requestDto.getAfterDate());

			RangeQuery afterRange = RangeQuery.of(rq ->
					rq.untyped(ut -> ut
							.field("createdAt")
							.from(JsonData.fromJson(afterStr))  // => createdAt >= afterStr
					)
			);

			Query afterQuery = Query.of(qb -> qb.range(afterRange));
			filterQueries.add(afterQuery);
		}

		// 특정 날짜 하루만 (EXACT) → untyped .from(...) .to(...)
		if (options.contains(SearchOption.EXACT) && requestDto.getExactDate() != null) {
			LocalDateTime date = requestDto.getExactDate();

			String startOfDay = localDateTimeToString(date.withHour(0).withMinute(0).withSecond(0));
			String endOfDay   = localDateTimeToString(date.withHour(23).withMinute(59).withSecond(59));

			RangeQuery exactRange = RangeQuery.of(rq ->
					rq.untyped(ut -> ut
							.field("createdAt")
							.from(JsonData.fromJson(startOfDay)) // >= startOfDay
							.to(JsonData.fromJson(endOfDay))     // <= endOfDay
					)
			);

			Query exactQuery = Query.of(qb -> qb.range(exactRange));
			filterQueries.add(exactQuery);
		}

		return filterQueries;
	}

	/**
	 * bool 쿼리로 필터링 조건을 묶어 SearchRequest 생성
	 */
	private SearchRequest buildSearchRequest(String index, List<Query> filterQueries) {
		Query boolQuery = new Query.Builder()
				.bool(b -> b.filter(filterQueries))
				.build();

		return new SearchRequest.Builder()
				.index(index)
				.query(boolQuery)
				.build();
	}

	/**
	 * 검색 실행 후 Page 형태로 결과 변환
	 */
	private <T> Page<T> executeSearch(SearchRequest searchRequest, PageRequest pageable, Class<T> clazz) throws IOException {
		SearchResponse<T> response = elasticsearchClient.search(searchRequest, clazz);

		List<T> results = new ArrayList<>();
		for (Hit<T> hit : response.hits().hits()) {
			results.add(hit.source());
		}

		long totalHits = Optional.ofNullable(response.hits().total())
				.map(t -> t.value())
				.orElse(0L);

		return new PageImpl<>(results, pageable, totalHits);
	}

	/**
	 * 검색 옵션 유효성 검사
	 */
	private void checkOptionValid(List<SearchOption> options) {
		if (options == null || options.isEmpty()) {
			log.warn("검색 옵션이 비어 있습니다. 검색을 수행할 수 없습니다.");
			throw new IllegalArgumentException("At least one search option must be provided.");
		}
	}

	/**
	 * LocalDateTime → Elasticsearch가 이해할 수 있는 문자열(ISO8601) 변환
	 */
	private String localDateTimeToString(LocalDateTime dateTime) {
		return dateTime.toString(); // 예) 2025-02-05T12:34:56
	}
}
