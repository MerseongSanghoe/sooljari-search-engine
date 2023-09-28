package com.merseongsanghoe.sooljarisearchengine.DAO;

import com.merseongsanghoe.sooljarisearchengine.domain.AlcoholDocument;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AlcoholElasticsearchRepository extends ElasticsearchRepository<AlcoholDocument, String> {
    SearchHits<AlcoholDocument> findByTitle(String title);
}
