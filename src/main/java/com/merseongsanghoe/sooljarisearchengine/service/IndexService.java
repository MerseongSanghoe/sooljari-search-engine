package com.merseongsanghoe.sooljarisearchengine.service;

import com.merseongsanghoe.sooljarisearchengine.DAO.AlcoholElasticsearchRepository;
import com.merseongsanghoe.sooljarisearchengine.DAO.AlcoholRepository;
import com.merseongsanghoe.sooljarisearchengine.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final AlcoholRepository alcoholRepository;
    private final AlcoholElasticsearchRepository alcoholElasticsearchRepository;

    /**
     * 데이터베이스 속 alcohol 데이터 전부 elasticsearch에 인덱싱
     */
    @Transactional(readOnly = true)
    public void indexAll() {
        // TODO: 데이터가 많을 때 전체 인덱싱 성능 확인 필요
        List<Alcohol> alcohols = alcoholRepository.findAllWithSearchKeys();

        for (Alcohol alcohol : alcohols) {
            // alcohol 엔티티를 엘라스틱서치 도큐먼트인 alcoholDocument 객체로 변환
            AlcoholDocument alcoholDocument = AlcoholDocument.builder()
                    .alcoholId(alcohol.getId())
                    .title(alcohol.getTitle())
                    .category(alcohol.getCategory())
                    .degree(alcohol.getDegree())
                    .build();

            for (Tag tag : alcohol.getTags()) {
                alcoholDocument.addTag(new TagDocument(tag.getTitle()));
            }
            for (AlcSearchKey searchKey : alcohol.getSearchKeys()) {
                alcoholDocument.addSearchKey(new AlcSearchKeyDocument(searchKey.getKey()));
            }

            alcoholElasticsearchRepository.save(alcoholDocument);
        }
    }
}
