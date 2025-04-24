package website.code.coffeeShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import website.code.coffeeShop.configs.FileStorageProperties;
import website.code.coffeeShop.exception.FileNotFoundException;
import website.code.coffeeShop.exception.FileStorageException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties, FileStorageProperties fileStorageProperties1) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.fileStorageLocation);
        }catch (Exception e){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence." + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch (IOException e){
            throw new FileStorageException("Could not store file " + fileName + ".", e);
        }
    }

    @Override
    public Path load(String fileName) {return fileStorageLocation.resolve(fileName);}

    @Override
    public Resource loadAsResource(String fileName) {
        try{
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            } else {
                throw new FileStorageException("Could not read file " + fileName + ".");
            }
        }catch (MalformedURLException e) {
            throw new FileStorageException("Could not read file " + fileName + ".", e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
