package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.*;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.enums.UserErrorCode;
import com.minizin.travel.user.domain.exception.CustomUserException;
import com.minizin.travel.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
    }

    @Transactional
    public UpdatePasswordDto.Response updatePassword(
            UpdatePasswordDto.Request request, PrincipalDetails principalDetails) {
        UserEntity userEntity = userRepository.findByUsername(principalDetails.getUsername())
                .orElseThrow(() -> new CustomUserException(UserErrorCode.USER_NOT_FOUND));
        if (!bCryptPasswordEncoder.matches(request.getOriginalPassword(), userEntity.getPassword())) {
            throw new CustomUserException(UserErrorCode.PASSWORD_UN_MATCHED);
        }

        userEntity.setPassword(bCryptPasswordEncoder.encode(request.getChangePassword()));

        return UpdatePasswordDto.Response.fromUserEntity(userEntity);
    }

    @Transactional
    public UpdateEmailDto.Response updateEmail(
            UpdateEmailDto.Request request, PrincipalDetails principalDetails
    ) {
        UserEntity userEntity = userRepository.findByUsername(principalDetails.getUsername())
                .orElseThrow(() -> new CustomUserException(UserErrorCode.USER_NOT_FOUND));

        userEntity.setEmail(request.getEmail());

        return UpdateEmailDto.Response.fromUserEntity(userEntity);
    }

    @Transactional
    public UpdateNicknameDto.Response updateNickname(
            UpdateNicknameDto.Request request, PrincipalDetails principalDetails
    ) {
        UserEntity userEntity = userRepository.findByUsername(principalDetails.getUsername())
                .orElseThrow(() -> new CustomUserException(UserErrorCode.USER_NOT_FOUND));

        userEntity.setNickname(request.getNickname());

        return UpdateNicknameDto.Response.fromUserEntity(userEntity);
    }
}
