package com.minizin.travel.s3.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.minizin.travel.s3.dto.FileUploadDto;
import com.minizin.travel.s3.service.S3FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.s3.controller
 * <p>
 * Description: FileController
 *
 * @author JANG CHIHUN
 * @date 6/8/24 15:20 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {

	private final S3FileService fileService;

	// 프론트에서 ajax 를 통해 /upload 로 MultipartFile 형태로 파일과 roomId 를 전달받는다.
	// 전달받은 file 를 uploadFile 메서드를 통해 업로드한다.
	@PostMapping("/s3/upload")
	public FileUploadDto uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("roomId") String roomId) {
		FileUploadDto fileReq = fileService.uploadFile(file, UUID.randomUUID().toString(), roomId);
		log.info("최종 upload Data {}", fileReq);

		// fileReq 객체 리턴
		return fileReq;
	}

	// get 으로 요청이 오면 아래 download 메서드를 실행한다.
	// fileName 과 파라미터로 넘어온 fileDir 을 getObject 메서드에 매개변수로 넣는다.
	@GetMapping("/s3/download/{fileName}")
	public ResponseEntity<byte[]> download(@PathVariable String fileName, @RequestParam("fileDir") String fileDir) {
		log.info("fileDir : fileName [{} : {}]", fileDir, fileName);
		try {
			// 변환된 byte, httpHeader 와 HttpStatus 가 포함된 ResponseEntity 객체를 return 한다.
			return fileService.getObject(fileDir, fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
