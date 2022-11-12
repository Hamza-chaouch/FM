package com.solaries.fileManager.services.storage;

import com.solaries.fileManager.entities.SODocument;
import com.solaries.fileManager.repositories.SODocumentRepository;
import com.solaries.fileManager.services.storage.exception.StorageException;
import com.solaries.fileManager.services.storage.exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation;
    private final  SODocumentRepository soDocumentRepository;
    @Autowired
    public FileSystemStorageService (StorageProperties properties, SODocumentRepository soDocumentRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.soDocumentRepository = soDocumentRepository;
    }

    @Override
    public void store (MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = Paths.get(getPathForUpload()).resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(Paths.get(getPathForUpload()).toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                // to do add code to persist file into database

                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
                System.out.println("file.getOriginalFilename() ===>"+rootLocation.resolve(file.getOriginalFilename()));
                SODocument soDocument = (new SODocument()).
                        setSize(file.getSize()).
                        setCreatedAt(LocalDateTime.now()).
                        setName(file.getOriginalFilename()).
                        setFilePath(getPathForUpload()+"/"+file.getOriginalFilename());
                soDocumentRepository.saveAndFlush(soDocument);

            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll () {
        try {

            createUploadDir();
            return Files.walk(this.rootLocation, 10)
                    .filter(path -> !path.equals(this.rootLocation))
                    // to display only files
                    .filter(Files::isRegularFile)
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }


    private String getPathForUpload () {
        int yearDir = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        return   this.rootLocation+"/"+yearDir+"/"+month;
    }

    private void createUploadDir () throws IOException {

        String directoryName = getPathForUpload();
        File directory = new File(directoryName);
        if (!directory.exists()){
           // directory.mkdirs();
            Files.createDirectories(Paths.get(directory.toURI()));
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
    }

    @Override
    public Path load (String filename) {

        SODocument file = soDocumentRepository.findByName(filename);
        System.out.println("=>>>>>>>>>>>>>  "+file);
        return Paths.get(file.getFilePath());
    }

    @Override
    public Resource loadAsResource (String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable() || resource.isFile()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
