package com.testing.hibernate.repository;

import com.testing.hibernate.model.fetch_mode.FetchTestCustomer;
import com.testing.hibernate.model.join.JoinTestCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinTestCustomerRepo extends JpaRepository<JoinTestCustomer, Long> {


    // distinct to avoid "cartesian" objects
    @Query("select customer from JoinTestCustomer customer left join fetch customer.documents")
    List<JoinTestCustomer> getAllCustomers();

}
