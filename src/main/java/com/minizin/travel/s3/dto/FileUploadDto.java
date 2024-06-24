package com.minizin.travel.s3.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class: BaseEntity Project: package com.minizin.travel.chat.s3.dto
 * <p>
 * Description: FileUploadDto
 *
 * @author JANG CHIHUN
 * @date 6/8/24 10:20 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FileUploadDto {

	private MultipartFile file; // MultipartFile

	private String originFileName; // 파일 원본 이름

	private String transaction; // UUID 를 활용한 랜덤한 파일 위치

	private String charRoom; // 파일이 올라간 채팅방 ID

	private String s3DataUrl; // 파일 링크

	private String fileDir;  // S3 파일 경로
}
