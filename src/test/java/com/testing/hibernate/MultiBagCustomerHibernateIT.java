package com.testing.hibernate;

import com.testing.hibernate.model.fetch_mode.FetchTestCustomer;
import com.testing.hibernate.model.fetch_mode.FetchTestDocument;
import com.testing.hibernate.model.join.JoinTestCustomer;
import com.testing.hibernate.model.join.JoinTestDocument;
import com.testing.hibernate.model.multibag.MultiBagTestCompany;
import com.testing.hibernate.model.multibag.MultiBagTestCustomer;
import com.testing.hibernate.model.multibag.MultiBagTestDocument;
import com.testing.hibernate.repository.JoinTestCustomerRepo;
import com.testing.hibernate.repository.MultiBagTestCustomerRepo;
import org.hibernate.criterion.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
@Sql(executionPhase = BEFORE_TEST_METHOD,
        value = {"classpath:db/data/populate_customer_for_customer_search.sql",
                "classpath:db/data/populate_customer_document_for_customer_search.sql",
                "classpath:db/data/populate_customer_company_for_customer_search.sql",
        })
@Sql(executionPhase = AFTER_TEST_METHOD, value = {"classpath:db/data/drop_data.sql"})
public class MultiBagCustomerHibernateIT {


    @Autowired
    private MultiBagTestCustomerRepo customerRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testGetCustomersByCriteria() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MultiBagTestCustomer> query = criteriaBuilder
                .createQuery(MultiBagTestCustomer.class);
        Root<MultiBagTestCustomer> root = query.from(MultiBagTestCustomer.class);
        root.fetch("documents", JoinType.LEFT);
        root.fetch("companies", JoinType.LEFT);
        query.select(root).orderBy(criteriaBuilder.asc(root.get("id"))); // .distinct(true);

        List<MultiBagTestCustomer> resultList = entityManager.createQuery(query).getResultList();

        List<String> docStatuses = resultList.stream()
                .map(MultiBagTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(MultiBagTestDocument::getStatus)
                .collect(Collectors.toList());

        List<String> companyNames = resultList.stream()
                .map(MultiBagTestCustomer::getCompanies)
                .flatMap(Collection::stream)
                .map(MultiBagTestCompany::getCompanyName)
                .collect(Collectors.toList());

        System.out.println(companyNames);
        System.out.println(docStatuses);


        Assertions.assertEquals(11, resultList.size());
    }

    @Test
    void testGetCustomersByRepoAndHqlFetchAll() {
        List<MultiBagTestCustomer> resultList = customerRepo.getAllCustomersFetchAll();

        List<String> docStatuses = resultList.stream()
                .map(MultiBagTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(MultiBagTestDocument::getStatus)
                .collect(Collectors.toList());

        List<String> companyNames = resultList.stream()
                .map(MultiBagTestCustomer::getCompanies)
                .flatMap(Collection::stream)
                .map(MultiBagTestCompany::getCompanyName)
                .collect(Collectors.toList());

        System.out.println(companyNames);
        System.out.println(docStatuses);

        Assertions.assertEquals(11, resultList.size());
    }


    @Test
    void testGetCustomersByRepoAndHqlExperiment() {
        // play with BatchSize/Subselect
//        List<MultiBagTestCustomer> resultList = customerRepo.findAll();
//
//        List<String> docStatuses = resultList.stream()
//                .map(MultiBagTestCustomer::getDocuments)
//                .flatMap(Collection::stream)
//                .map(MultiBagTestDocument::getStatus)
//                .collect(Collectors.toList());
//
//        List<String> companyNames = resultList.stream()
//                .map(MultiBagTestCustomer::getCompanies)
//                .flatMap(Collection::stream)
//                .map(MultiBagTestCompany::getCompanyName)
//                .collect(Collectors.toList());
//
//        System.out.println(companyNames);
//        System.out.println(docStatuses);
//
//        Assertions.assertEquals(11, resultList.size());

        //play with join and IN

        List<MultiBagTestCustomer> resultList2 = customerRepo.getAllCustomersFetchDocuments();

        List<String> docStatuses2 = resultList2.stream()
                .map(MultiBagTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(MultiBagTestDocument::getStatus)
                .collect(Collectors.toList());

        System.out.println(docStatuses2);

        Set<Long> customerIds = resultList2.stream().map(MultiBagTestCustomer::getId).collect(Collectors.toSet());
        List<MultiBagTestCompany> companiesByCustomerIds = customerRepo.getCompaniesByCustomerIds(customerIds);

        List<String> names = companiesByCustomerIds.stream()
                .map(MultiBagTestCompany::getCompanyName)
                .collect(Collectors.toList());

        System.out.println(names);
    }

    @Test
    void testGetCustomersByRepoAndHqlExperiment2() {

        // play with two separate JOINS and IN

        List<MultiBagTestCustomer> resultList2 = customerRepo.getAllCustomersFetchDocuments();

        List<MultiBagTestCustomer> customersWithCompanies = customerRepo.getAllCustomersFetchCompaniesWhereCustomerIn(resultList2);

        List<String> companies = customersWithCompanies.stream()
                .map(MultiBagTestCustomer::getCompanies)
                .flatMap(Collection::stream)
                .map(MultiBagTestCompany::getCompanyName)
                .collect(Collectors.toList());

        List<String> docStatuses3 = customersWithCompanies.stream()
                .map(MultiBagTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(MultiBagTestDocument::getStatus)
                .collect(Collectors.toList());

        System.out.println(docStatuses3);

        System.out.println(companies);

    }


}
