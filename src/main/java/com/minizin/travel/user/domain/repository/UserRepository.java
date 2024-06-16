package com.minizin.travel.user.domain.repository;

import com.minizin.travel.user.domain.entity.UserEntity;

import java.util.Optional;

import com.minizin.travel.user.domain.enums.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<UserEntity> findByEmailAndLoginType(String email, LoginType loginType);

}
