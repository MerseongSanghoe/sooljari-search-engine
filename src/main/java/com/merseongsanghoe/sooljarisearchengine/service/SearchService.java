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
     * title 검색어를 활용해 title 필드 타겟으로 elasticsearch에 검색 쿼리
     * @param title title 필드 타겟의 검색어
     * @param page 페이징 관련 변수가 담긴 Pageable 객체
     * @return responseBody에 바로 적재할 수 있는 형태의 Map 객체
     */
    public Map<String, Object> searchTitle(String title, Pageable page) {
        // elasticsearch 검색
//        SearchHits<AlcoholDocument> searchHits = alcoholElasticsearchRepository.findByTitle(title, page).getSearchHits();
        Query searchQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("title")
                                .query(title)))
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
