package com.minizin.travel.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class: BaseEntity Project: com.minizin.travel.chat.dto
 * <p>
 * Description: WebSocketMessage
 *
 * @author JANG CHIHUN
 * @date 6/5/24 10:10 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {
	private String from; // 보내는 유저 UUID
	private String type; // 메시지 타입
	private String data; // roomId
	private Object candidate; // 상태
	private Object sdp; // sdp 정보
}
