package tn.esprit.projet_pi.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class FileStorageService {
    private static final Logger LOGGER = Logger.getLogger(FileStorageService.class.getName());

    private final Path fileStorageLocation;



    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            LOGGER.severe("❌ Impossible de créer le répertoire où les fichiers téléchargés seront stockés.");
            throw new RuntimeException("Impossible de créer le répertoire où les fichiers téléchargés seront stockés.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            // Normalize file name
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            // Generate unique file name
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info("✅ Fichier enregistré avec succès: " + fileName);
            return fileName;
        } catch (IOException ex) {
            LOGGER.severe("❌ Échec du stockage du fichier. Erreur: " + ex.getMessage());
            throw new RuntimeException("Échec du stockage du fichier.", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                LOGGER.warning("⚠️ Fichier non trouvé: " + fileName);
                throw new RuntimeException("Fichier non trouvé: " + fileName);
            }
        } catch (MalformedURLException ex) {
            LOGGER.severe("❌ Erreur lors du chargement du fichier: " + fileName);
            throw new RuntimeException("Erreur lors du chargement du fichier: " + fileName, ex);
        }
    }
}