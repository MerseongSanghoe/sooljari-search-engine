package com.merseongsanghoe.sooljarisearchengine.DAO;

import com.merseongsanghoe.sooljarisearchengine.node.AlcoholNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface AlcoholNodeRepository extends Neo4jRepository<AlcoholNode, Long> {
    @Query("MATCH (a:Alcohol {dbid: $dbid})-[i:LINKED]->(t:Tag) " +
            "WITH a, i, t ORDER BY i.weight DESC " +
            "RETURN a, collect(i), collect(t)")
    Optional<AlcoholNode> findByIdOrderByTagWeightDesc(Long dbid);
}
