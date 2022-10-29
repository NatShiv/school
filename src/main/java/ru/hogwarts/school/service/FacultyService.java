package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.EmblemFaculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.DataEntryError;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class FacultyService {
    private Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.debug("Вызван метод - создть факультет.");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.debug("Вызван метод - найти факультет с номером {}.", id);
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("Факультет с номером {} не найден", id);
            return new FacultyNotFoundException(id);
        });
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.debug("Вызван метод - изменить факультет с номером {}.", id);
     findFaculty(id);
        return facultyRepository.save(faculty);
    }

    public Faculty removeFaculty(Long id) {
        logger.debug("Вызван метод - удалить факультет с номером {}.", id);
        Faculty faculty = findFaculty(id);
        facultyRepository.deleteById(id);
        return faculty;
    }

    public Collection<Faculty> findByColor(String color) {
        logger.debug("Вызван метод - найти факультет по цвету {}.", color);
        if (color.isBlank()) {
            logger.warn("Пользователь не ввел данные и программа ввела дефолтное пустое значение");
            throw new DataEntryError("Введите цвет факультета.");
        }
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String nameOrColor) {
        logger.debug("Вызван метод - найти факультет по цвету или названию {}.", nameOrColor);
        if (nameOrColor.isBlank()) {
            logger.warn("Пользователь не ввел данные и программа ввела дефолтное пустое значение");
            throw new DataEntryError("Введите цвет или название факультета.");
        }
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }

    public Collection<Student> findStudentByFaculty(Long id) {
        logger.debug("Вызван метод - найти всех студентов факультетас номером {}.", id);
        return facultyRepository.findStudents(id);
    }

    public void saveEmblem(Long id, EmblemFaculty emblemFaculty) {
        logger.debug("Вызван метод - прикрепить эмблему факультету с номером {}.", id);
        Faculty faculty = findFaculty(id);
        faculty.emblemFacultySet(emblemFaculty);
        facultyRepository.save(faculty);
    }
}