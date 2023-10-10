package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.service.AlcoholService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/index")
public class IndexController {

    private final AlcoholService alcoholService;

    @PostMapping("/all")
    public ResponseEntity<Void> indexAll() {
        alcoholService.indexAll();

        return ResponseEntity.ok().build();
    }
}
