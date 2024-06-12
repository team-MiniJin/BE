package com.minizin.travel.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minizin.travel.user.domain.User;

/**
 * Class: BaseEntity Project: com.minizin.travel.user.repository
 * <p>
 * Description: UserRepository
 *
 * @author JANG CHIHUN
 * @date 6/3/24 21:30 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
