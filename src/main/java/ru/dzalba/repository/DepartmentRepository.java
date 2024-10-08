package ru.dzalba.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dzalba.models.Department;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class DepartmentRepository extends AbstractRepository<Department> {

    @PersistenceContext
    private EntityManager entityManager;

    public DepartmentRepository() {
        super(Department.class);
    }

    @Transactional
    public Department findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);
        query.select(root).where(cb.equal(root.get("name"), name));
        return entityManager.createQuery(query).getSingleResult();
    }
}
