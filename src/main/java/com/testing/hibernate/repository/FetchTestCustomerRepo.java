package com.testing.hibernate.repository;

import com.testing.hibernate.model.fetch_mode.FetchTestCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FetchTestCustomerRepo extends JpaRepository<FetchTestCustomer, Long> {


    @Query("select customer from FetchTestCustomer customer")
    List<FetchTestCustomer> getAllCustomers();

}
