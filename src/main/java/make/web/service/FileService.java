package make.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); //랜덤 파일명 중복되지 않게
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); //확장자
        String savedFileName = uuid.toString() + extension; //저장될 파일의 이름(uuid) + 확장자(extension)
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if(deleteFile.exists()) {
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
