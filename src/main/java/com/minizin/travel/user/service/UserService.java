package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.FindIdDto;
import com.minizin.travel.user.domain.dto.FindPasswordDto;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailService mailService;

    public FindIdDto.Response findId(FindIdDto.Request request) {
        UserEntity userEntity = userRepository.findByEmailAndLoginType(request.getEmail(), LoginType.LOCAL)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 이메일입니다."));

        return FindIdDto.Response.builder().username(userEntity.getUsername()).build();
    }

    @Transactional
    public void findPassword(FindPasswordDto.Request request) {
        UserEntity userEntity = userRepository.findByUsernameAndEmail(request.getUsername(), request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        String password = mailService.sendTemporaryPassword(request);
        userEntity.setPassword(password);
    }

}
