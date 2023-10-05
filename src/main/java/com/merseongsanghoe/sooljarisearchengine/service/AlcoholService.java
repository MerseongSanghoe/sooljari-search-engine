package com.merseongsanghoe.sooljarisearchengine.service;

import com.merseongsanghoe.sooljarisearchengine.DAO.AlcoholElasticsearchRepository;
import com.merseongsanghoe.sooljarisearchengine.DAO.AlcoholRepository;
import com.merseongsanghoe.sooljarisearchengine.DTO.AlcoholDTO;
import com.merseongsanghoe.sooljarisearchengine.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlcoholService {

    private final AlcoholRepository alcoholRepository;
    private final AlcoholElasticsearchRepository alcoholElasticsearchRepository;

    /**
     * title 검색어를 활용해 title 필드 타겟으로 elasticsearch에 검색 쿼리
     * @param title title 필드 타겟의 검색어
     * @param page 페이징 관련 변수가 담긴 Pageable 객체
     * @return responseBody에 바로 적재할 수 있는 형태의 Map 객체
     */
    public Map<String, Object> searchTitle(String title, Pageable page) {
        // elasticsearch 검색
        SearchHits<AlcoholDocument> searchHits = alcoholElasticsearchRepository.findByTitle(title, page).getSearchHits();

        // 리턴할 결과 Map 객체
        Map<String, Object> result = new HashMap<>();

        // 결과 개수
        result.put("count", searchHits.getTotalHits());

        // 결과 컨텐츠
        List<AlcoholDTO> alcoholDTOList = new ArrayList<>();
        for(SearchHit<AlcoholDocument> hit : searchHits) {
            AlcoholDTO dto = new AlcoholDTO();

            List<String> tags = new ArrayList<>();
            for (TagDocument tag : hit.getContent().getTags()) {
                tags.add(tag.getTitle());
            }

            dto.setScore(hit.getScore());
            dto.setId(hit.getContent().getAlcoholId());
            dto.setTitle(hit.getContent().getTitle());
            dto.setCategory(hit.getContent().getCategory());
            dto.setDegree(hit.getContent().getDegree());
            dto.setTags(tags);

            alcoholDTOList.add(dto);
        }
        result.put("data", alcoholDTOList);

        return result;
    }

    /**
     * 데이터베이스 속 alcohol 데이터 전부 elasticsearch에 인덱싱
     */
    @Transactional(readOnly = true)
    public void indexAll() {
        // TODO: 데이터가 많을 때 전체 인덱싱 성능 확인 필요
        List<Alcohol> alcohols = alcoholRepository.findAllWithSearchKeys();

        for (Alcohol alcohol : alcohols) {
            // alcohol 엔티티를 인덱싱할 형태로 변환
            AlcoholDocument alcoholDocument = new AlcoholDocument();

            alcoholDocument.setAlcoholId(alcohol.getId());
            alcoholDocument.setTitle(alcohol.getTitle());
            alcoholDocument.setCategory(alcohol.getCategory());
            alcoholDocument.setDegree(alcohol.getDegree());
            for (Tag tag : alcohol.getTags()) {
                alcoholDocument.getTags().add(new TagDocument(tag.getTitle()));
            }
            for (AlcSearchKey searchKey : alcohol.getSearchKeys()) {
                alcoholDocument.getSearchKeys().add(new AlcSearchKeyDocument(searchKey.getKey()));
            }

            alcoholElasticsearchRepository.save(alcoholDocument);
        }
    }
}
