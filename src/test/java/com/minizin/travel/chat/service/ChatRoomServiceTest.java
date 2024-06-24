package com.minizin.travel.chat.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.minizin.travel.chat.domain.ChatRoom;
import com.minizin.travel.chat.dto.ChatRoomRequest;
import com.minizin.travel.chat.dto.ChatRoomResponse;
import com.minizin.travel.chat.repository.ChatRoomRepository;
import com.minizin.travel.s3.service.FileService;

/**
 * Class: BaseEntity Project: com.minizin.travel.user.service.UserService
 * <p>
 * Description: ChatRoomServiceTest
 *
 * @author JANG CHIHUN
 * @date 6/7/24 19:40 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */

class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void CreateChatRoomSuccessTest(){
        //given
        ChatRoomRequest request = new ChatRoomRequest("testRoom", "testPwd", true, 1);
        ChatRoom chatRoom = ChatRoomResponse.toEntity(request);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);

        //when
        ChatRoomResponse response = chatRoomService.createChatRoom(request);

        //then
        assertThat(request.getRoomName()).isEqualTo(response.roomName());
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    void findAllRoomSuccessTest(){
        //given
        ChatRoom chatRoom1 = ChatRoom.builder()
                .roomId("1")
                .roomName("Room1")
                .build();

        ChatRoom chatRoom2 = ChatRoom.builder()
                .roomId("2")
                .roomName("Room2")
                .build();

        List<ChatRoom> chatRooms = Arrays.asList(chatRoom1, chatRoom2);
        when(chatRoomRepository.findAll()).thenReturn(chatRooms);

        //when
        List<ChatRoomResponse> responses = chatRoomService.findAllRoom();

        //then
        assertThat(responses.size()).isEqualTo(2);
        verify(chatRoomRepository, times(1)).findAll();
    }

    @Test
    void findRoomByIdTest(){
        //given
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId("1")
                .roomName("Room")
                .build();

        when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));

        //when
        ChatRoomResponse response = chatRoomService.findRoomById("1");

        //then
        assertThat("Room").isEqualTo(response.roomName());
        verify(chatRoomRepository, times(1)).findByRoomId("1");
    }

    @Test
    public void getUserListSuccessTest() {
        //given
        ChatRoom chatRoom = ChatRoom.builder()
            .roomId("1")
            .userList(new HashMap<>(Map.of("uuid1", "user1", "uuid2", "user2")))
            .build();

        when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));
        //when
        List<String> userList = chatRoomService.getUserList("1");

        //then
        assertThat(userList.size()).isEqualTo(2);
        verify(chatRoomRepository, times(1)).findByRoomId("1");
    }

    @Test
    public void delUserSuccessTest() {
        //given
        Map<String, String> userList = new HashMap<>();
        userList.put("uuid", "user");

        ChatRoom chatRoom = ChatRoom.builder()
            .roomId("1")
            .userList(userList)
            .build();

        when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));

        //when
        chatRoomService.delUser("1", "uuid");
        //then
        assertThat(chatRoom.getUserList().containsKey("uuid")).isFalse();
        verify(chatRoomRepository, times(1)).findByRoomId("1");
    }

    @Test
    void delChatRoomSuccessTest() {
        //given
        ChatRoom chatRoom = ChatRoom.builder()
            .roomId("1")
            .build();

        when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));
        doNothing().when(fileService).deleteFileDir("1");

        //when
        chatRoomService.delChatRoom("1");

        //then
        verify(chatRoomRepository, times(1)).delete(chatRoom);
        verify(fileService, times(1)).deleteFileDir("1");
    }

    @Test
    void isDuplicateName_Success() {
        //given
        Map<String, String> userList = new HashMap<>();
        userList.put("uuid1", "user1");

        ChatRoom chatRoom = ChatRoom.builder()
            .roomId("1")
            .userList(userList)
            .build();

        when(chatRoomRepository.findByRoomId("1")).thenReturn(Optional.of(chatRoom));

        //when
        String uniqueName = chatRoomService.isDuplicateName("1", "user1");

        assertThat(uniqueName).isNotNull();
        assertThat("user1").isNotEqualTo(uniqueName);
        verify(chatRoomRepository, times(1)).findByRoomId("1");
    }
}
