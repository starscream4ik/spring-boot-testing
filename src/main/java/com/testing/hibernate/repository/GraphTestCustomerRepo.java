package com.testing.hibernate.repository;

import com.testing.hibernate.model.graph.GraphTestCustomer;
import com.testing.hibernate.model.join.JoinTestCustomer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface GraphTestCustomerRepo extends JpaRepository<GraphTestCustomer, Long> {

    @EntityGraph(value = "join-documents")
    List<GraphTestCustomer> findAll();

    @EntityGraph(value = "join-documents")
    @Query("select customer from GraphTestCustomer customer") // no need for distinct, graph will not have "cartesian" objects
    List<GraphTestCustomer> getAllCustomers();


}
