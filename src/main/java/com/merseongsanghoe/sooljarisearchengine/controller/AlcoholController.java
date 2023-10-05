package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.service.AlcoholService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/alcohol")
public class AlcoholController {
    private final AlcoholService alcoholService;

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam("s") String s,
                                                      Pageable page) {
        Map<String, Object> results = alcoholService.searchTitle(s, page);

        return ResponseEntity.ok().body(results);
    }

    @PostMapping("/index-all")
    public ResponseEntity<Void> indexAll() {
        alcoholService.indexAll();

        return ResponseEntity.ok().build();
    }
}
