package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.FindIdDto;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public FindIdDto.Response findId(FindIdDto.Request request) {
        UserEntity userEntity = userRepository.findByEmailAndLoginType(request.getEmail(), LoginType.LOCAL)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 이메일입니다."));

        return FindIdDto.Response.builder().username(userEntity.getUsername()).build();
    }

}
