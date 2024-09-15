package ru.dzalba.repository;

import org.springframework.stereotype.Repository;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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
}