package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.AvatarStudent;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.DataEntryError;
import ru.hogwarts.school.repository.AvatarStudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarStudentService {
    @Value("avatars")
    private String avatarsDir;
    private Logger logger = LoggerFactory.getLogger(AvatarStudentService.class);
    private final AvatarStudentRepository avatarStudentRepository;
    private final StudentService studentService;

    public AvatarStudentService(AvatarStudentRepository avatarStudentRepository, StudentService studentService) {
        this.avatarStudentRepository = avatarStudentRepository;
        this.studentService = studentService;
    }

    public void uploadAvatarStudent(Long studentId, MultipartFile file) {
        logger.debug("Вызван метод создающий аватар для студента с номером {} из переданного файла.", studentId);
        Student student = studentService.findStudent(studentId);
        String extension = Optional.ofNullable(file.getOriginalFilename()).
                map(s -> s.substring(file.getOriginalFilename().lastIndexOf("."))).
                orElse(" ");
        Path filePath = Path.of(avatarsDir, studentId + extension);
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            logger.error("Возникла проблема при создании файла или директории.");
            throw new RuntimeException("Возникла проблема при создании файла или директории.");
        }
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        } catch (IOException e) {
            logger.error("Возникла проблема при чтении или записи файла.");
            throw new RuntimeException("Возникла проблема при чтении или записи файла");
        }

        AvatarStudent avatarStudent = avatarStudentRepository.findByStudentId(studentId).orElseGet(AvatarStudent::new);
        avatarStudent.setStudent(student);
        avatarStudent.setFilePath(filePath.toString());
        avatarStudent.setFileSize(file.getSize());
        avatarStudent.setMediaType(file.getContentType());
        avatarStudent.setPreview(Preview.generateImagePreview(filePath));
        avatarStudentRepository.save(avatarStudent);
        studentService.saveAvatar(studentId, avatarStudent);

    }

    public Pair<String, byte[]> getAvatar(Long studentId) {
        logger.debug("Вызван метод - показать аватар.");
        AvatarStudent avatarStudent = avatarStudentRepository.findByStudentId(studentId).orElseThrow(() -> {
            logger.error("У студента с номером {} нет аватара.", studentId);
            return new AvatarNotFoundException(studentId);
        });

        try {
            return Pair.of(avatarStudent.getMediaType(), Files.readAllBytes(Paths.get(avatarStudent.getFilePath())));
        } catch (IOException e) {
            logger.error("Возникла проблема при чтении файла.");
            throw new RuntimeException("Возникла проблема при чтении файла");
        }
    }

    public Pair<String, byte[]> getPreview(Long studentId) {
        logger.debug("Вызван метод - показать уменьшенное изображение аватара.");
        AvatarStudent avatarStudent = avatarStudentRepository.findByStudentId(studentId).orElseThrow(() -> {
            logger.error("У студента с номером {} нет аватара.", studentId);
            return new AvatarNotFoundException(studentId);
        });
        return Pair.of(avatarStudent.getMediaType(), avatarStudent.getPreview());
    }

    public List<AvatarStudent> getAvatarPage(int pageNumber, int pageSize) {
        logger.debug("Вызван метод - вывести постранично всех студентов у которых есть аватар.");
        if (pageNumber < 1) {
            logger.error("Номер страницы переданный в метод меньше единицы.");
            throw new DataEntryError("Номер страницы не должен быть меньше единицы!");
        }
        if (pageSize < 1) {
            logger.error("Колличество строк на странице переданные в метод меньше единицы.");
            throw new DataEntryError("Не должно быть меньше одного элемента на странице!");
        }
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarStudentRepository.findAll(pageRequest).getContent();
    }
}
