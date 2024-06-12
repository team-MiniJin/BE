package com.minizin.travel.chat.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.minizin.travel.chat.domain.ChatRoom;
import com.minizin.travel.chat.repository.ChatRoomRepository;

/**
 * Class: BaseEntity Project: com.minizin.travel.user.service.UserService
 * <p>
 * Description: ChatServiceTest
 *
 * @author JANG CHIHUN
 * @date 6/7/24 19:50 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

class ChatServiceTest {

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@InjectMocks
	private ChatService chatService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void plusUserCntSuccessTest() {
	    //given
		ChatRoom chatRoom = ChatRoom.builder()
			.roomId("1")
			.userCount(0)
			.build();

		when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));

	    //when
		chatService.plusUserCnt("1");

	    //then
		assertThat(chatRoom.getUserCount()).isEqualTo(1);
		verify(chatRoomRepository, times(1)).findByRoomId("1");
	}

	@Test
	void minusUserCntSuccessTest() {
		//given
		ChatRoom chatRoom = ChatRoom.builder()
			.roomId("1")
			.userCount(1)
			.build();

		when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));

		//when
		chatService.minusUserCnt("1");

		//then
		assertThat(chatRoom.getUserCount()).isEqualTo(0);
		verify(chatRoomRepository, times(1)).findByRoomId("1");
	}

	@Test
	void chkRoomUserCnt_SuccessTest() {
		//given
		ChatRoom chatRoom = ChatRoom.builder()
			.roomId("1")
			.userCount(1)
			.maxUserCnt(5)
			.build();

		when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));

		//when
		boolean canAddUser = chatService.chkRoomUserCnt("1");

		assertThat(canAddUser).isTrue();
		verify(chatRoomRepository, times(1)).findByRoomId("1");
	}

	@Test
	void chkRoomUserCnt_FailedTest() {
		//given
		ChatRoom chatRoom = ChatRoom.builder()
			.roomId("1")
			.userCount(5)
			.maxUserCnt(5)
			.build();

		when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));

		//when
		boolean canAddUser = chatService.chkRoomUserCnt("1");

		assertThat(canAddUser).isFalse();
		verify(chatRoomRepository, times(1)).findByRoomId("1");
	}
}
