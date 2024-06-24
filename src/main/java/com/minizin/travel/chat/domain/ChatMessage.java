package com.minizin.travel.chat.domain;

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
 * Class: BaseEntity Project: package com.minizin.travel.chat.domain
 * <p>
 * Description: ChatMessage
 *
 * @author JANG CHIHUN
 * @date 6/74/24 11:30 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String roomId;
	private String sender;
	private String message;
	private LocalDateTime timestamp;
}
