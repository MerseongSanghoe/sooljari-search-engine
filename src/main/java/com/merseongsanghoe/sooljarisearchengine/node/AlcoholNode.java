package com.merseongsanghoe.sooljarisearchengine.node;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Alcohol")
@Getter
public class AlcoholNode {
    @Id
    private Long dbid;

    @Relationship(type = "LINKED")
    private List<TagLink> tags = new ArrayList<>();
}
