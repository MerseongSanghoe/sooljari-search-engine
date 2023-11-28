package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.exception.RequiredRequestParamIsMissingException;
import com.merseongsanghoe.sooljarisearchengine.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> search(@RequestParam(value = "s", defaultValue = "") String s,
                                                      @RequestParam(value = "t", defaultValue = "") String t,
                                                      Pageable page) {
        // DEBUG LOG: 검색 키워드
        log.debug("Search Keyword: {}", s);
        log.debug("Search Tag Keyword: {}", t);

        // s와 t 검색어 전부 없으면 예외 발생
        // TODO: 더 깔끔하게 처리하는 방법 없을까...
        if (s.isEmpty() && t.isEmpty()) {
            throw new RequiredRequestParamIsMissingException("s or t");
        }

        Map<String, Object> results = searchService.search(s, t, page);

        return ResponseEntity.ok().body(results);
    }

}
