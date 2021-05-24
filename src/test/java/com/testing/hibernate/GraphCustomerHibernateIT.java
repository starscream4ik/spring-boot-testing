package com.testing.hibernate;

import com.testing.hibernate.model.fetch_mode.FetchTestCustomer;
import com.testing.hibernate.model.fetch_mode.FetchTestDocument;
import com.testing.hibernate.model.graph.GraphTestCustomer;
import com.testing.hibernate.model.graph.GraphTestDocument;
import com.testing.hibernate.model.join.JoinTestCustomer;
import com.testing.hibernate.model.join.JoinTestDocument;
import com.testing.hibernate.repository.GraphTestCustomerRepo;
import com.testing.hibernate.repository.JoinTestCustomerRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class GraphCustomerHibernateIT {


    @Autowired
    private GraphTestCustomerRepo customerRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testGetCustomersByCriteria() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("join-documents");
        CriteriaQuery<GraphTestCustomer> query = entityManager.getCriteriaBuilder()
                .createQuery(GraphTestCustomer.class);
        Root<GraphTestCustomer> root = query.from(GraphTestCustomer.class);
        query.select(root); // no need for distinct, graph will not have "cartesian" objects
        TypedQuery<GraphTestCustomer> graphQuery = entityManager.createQuery(query);
        graphQuery.setHint("javax.persistence.fetchgraph", entityGraph);
        List<String> docNames = graphQuery
                .getResultList().stream()
                .map(GraphTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(GraphTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
    }

    @Test
    void testGetCustomersByRepoAndHql() {
        List<String> docNames = customerRepo.getAllCustomers().stream()
                .map(GraphTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(GraphTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
    }

    @Test
    void testGetCustomersByRepo() {
        List<String> docNames = customerRepo.findAll().stream()
                .map(GraphTestCustomer::getDocuments)
                .flatMap(Collection::stream)
                .map(GraphTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(6, docNames.size());
    }

    @Test
    void testGetCustomersByFind() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("join-documents");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        List<String> docNames = entityManager.find(FetchTestCustomer.class, 1235L, properties).getDocuments().stream()
                .map(FetchTestDocument::getStatus)
                .collect(Collectors.toList());

        Assertions.assertEquals(2, docNames.size());
    }


}
