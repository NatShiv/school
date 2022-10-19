package ru.hogwarts.school.controller;

import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarStudentService;

@RestController("/avatar")
public class AvatarStudentController {
    private final AvatarStudentService avatarStudentService;

    public AvatarStudentController(AvatarStudentService avatarStudentService) {
        this.avatarStudentService = avatarStudentService;
    }

    @PostMapping(value = "{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadEmblem(@PathVariable Long studentId, @RequestParam MultipartFile avatar){
        if (avatar.getSize() > 1024 * 10000) {
            return ResponseEntity.badRequest().body("Размер файла должен быть меньше 10мб");
        }
        avatarStudentService.uploadAvatarStudent(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long id) {
        Pair<String, byte[]> pair = avatarStudentService.getAvatar(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getFirst()))
                .contentLength(pair.getSecond().length)
                .body(pair.getSecond());
    }

    @GetMapping("/{id}/previev")
    public ResponseEntity<byte[]> getPreview(@PathVariable Long id) {
        Pair<String, byte[]> pair = avatarStudentService.getPreview(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getFirst()))
                .contentLength(pair.getSecond().length)
                .body(pair.getSecond());

    }
}
