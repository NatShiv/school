package ru.hogwarts.school.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.EmblemFaculty;
import ru.hogwarts.school.service.EmblemFacultyService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/emblem")
public class EmblemFacultyController {
    private final EmblemFacultyService emblemFacultyService;

    public EmblemFacultyController(EmblemFacultyService emblemFacultyService) {
        this.emblemFacultyService = emblemFacultyService;
    }

    @PostMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadEmblem(@PathVariable Long id, @RequestParam MultipartFile emblem) {
        if (emblem.getSize() > 1024 * 10000) {
            return ResponseEntity.badRequest().body("Размер файла должен быть меньше 1мб");
        }
        emblemFacultyService.uploadEmblem(id, emblem);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public void downloadEmblem(@PathVariable Long id, HttpServletResponse response) {
        EmblemFaculty emblemFaculty = emblemFacultyService.findEmblemFaculty(id);
        Path path = Path.of(emblemFaculty.getFilePath());
        try (InputStream inputStream = Files.newInputStream(path);
             OutputStream outputStream = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(emblemFaculty.getMediaType());
            response.setContentLength((int) emblemFaculty.getFileSize());
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Возникла проблема при чтении или записи файла");
        }
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<byte[]> downloadEmblem(@PathVariable Long id) {
        EmblemFaculty emblemFaculty = emblemFacultyService.findEmblemFaculty(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(emblemFaculty.getMediaType()));
        httpHeaders.setContentLength(emblemFaculty.getPreview().length);

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(emblemFaculty.getPreview());
    }

}
