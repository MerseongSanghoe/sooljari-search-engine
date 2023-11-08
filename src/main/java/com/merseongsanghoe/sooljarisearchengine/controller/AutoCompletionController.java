package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.exception.RequiredRequestBodyIsMissingException;
import com.merseongsanghoe.sooljarisearchengine.service.IndexService;
import com.merseongsanghoe.sooljarisearchengine.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auto-completion")
@RequiredArgsConstructor
public class AutoCompletionController {

    private final SearchService searchService;
    private final IndexService indexService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAutoCompletion(@RequestParam("k") String k,
                                                                 Pageable page) {
        // DEBUG LOG: 검색어 자동완성 키워드
        log.debug("GET AutoCompletion Keyword: {}", k);

        Map<String, Object> results = searchService.getAutoCompletion(k, page);

        return ResponseEntity.ok(results);
    }

    /**
     * String 타입의 keyword를 입력받아, 자동완성 키워드에 추가
     * @param req Request Body
     */
    @PostMapping("")
    public ResponseEntity<Void> putAutoCompletion(@RequestBody Map<String, String> req) {
        String keyword = req.get("keyword");

        // "keyword" req body 값이 없다면 BAD REQUEST
        if (keyword == null || keyword.isBlank()) {
            throw new RequiredRequestBodyIsMissingException("keyword");
        }

        // DEBUG LOG: 추가할 검색어 자동완성 키워드
        log.debug("PUT AutoCompletion Keyword: {}", keyword);

        indexService.putCompletionKeyword(keyword);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/alcohol-title")
    public ResponseEntity<Void> putAlcoholTitleAutoCompletion() {
        indexService.putAllTitlesToAutoCompletion();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
