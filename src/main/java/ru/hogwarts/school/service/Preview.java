package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Preview {
    private static Logger logger= LoggerFactory.getLogger(Preview.class);
    public static byte[] generateImagePreview(Path filePath) {
        logger.debug("Вызван метод для уменьшения изображения.");
        try (InputStream inputStream = Files.newInputStream(filePath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            BufferedImage image = ImageIO.read(bufferedInputStream);
            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            String extension = (filePath.getFileName().toString()).substring((filePath.getFileName().toString()).lastIndexOf(".") + 1);

            ImageIO.write(preview, extension, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (
                IOException e) {
            logger.error("Возникла проблема при уменьшении файла");
            throw new RuntimeException("Возникла проблема при уменьшении файла");
        }
    }
}
