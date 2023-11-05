package com.merseongsanghoe.sooljarisearchengine.DAO;

import com.merseongsanghoe.sooljarisearchengine.document.AutoCompletionDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AutoCompletionElasticsearchRepository extends ElasticsearchRepository<AutoCompletionDocument, String> {
    List<AutoCompletionDocument> findByKeyword(String keyword, Pageable pageable);
}
