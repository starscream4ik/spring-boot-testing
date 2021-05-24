package com.testing.hibernate;

import com.testing.hibernate.model.fetch_mode.FetchTestCustomer;
import com.testing.hibernate.model.fetch_mode.FetchTestDocument;
import com.testing.hibernate.repository.FetchTestCustomerRepo;
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
public class FetchModeCustomerHibernateIT {


    @Autowired
    private FetchTestCustomerRepo customerRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testGetCustomersByCriteria() {
        CriteriaQuery<FetchTestCustomer> query = entityManager.getCriteriaBuilder()
                .createQuery(FetchTestCustomer.class);
        Root<FetchTestCustomer> root = query.from(FetchTestCustomer.class);
        query.select(root);
        List<String> docNames = entityManager.createQuery(query).getResultList().stream()
                .map(FetchTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(FetchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
    }

    @Test
    void testGetCustomersByRepoAndHql() {
        List<String> docNames = customerRepo.getAllCustomers().stream()
                .map(FetchTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(FetchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
    }

    @Test
    void testGetCustomersByRepo() {
        List<String> docNames = customerRepo.findAll().stream()
                .map(FetchTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(FetchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
    }

    @Test
    void testGetCustomersByFind() {
        List<String> docNames = entityManager.find(FetchTestCustomer.class, 1235L).getDocuments().stream()
                .map(FetchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(2, docNames.size());
    }


}
