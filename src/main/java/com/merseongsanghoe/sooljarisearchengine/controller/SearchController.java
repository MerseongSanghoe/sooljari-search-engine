package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> search(@RequestParam("s") String s,
                                                      Pageable page) {
        Map<String, Object> results = searchService.searchTitle(s, page);

        return ResponseEntity.ok().body(results);
    }

}