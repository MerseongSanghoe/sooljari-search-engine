package com.merseongsanghoe.sooljarisearchengine.service;

import com.merseongsanghoe.sooljarisearchengine.DAO.AlcoholElasticsearchRepository;
import com.merseongsanghoe.sooljarisearchengine.DAO.AutoCompletionElasticsearchRepository;
import com.merseongsanghoe.sooljarisearchengine.DTO.SearchResultDTO;
import com.merseongsanghoe.sooljarisearchengine.document.AlcoholDocument;
import com.merseongsanghoe.sooljarisearchengine.document.AutoCompletionDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final AlcoholElasticsearchRepository alcoholElasticsearchRepository;
    private final AutoCompletionElasticsearchRepository autoCompletionElasticsearchRepository;

    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 주어진 검색 키워드로 elasticsearch에 검색 쿼리
     * @param title title, tag 필드 타겟 검색어
     * @param tag tag 필드 타겟 검색어
     * @param page 페이징 관련 변수가 담긴 Pageable 객체
     * @return responseBody에 바로 적재할 수 있는 형태의 Map 객체
     */
    public Map<String, Object> search(String title, String tag, Pageable page) {
        // elasticsearch 검색
        Query searchQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s
                                        .multiMatch(m -> m
                                            .fields("title", "tags.title")
                                            .query(title)))
                                .should(s -> s
                                        .match(m -> m
                                                .field("tags.title")
                                                .query(tag)))))
                .withPageable(page)
                .build();
        SearchHits<AlcoholDocument> searchHits = elasticsearchOperations.search(searchQuery, AlcoholDocument.class);

        // 리턴할 결과 Map 객체
        Map<String, Object> result = new HashMap<>();

        // 검색 결과 메타데이터
        result.put("count", searchHits.getTotalHits());
        result.put("page", page.getPageNumber());
        result.put("size", page.getPageSize());

        // 결과 컨텐츠
        List<SearchResultDTO> searchResultDTOList = new ArrayList<>();
        for(SearchHit<AlcoholDocument> hit : searchHits) {
            SearchResultDTO dto = SearchResultDTO.of(hit.getScore(), hit.getContent());

            searchResultDTOList.add(dto);
        }
        result.put("data", searchResultDTOList);

        return result;
    }

    /**
     * keyword를 받아서 검색어 자동완성 결과 반환
     * @param keyword
     * @param page
     * @return
     */
    public Map<String, Object> getAutoCompletion(String keyword, Pageable page) {
        List<AutoCompletionDocument> result = autoCompletionElasticsearchRepository.findByKeyword(keyword, page);

        Map<String, Object> response = new HashMap<>();
        response.put("data",
                result.stream()
                        .map(AutoCompletionDocument::getKeyword)
                        .collect(Collectors.toList()));

        return response;
    }
}
