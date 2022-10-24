package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.AvatarStudent;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.repository.AvatarStudentRepository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarStudentService {
    @Value("avatars")
    private String avatarsDir;
    private final AvatarStudentRepository avatarStudentRepository;
    private final StudentService studentService;

    public AvatarStudentService(AvatarStudentRepository avatarStudentRepository, StudentService studentService) {
        this.avatarStudentRepository = avatarStudentRepository;
        this.studentService = studentService;
    }

     public void uploadAvatarStudent(Long id, MultipartFile file) {
        Student student = studentService.findStudent(id);
         String extension = Optional.ofNullable(file.getOriginalFilename()).
                 map(s -> s.substring(file.getOriginalFilename().lastIndexOf("."))).
                 orElse(" ");
         Path filePath = Path.of(avatarsDir, id +  extension);
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Возникла проблема при создании файла или директории.");
        }
         try (InputStream inputStream = file.getInputStream();
              OutputStream outputStream = Files.newOutputStream(filePath, CREATE_NEW);
              BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
              BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)) {
             bufferedInputStream.transferTo(bufferedOutputStream);
         } catch (IOException e) {
             throw new RuntimeException("Возникла проблема при чтении или записи файла");
         }

         AvatarStudent avatarStudent = avatarStudentRepository.findByStudentId(id).orElseGet(AvatarStudent::new);
            avatarStudent.setStudent(student);
            avatarStudent.setFilePath(filePath.toString());
            avatarStudent.setFileSize(file.getSize());
            avatarStudent.setMediaType(file.getContentType());
            avatarStudent.setPreview(Preview.generateImagePreview(filePath));

            avatarStudentRepository.save(avatarStudent);

    }

    public Pair<String, byte[]> getAvatar(Long id) {
        AvatarStudent avatarStudent = avatarStudentRepository.findByStudentId(id).orElseThrow(() -> new AvatarNotFoundException(id));
        try {
            return Pair.of(avatarStudent.getMediaType(), Files.readAllBytes(Paths.get(avatarStudent.getFilePath())));
        } catch (IOException e) {
            throw new RuntimeException("Возникла проблема при чтении файла");
        }
    }

    public Pair<String, byte[]> getPreview(Long id) {
        AvatarStudent avatarStudent = avatarStudentRepository.findByStudentId(id).orElseThrow(() -> new AvatarNotFoundException(id));
        return Pair.of(avatarStudent.getMediaType(), avatarStudent.getPreview());
    }
}
