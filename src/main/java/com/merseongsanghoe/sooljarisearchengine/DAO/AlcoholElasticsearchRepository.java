package com.merseongsanghoe.sooljarisearchengine.DAO;

import com.merseongsanghoe.sooljarisearchengine.document.AlcoholDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AlcoholElasticsearchRepository extends ElasticsearchRepository<AlcoholDocument, Long> {
    SearchPage<AlcoholDocument> findByTitle(String title, Pageable page);
}
