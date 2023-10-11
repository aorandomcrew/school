package ru.hogwarts.school.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.DataNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;



@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    @Value("${path.to.avatars.folder}")
    private Path pathToAvatars;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }
    public Avatar getById(Long id){
        return avatarRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }


    public Long save(Long studentId, MultipartFile multipartFile) throws IOException{
        String fullPath = saveToDisk(studentId,multipartFile);
        Avatar avatar = saveToDb(studentId,multipartFile,fullPath);
        return avatar.getId();
    }
    public String saveToDisk(Long studentId, MultipartFile multipartFile) throws IOException {
        //метод создаст папки, если их нет
        Files.createDirectories(pathToAvatars);
        //формируем имя файла
        String originalFileName = multipartFile.getOriginalFilename();
        //расширешние файла
        int index = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(index);
        //имя
        String fileName = studentId + fileExtension;
        //сохранение файла(toAbsolutePath() - полный путь до файла)
        String fullPath = pathToAvatars.toAbsolutePath().toFile() + "/" + fileName;
        FileOutputStream path = new FileOutputStream(fullPath);
        multipartFile.getInputStream().transferTo(path);
        path.close();
        return fullPath;
    }
    public Avatar saveToDb(Long studentId,MultipartFile multipartFile, String fullPath) throws IOException{
        Student studentReference = studentRepository.getReferenceById(studentId);
        Avatar avatar = avatarRepository.findByStudent(studentReference)
                .orElse(new Avatar());
        avatar.setStudent(studentReference);
        avatar.setFilePath(fullPath);
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setData(multipartFile.getBytes());
        avatarRepository.save(avatar);
        return avatar;
    }
}
