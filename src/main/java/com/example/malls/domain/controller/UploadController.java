
package com.example.malls.domain.controller;

import com.example.malls.domain.dto.upload.UploadFileDTO;
import com.example.malls.domain.dto.upload.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UploadController {
    @Value("${com.example.malls.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public ResponseEntity<List<UploadResultDTO>> upload(UploadFileDTO uploadFileDTO) {
        log.info(uploadFileDTO);

        // todo 1. File Validate Check
        if (uploadFileDTO.getFiles() == null || uploadFileDTO.getFiles().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<UploadResultDTO> list = new ArrayList<>();

        // todo 2. UploadFileDTO -> bring the file data
        for (MultipartFile multipartFile : uploadFileDTO.getFiles()) {
            String originalName = multipartFile.getOriginalFilename();
            log.info("원본 파일명 {}", originalName);

            // todo 3. save file root and uuid processing
            String uuid = UUID.randomUUID().toString();
            Path savePath = Paths.get(uploadPath, uuid+"_"+ originalName);
//            Path savePath = Paths.get(uploadPath, uuid+ File.separator+ originalName);
            boolean image = false;

            // todo 4. file saving(transferTo) and make the Thumbnail file
            try {
                multipartFile.transferTo(savePath);
                log.info("파일 저장 경로: {}", savePath.toString());

                // 이미지 파일 종류시
                if(Files.probeContentType(savePath).startsWith("image")){
                    image = true;

                    File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
//                    File thumbFile = new File(uploadPath, "s_"+uuid+File.separator+originalName);

                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    log.info("썸네일 생성 경로: {}", thumbFile.getAbsolutePath()); // 썸네일 생성 경로 로그 추가

                }
            } catch (IOException e) {
                log.error("파일 업로드 오류", e);
            }

            // todo 5. return result(UploadResultDTO)
            list.add(UploadResultDTO.builder()
                    .uuid(uuid)
                    .fileName(originalName)
                    .img(image)
                    .build());
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        // todo 1. Generating File Resource
        String filePath = uploadPath + File.separator + fileName; // 파일 경로 생성
        Resource resource = new FileSystemResource(filePath); // 파일 시스템 리소스 생성

        log.info("파일 조회 경로: {}", filePath);

        HttpHeaders headers = new HttpHeaders();

        try {
            // todo 2. validation check
            if (!resource.exists()) { // 파일 존재 여부 확인
                log.error("파일이 존재하지 않습니다: {}", fileName);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{fileName}")
    public ResponseEntity<Map<String, Boolean>> removeFile(@PathVariable String fileName) {

        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);

        log.info("파일 삭제 경로 : {}", uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();

        boolean removed = false;

        try {
            // todo 1. validation check
            if(!resource.exists()){
                log.error("파일이 존재하지 않습니다 : {}",fileName) ;
                resultMap.put("result", false);
                return  new ResponseEntity<>(resultMap, HttpStatus.NOT_FOUND);
            }
            // todo 2. file remove
            String contentType = Files.probeContentType(resource.getFile().toPath());

            removed = resource.getFile().delete();

            // todo 3. thumbnail remove
            // 섬네일 존재시
            if (contentType != null && contentType.startsWith("image")) {
                File thumbFile = new File(uploadPath+File.separator+"s_"+fileName);
                thumbFile.delete();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
