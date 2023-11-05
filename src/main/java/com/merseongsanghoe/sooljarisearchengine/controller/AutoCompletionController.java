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
}
