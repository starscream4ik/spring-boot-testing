package com.testing.hibernate.repository;

import com.testing.hibernate.model.batch.BatchTestCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchTestCustomerRepo extends JpaRepository<BatchTestCustomer, Long> {


    @Query("select customer from BatchTestCustomer customer")
    List<BatchTestCustomer> getAllCustomers();

}
