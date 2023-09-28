package com.merseongsanghoe.sooljarisearchengine.DAO;

import com.merseongsanghoe.sooljarisearchengine.domain.Alcohol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlcoholRepository extends JpaRepository<Alcohol, Long> {
    @Query("SELECT DISTINCT a FROM Alcohol a " +
            "LEFT JOIN FETCH a.searchKeys")
    List<Alcohol> findAllWithSearchKeys();
}
