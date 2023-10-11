package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/index")
public class IndexController {

    private final IndexService indexService;

    @PostMapping("/all")
    public ResponseEntity<Void> indexAll() {
        indexService.indexAll();

        return ResponseEntity.ok().build();
    }
}
