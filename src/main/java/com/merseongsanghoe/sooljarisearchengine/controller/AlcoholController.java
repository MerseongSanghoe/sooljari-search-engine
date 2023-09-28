package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.service.AlcoholService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alcohol")
public class AlcoholController {
    private final AlcoholService alcoholService;

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam("s") String s) {
        // TODO: 페이징 옵션 추가 필요
        Map<String, Object> results = alcoholService.searchTitle(s);

        return ResponseEntity.ok().body(results);
    }

    @PostMapping("/index-all")
    public ResponseEntity<Void> indexAll() {
        alcoholService.indexAll();

        return ResponseEntity.ok().build();
    }
}
