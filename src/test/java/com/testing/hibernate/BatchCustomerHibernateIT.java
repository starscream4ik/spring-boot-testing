package com.testing.hibernate;

import com.testing.hibernate.model.batch.BatchTestCustomer;
import com.testing.hibernate.model.batch.BatchTestDocument;
import com.testing.hibernate.repository.BatchTestCustomerRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
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
                "classpath:db/data/populate_customer_document_for_customer_search.sql"})
@Sql(executionPhase = AFTER_TEST_METHOD, value = {"classpath:db/data/drop_data.sql"})
public class BatchCustomerHibernateIT {

    @Autowired
    private BatchTestCustomerRepo customerRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void testGetCustomersByCriteria() {
        CriteriaQuery<BatchTestCustomer> query = entityManager.getCriteriaBuilder().createQuery(BatchTestCustomer.class);
        Root<BatchTestCustomer> root = query.from(BatchTestCustomer.class);
        query.select(root);
        List<BatchTestCustomer> resultList = entityManager.createQuery(query).getResultList();
        List<String> docNames = resultList.stream()
                .map(BatchTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(BatchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
        Assertions.assertEquals(11, resultList.size());
    }

    @Test
    @Transactional
    void testGetCustomersByRepoAndHql() {
        List<String> docNames = customerRepo.getAllCustomers().stream()
                .map(BatchTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(BatchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
    }

    @Test
    @Transactional
    void testGetCustomersByRepo() {
        List<String> docNames = customerRepo.findAll().stream()
                .map(BatchTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(BatchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
    }

    @Test
    @Transactional
    void testGetCustomersByFind() {
        List<String> docNames = entityManager.find(BatchTestCustomer.class, 1235L).getDocuments().stream()
                .map(BatchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(2, docNames.size());
    }


}
