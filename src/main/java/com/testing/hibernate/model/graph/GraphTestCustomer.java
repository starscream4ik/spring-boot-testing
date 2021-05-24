package com.testing.hibernate.model.graph;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customer")
@NamedEntityGraph(
        name = "join-documents",
        attributeNodes = {
                @NamedAttributeNode("documents")
        }
)
public class GraphTestCustomer {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<GraphTestDocument> documents = new ArrayList<>();
}
