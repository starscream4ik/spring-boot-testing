package com.testing.hibernate.model.multibag;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "customer")
@EqualsAndHashCode(of = "id")
public class MultiBagTestCustomer {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<MultiBagTestDocument> documents;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<MultiBagTestCompany> companies;

}
