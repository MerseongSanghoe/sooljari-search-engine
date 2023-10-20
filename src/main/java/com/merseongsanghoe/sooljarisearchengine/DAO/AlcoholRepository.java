package com.merseongsanghoe.sooljarisearchengine.DAO;

import com.merseongsanghoe.sooljarisearchengine.entity.Alcohol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlcoholRepository extends JpaRepository<Alcohol, Long> {
    @Query("SELECT DISTINCT a FROM Alcohol a " +
            "LEFT JOIN FETCH a.searchKeys")
    List<Alcohol> findAllWithSearchKeys();

    @Query("SELECT DISTINCT a FROM Alcohol a " +
            "LEFT JOIN FETCH a.searchKeys " +
            "WHERE a.id = :id")
    Optional<Alcohol> findByIdWithSearchKeys(Long id);
}
