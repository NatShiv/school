package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.EmblemFaculty;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.EmblemNotFoundException;
import ru.hogwarts.school.repository.EmblemFacultyRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class EmblemFacultyService {
    @Value("${faculties.emblem.dir.path}")
    private String emblemsDir;
    private final FacultyService facultyService;
    private final EmblemFacultyRepository emblemFacultyRepository;

    public EmblemFacultyService(FacultyService facultyService, EmblemFacultyRepository emblemFacultyRepository) {
        this.facultyService = facultyService;
        this.emblemFacultyRepository = emblemFacultyRepository;
    }

    public EmblemFaculty findEmblemFaculty(Long facultyId) {
        return emblemFacultyRepository.findByFacultyId(facultyId).orElseThrow(() -> new EmblemNotFoundException(facultyId));
    }

    public void uploadEmblem(Long facultyID, MultipartFile file)  {
        Faculty faculty = facultyService.findFaculty(facultyID);
        String extension = Optional.ofNullable(file.getOriginalFilename()).
                map(s -> s.substring(file.getOriginalFilename().lastIndexOf("."))).
                orElse(" ");
        Path filePath = Path.of(emblemsDir, facultyID + extension);
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

        EmblemFaculty emblemFaculty = emblemFacultyRepository.findByFacultyId(facultyID).orElseGet(EmblemFaculty::new);
        emblemFaculty.setFaculty(faculty);
        emblemFaculty.setFilePath(filePath.toString());
        emblemFaculty.setFileSize(file.getSize());
        emblemFaculty.setMediaType(file.getContentType());
        emblemFaculty.setPreview(Preview.generateImagePreview(filePath));

        emblemFacultyRepository.save(emblemFaculty);
    }
}