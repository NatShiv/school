package ru.hogwarts.school.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Student;

import java.util.Collection;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);
    Collection<Student> findByAgeBetween(int min, int max);
@Query("SELECT COUNT (id) FROM Student ")
   int totalStudents();
  @Query("SELECT AVG(age) from Student")
  int averageAge();

    @Query(" SELECT s FROM Student s ORDER BY s.id DESC")
    Collection<Student> getFiveEndStudents(PageRequest pageRequest);
}
