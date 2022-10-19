package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.EmblemFaculty;

import java.util.Optional;

public interface EmblemFacultyRepository extends JpaRepository<EmblemFaculty, Long> {
        Optional<EmblemFaculty> findByFacultyId(Long facultyID);
}
