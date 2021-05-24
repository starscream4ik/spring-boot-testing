package com.testing.hibernate.model.multibag;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "document")
@EqualsAndHashCode(of = "id")
public class MultiBagTestDocument {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private MultiBagTestCustomer customer;

    private String status;
    private String category;
    private ZonedDateTime updateTime;

    private String uploadBank;
    private ZonedDateTime uploadTime;

}
