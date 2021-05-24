package com.testing.hibernate.repository;

import com.testing.hibernate.model.join.JoinTestCustomer;
import com.testing.hibernate.model.multibag.MultiBagTestCompany;
import com.testing.hibernate.model.multibag.MultiBagTestCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MultiBagTestCustomerRepo extends JpaRepository<MultiBagTestCustomer, Long> {


    @Query("select  customer from MultiBagTestCustomer customer")
    List<MultiBagTestCustomer> getAllCustomers();

    @Query("select  customer from MultiBagTestCustomer customer "
            + "left join fetch customer.companies "
            + "left join fetch customer.documents")
    List<MultiBagTestCustomer> getAllCustomersFetchAll();

    @Query("select  customer from MultiBagTestCustomer customer "
            + "left join fetch customer.documents")
    List<MultiBagTestCustomer> getAllCustomersFetchDocuments();

    @Query("select customer from MultiBagTestCustomer customer "
            + "left join fetch customer.companies where customer in :customers")
    List<MultiBagTestCustomer> getAllCustomersFetchCompaniesWhereCustomerIn(Collection<MultiBagTestCustomer> customers);


    @Query("select company from MultiBagTestCompany company where company.customer.id in (:ids)")
    List<MultiBagTestCompany> getCompaniesByCustomerIds(Collection<Long> ids);





}
