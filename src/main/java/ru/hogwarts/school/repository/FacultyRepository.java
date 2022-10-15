package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;

import java.util.Collection;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Collection<Faculty> findByColor(String color);
    Collection<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
    @Query("SELECT s FROM Faculty f join Student s on s.faculty.id=f.id where f.id =:id")
    Collection<Student> findStudents(Long id);
}
