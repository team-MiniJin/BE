package com.minizin.travel.s3.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class: BaseEntity Project: package com.minizin.travel.s3.domain
 * <p>
 * Description: ChatFile
 *
 * @author JANG CHIHUN
 * @date 6/17/24 11:35 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class ChatFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String roomId;
	private String fileName;
	private String filePath;
	private String s3Url;
	private LocalDateTime timestamp;
}
