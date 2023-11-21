package com.merseongsanghoe.sooljarisearchengine.node;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Getter
public class TagLink {
    @RelationshipId
    private Long id;

    @TargetNode
    private TagNode tag;

    private Integer weight;
}
