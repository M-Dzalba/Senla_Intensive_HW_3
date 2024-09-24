package ru.dzalba.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Position;
import ru.dzalba.models.ProjectParticipation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;


@Repository
public class EmployeeRepository extends AbstractRepository<Employee> {

    @PersistenceContext
    private EntityManager entityManager;

    public EmployeeRepository() {
        super(Employee.class);
    }

    public List<Employee> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Predicate namePredicate = criteriaBuilder.like(employeeRoot.get("fullName"), "%" + name + "%");
        criteriaQuery.where(namePredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<Employee> findByDepartment(Department department) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> employeeRoot = query.from(Employee.class);

        Predicate departmentPredicate = cb.equal(employeeRoot.get("department"), department);
        query.where(departmentPredicate);

        return entityManager.createQuery(query).getResultList();
    }

    public List<Employee> findAllWithDetails() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        // Eagerly fetch associations
        Fetch<Employee, Position> positionFetch = employeeRoot.fetch("position", JoinType.LEFT);
        Fetch<Employee, Department> departmentFetch = employeeRoot.fetch("department", JoinType.LEFT);
        Fetch<Employee, ProjectParticipation> projectParticipationFetch = employeeRoot.fetch("projectParticipations", JoinType.LEFT);

        criteriaQuery.select(employeeRoot);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    // JPQL
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.position LEFT JOIN FETCH e.department LEFT JOIN FETCH e.projectParticipations WHERE e.id = :id")
    public Employee findByIdWithDetails(@Param("id") Integer id) {
        return entityManager.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.position LEFT JOIN FETCH e.department LEFT JOIN FETCH e.projectParticipations WHERE e.id = :id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    // EntityGraph
    @EntityGraph(attributePaths = {"position", "department", "projectParticipations"})
    public Optional<Employee> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Employee.class, id));
    }
}