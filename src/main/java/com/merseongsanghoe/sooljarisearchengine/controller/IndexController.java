package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.service.IndexService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/index")
public class IndexController {

    private final IndexService indexService;

    /**
     * 전체 데이터 인덱싱
     * 인덱스가 없다면 새로 생성, 있다면 교체
     * "db" if true, db에서 데이터를 직접 추출해서 새로 인덱싱
     */
    @PostMapping("/all")
    public ResponseEntity<Void> indexAll(HttpServletRequest rq) {
        // query parameter "db"가 존재한다면 true
        boolean db = (rq.getParameter("db") != null);
        indexService.indexAll(db);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 특정 데이터 인덱싱 혹은 업데이트
     * @param id 생성 혹은 수정할 데이터의 DB id
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> index(@PathVariable("id") Long id) {
        indexService.indexSingleDocument(id);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 특정 데이터 인덱스에서 삭제
     * @param id 삭제할 데이터의 DB id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable("id") Long id) {
        indexService.removeSingleDocument(id);

        return ResponseEntity.noContent().build();
    }
}
