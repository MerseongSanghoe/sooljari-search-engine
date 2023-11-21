package com.merseongsanghoe.sooljarisearchengine.node;

import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Tag")
@Getter
public class TagNode {
    @Id
    private String title;
}
