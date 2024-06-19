package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.FindIdDto;
import com.minizin.travel.user.domain.dto.FindPasswordDto;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.enums.UserErrorCode;
import com.minizin.travel.user.domain.exception.CustomUserException;
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
                .orElseThrow(() -> new CustomUserException(UserErrorCode.EMAIL_UN_REGISTERED));

        return FindIdDto.Response.builder().username(userEntity.getUsername()).build();
    }

    @Transactional
    public void findPassword(FindPasswordDto.Request request) {
        UserEntity userEntity = userRepository.findByUsernameAndEmail(request.getUsername(), request.getEmail())
                .orElseThrow(() -> new CustomUserException(UserErrorCode.USER_NOT_FOUND));

        String password = mailService.sendTemporaryPassword(request);
        userEntity.setPassword(password);
    }

}
