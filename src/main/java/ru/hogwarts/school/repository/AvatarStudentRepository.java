package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.AvatarStudent;

import java.util.Optional;

public interface AvatarStudentRepository extends JpaRepository<AvatarStudent, Long> {
    Optional<AvatarStudent> findByStudentId(Long facultyID);
}
