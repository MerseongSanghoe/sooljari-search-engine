package com.merseongsanghoe.sooljarisearchengine.service;

import com.merseongsanghoe.sooljarisearchengine.DAO.AlcoholElasticsearchRepository;
import com.merseongsanghoe.sooljarisearchengine.DAO.AlcoholNodeRepository;
import com.merseongsanghoe.sooljarisearchengine.DAO.AlcoholRepository;
import com.merseongsanghoe.sooljarisearchengine.DAO.AutoCompletionElasticsearchRepository;
import com.merseongsanghoe.sooljarisearchengine.document.AlcoholDocument;
import com.merseongsanghoe.sooljarisearchengine.document.AutoCompletionDocument;
import com.merseongsanghoe.sooljarisearchengine.entity.Alcohol;
import com.merseongsanghoe.sooljarisearchengine.exception.AlcoholDocumentNotFoundException;
import com.merseongsanghoe.sooljarisearchengine.exception.AlcoholNodeNotFoundException;
import com.merseongsanghoe.sooljarisearchengine.exception.AlcoholNotFoundException;
import com.merseongsanghoe.sooljarisearchengine.exception.CompletionKeywordDuplicatedException;
import com.merseongsanghoe.sooljarisearchengine.node.AlcoholNode;
import com.merseongsanghoe.sooljarisearchengine.util.IndexUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.reindex.ReindexRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndexService {

    private final AlcoholRepository alcoholRepository;

    private final AlcoholElasticsearchRepository alcoholElasticsearchRepository;
    private final AutoCompletionElasticsearchRepository autoCompletionElasticsearchRepository;

    private final AlcoholNodeRepository alcoholNodeRepository;

    private final ElasticsearchOperations elasticsearchOperations;

    private final String ALCOHOL_INDEX_NAME = "alcohols";
    private final String AUTO_COMPLETION_INDEX_NAME = "auto-completion";

    /**
     * 모든 alcohol 엔티티 객체와 그 객체들의 연관관계에 놓인 엔티티를
     * 한 번에 전부 불러오는 함수
     */
    @Transactional(readOnly = true)
    private List<Alcohol> getAllAlcohols() {
        List<Alcohol> alcohols = alcoholRepository.findAllWithSearchKeys();
        alcohols = alcoholRepository.findAllWithImages();

        return alcohols;
    }

    /**
     * 인덱스 존재 유무 확인 (Alias 여부 미구분)
     * @param indexName 인덱스 (or alias) 이름
     * @return if index exists, return true
     */
    private boolean existsIndex(String indexName) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        IndexOperations index = elasticsearchOperations.indexOps(indexCoordinates);

        return index.exists();
    }

    /**
     * {indexPrefixName}-{timestamp} 형식의 인덱스 생성 후 생성한 인덱스명 리턴
     * @param indexPrefixName 인덱스의 prefix 이름
     * @param documentClass 매핑 정보를 갖는 document Class
     * @return 생성한 인덱스 명
     */
    private String createIndex(String indexPrefixName, Class<?> documentClass) {
        // {indexPrefixName}-{timestamp} 형식의 실제 인덱스명 지정
        String indexName = IndexUtil.getIndexNameWithTimestamp(indexPrefixName);

        // 인덱스 생성 및 설정을 위한 IndexOperation 객체 생성
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));

        // 인덱싱할 데이터를 매핑한 Document의 Setting 정보 불러오기
        Settings settings = indexOperations.createSettings(documentClass);

        // 엘라스틱서치에 인덱스 생성 요청
        indexOperations.create(settings);

        // Document 클래스 속성에 지정한 매핑 정보를 보고, 인덱스 매핑 정보 업데이트
        indexOperations.putMapping(documentClass);

        // LOG: 인덱스 생성 로깅
        log.info("Create Index [{}]", indexName);

        // 실제 생성된 인덱스명 리턴
        return indexName;
    }

    /**
     * alias를 지정할 인덱스의 이름과 그 alias 이름을 받아서 alias 지정
     * 지정할 alias 이름에 이미 연결된 기존 인덱스가 존재한다면, 기존 alias 연결 제거
     * @param aliasName 지정할 alias 이름
     * @param indexName alias를 지정할 실제 인덱스 이름
     */
    private void setAlias(String aliasName, String indexName) {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(aliasName));

        AliasActions aliasActions = new AliasActions();

        // Alias add action 추가
        AliasAction addAction = new AliasAction.Add(
                AliasActionParameters.builder()
                            .withIndices(indexName)
                            .withAliases(aliasName)
                            .build());
        aliasActions.add(addAction);

        // alias에 이미 연결된 기존 인덱스가 있다면,
        // Alias remove_index action 추가
        if (indexOperations.exists()) {
            Set<String> oldIndexNameCollection = indexOperations.getAliases(aliasName).keySet();

            for (String oldIndexName : oldIndexNameCollection){
                // TODO: 인덱스 삭제 로깅 위치 변경 -> 실제로 삭제된 이후 로깅하도록
                log.info("Soon removed Index [{}]", oldIndexName);

                AliasAction removeAction = new AliasAction.RemoveIndex(
                        AliasActionParameters.builder()
                                .withIndices(oldIndexName)
                                .withAliases(aliasName)
                                .build());
                aliasActions.add(removeAction);
            }
        }

        // Alias action 적용
        // TODO: alias 함수의 리턴 값을 받아서, false라면 예외 발생?
        indexOperations.alias(aliasActions);

        log.info("Set Alias [{}] to Index [{}]", aliasName, indexName);
    }

    /**
     * 'indexName'의 이름 혹은 alias를 갖는 인덱스가 존재하지 않는다면, 인덱스 생성
     * @param indexName 생성할 인덱스 이름 (실제로는 인덱스의 Alias 이름)
     * @param documentClass 인덱스를 생성할 타겟 Document 클래스
     * @return  if index already exists, return false
     */
    private boolean createIndexIfNotExists(String indexName, Class<?> documentClass) {
        if (existsIndex(indexName)) {
            return false;
        }

        // 인덱스 생성 메소드 호출 후, 실제 생성된 인덱스 이름을 반환받음
        String createdIndexName = createIndex(indexName, documentClass);

        // alias 설정
        setAlias(indexName, createdIndexName);

        return true;
    }

    /**
     * 데이터베이스 속 alcohol 데이터 전부 elasticsearch에 인덱싱
     */
    private void insertDocumentsIntoIndexFromDatabase(String indexName) {
//        List<Alcohol> alcohols = alcoholRepository.findAllWithSearchKeys();
        List<Alcohol> alcohols = this.getAllAlcohols();

        List<AlcoholDocument> documentList = new ArrayList<>();
        for (Alcohol alcohol : alcohols) {
            // Alcohol 엔티티 객체로 AlcoholDocument 도큐먼트 객체 생성
            AlcoholDocument alcoholDocument = AlcoholDocument.of(alcohol);

            documentList.add(alcoholDocument);
        }

        IndexCoordinates targetIndex = IndexCoordinates.of(indexName);
        // 리스트에 담아서 한 번에 save
//        alcoholElasticsearchRepository.saveAll(documentList);
        elasticsearchOperations.save(documentList, targetIndex);

        // manually refresh
        IndexOperations indexOperations = elasticsearchOperations.indexOps(targetIndex);
        indexOperations.refresh();
    }

    /**
     * elasticsearch Reindex API를 호출하여 재인덱싱
     * @param sourceIndexName
     * @param destIndexName
     */
    private void callReindexAPI(String sourceIndexName, String destIndexName) {
        ReindexRequest request = ReindexRequest.builder(
                IndexCoordinates.of(sourceIndexName),
                IndexCoordinates.of(destIndexName))
                .build();

        // TODO: reindex API 비동기 호출... 그러려면 전반적으로 비동기 구조 수정이 필요함
         elasticsearchOperations.reindex(request);

        // Log: reindex api 호출 로깅
         log.info("Done Reindex from {} to {}", sourceIndexName, destIndexName);
    }

    /**
     * 기존 인덱스가 존재하는 상태에서, 새로 인덱스 생성하여 데이터 인덱싱 후 교체
     * @param doReadDatabase
     */
    private void reindex(boolean doReadDatabase) {
        // 새 index 생성
        String indexName = createIndex(ALCOHOL_INDEX_NAME, AlcoholDocument.class);

        // 새 index에 인덱싱 or Reindex API 호출?
        if (doReadDatabase) {
            // 새 index에 인덱싱
            insertDocumentsIntoIndexFromDatabase(indexName);
        } else {
            // reindex api 호출
            callReindexAPI(ALCOHOL_INDEX_NAME, indexName);
        }

        // alias 교체
        setAlias(ALCOHOL_INDEX_NAME, indexName);
    }

    /**
     * Alcohol 데이터 전체 인덱싱
     * @param doReadDatabase if true, extracts data from database
     */
    public void indexAll(Boolean doReadDatabase) {
        if (existsIndex(ALCOHOL_INDEX_NAME)){
            // 이미 기존 인덱스가 존재한다면, 재인덱싱
            reindex(doReadDatabase);
        } else {
            // 기존 인덱스가 존재하지않아 새로 인덱스 생성
            String indexName = createIndex(ALCOHOL_INDEX_NAME, AlcoholDocument.class);

            // alias 설정
            setAlias(ALCOHOL_INDEX_NAME, indexName);

            // 인덱싱
            insertDocumentsIntoIndexFromDatabase(indexName);
        }
    }

    /**
     * DB id를 파라미터로 받아 데이터베이스에 조회한 데이터로 인덱싱
     * @param id 데이터베이스 id
     */
    @Transactional(readOnly = true)
    public void indexSingleAlcoholDocument(Long id) {
        // 인덱스 존재 여부 확인 및 생성
        createIndexIfNotExists(ALCOHOL_INDEX_NAME, AlcoholDocument.class);

        // 관계형 데이터베이스에서 주류 정보 추출
        Optional<Alcohol> _target = alcoholRepository.findByIdWithSearchKeys(id);
        if (_target.isEmpty()) {
            throw new AlcoholNotFoundException(id);
        }

        Alcohol alcohol = _target.get();

        // 그래프 데이터베이스에서 주류 태그 정보 추출
        Optional<AlcoholNode> _node = alcoholNodeRepository.findByIdOrderByTagWeightDesc(id);
        if (_node.isEmpty()) {
            throw new AlcoholNodeNotFoundException(id);
        }

        AlcoholNode alcoholNode = _node.get();

        // Alcohol 엔티티 객체로 AlcoholDocument 도큐먼트 객체 생성
        AlcoholDocument alcoholDocument = AlcoholDocument.of(alcohol, alcoholNode);

        // repository save() 호출 시,
        // id 값 기준으로 인덱스에 없다면 save, 인덱스에 있다면 update
        alcoholElasticsearchRepository.save(alcoholDocument);
    }

    /**
     * Document id를 파라미터로 받아 인덱스에서 삭제
     * @param id 도큐먼트 id (== 데이터베이스 id)
     */
    public void removeSingleDocument(Long id) {
        Optional<AlcoholDocument> _target = alcoholElasticsearchRepository.findById(id);

        // 삭제할 타겟 도큐먼트가 존재하지 않는다면, 404 예외 발생
        if (_target.isEmpty()) {
            throw new AlcoholDocumentNotFoundException(id);
        }

        // index에서 document 삭제
        AlcoholDocument target = _target.get();
        alcoholElasticsearchRepository.delete(target);
    }

    /**
     * 검색어 자동완성 인덱스에 키워드 추가
     * @param keyword 인덱스에 추가할 키워드 문자열
     */
    public void putCompletionKeyword(String keyword) {
        // 인덱스 존재 여부 확인 및 생성
        createIndexIfNotExists(AUTO_COMPLETION_INDEX_NAME, AutoCompletionDocument.class);

        // 자동완성 키워드 중복 확인
        Query searchQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("keyword.keyword")
                                .query(keyword)))
                .build();
        SearchHits<AutoCompletionDocument> _exists = elasticsearchOperations.search(searchQuery, AutoCompletionDocument.class);

        // 중복되는 키워드가 존재한다면 duplication 예외 발생
        if (_exists.getTotalHits() > 0) {
            throw new CompletionKeywordDuplicatedException(keyword);
        }

        AutoCompletionDocument completion = AutoCompletionDocument.from(keyword);
        autoCompletionElasticsearchRepository.save(completion);
    }

    /**
     * 검색어 자동완성 인덱스에 술 데이터의 주류명 전부 추가
     */
    @Transactional(readOnly = true)
    public void putAllTitlesToAutoCompletion() {
        // 인덱스 존재 여부 확인 및 생성
        createIndexIfNotExists(AUTO_COMPLETION_INDEX_NAME, AutoCompletionDocument.class);

        List<Alcohol> alcoholList = alcoholRepository.findAll();

        List<AutoCompletionDocument> autoCompletionList = new ArrayList<>();
        for (Alcohol alcohol : alcoholList) {
            AutoCompletionDocument completion = AutoCompletionDocument.from(alcohol.getTitle());
            autoCompletionList.add(completion);
        }

        autoCompletionElasticsearchRepository.saveAll(autoCompletionList);
    }
}
