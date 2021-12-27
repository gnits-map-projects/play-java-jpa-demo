package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAPersonRepository implements PersonRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAPersonRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Person> add(Person person) {
        return supplyAsync(() -> wrap(em -> insert(em, person)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Person>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<Person> find(String name) {
        return supplyAsync(() -> wrap(em -> find(em, name)), executionContext);
    }

    @Override
    public CompletionStage<Boolean> updateCity(String name, String city) {
        return supplyAsync(() -> wrap(em -> update(em, name, city)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Person insert(EntityManager em, Person person) {
        em.persist(person);
        return person;
    }

    private Stream<Person> list(EntityManager em) {
        List<Person> persons = em.createQuery("select p from person p", Person.class).getResultList();
        return persons.stream();
    }

    private Person find(EntityManager em, String name) {
        Person person = em.createQuery("select p from person p where p.name=:name", Person.class)
                .setParameter("name", name).getSingleResult();
        return person;
    }

    private boolean update(EntityManager em, String name, String city) {
        int n = em.createQuery("update person p set p.city=:city where p.name=:name")
                .setParameter("city",  city)
                .setParameter("name", name)
                .executeUpdate();
        if(n == 1) return true;
        else return false;

    }
}
