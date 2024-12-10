package me.byeongyeonkim.springbootdeveloper.repository;

import me.byeongyeonkim.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // email로 사용자 정보를 가져옴
    // 닉네임 중복 여부 확인
    boolean existsByNickname(String nickname);
}
