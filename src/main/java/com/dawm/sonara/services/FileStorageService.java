package com.dawm.sonara.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload-root}")
    private String uploadPath;

    public String storeFile(MultipartFile file) {
        try {
            // Validar que no esté vacío
            if (file.isEmpty()) throw new RuntimeException("Archivo vacío");

            // Crear nombre único
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path root = Paths.get(uploadPath);

            // Crear carpeta si no existe
            if (!Files.exists(root)) Files.createDirectories(root);

            Files.copy(file.getInputStream(), root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar en " + uploadPath, e);
        }
    }
}