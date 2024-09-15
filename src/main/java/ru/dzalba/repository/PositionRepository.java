package ru.dzalba.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dzalba.models.Position;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PositionRepository extends AbstractRepository<Position> {

    @PersistenceContext
    private EntityManager entityManager;

    public PositionRepository() {
        super(Position.class);
    }

    @Transactional
    public void deleteById(int id) {
        Position position = findById(id).orElseThrow();
        entityManager.remove(position);
    }

    public List<Position> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Position> criteriaQuery = criteriaBuilder.createQuery(Position.class);
        Root<Position> positionRoot = criteriaQuery.from(Position.class);

        Predicate namePredicate = criteriaBuilder.like(positionRoot.get("name"), "%" + name + "%");
        criteriaQuery.where(namePredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }


}