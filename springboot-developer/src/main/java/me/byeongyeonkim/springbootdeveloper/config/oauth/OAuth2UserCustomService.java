package me.byeongyeonkim.springbootdeveloper.config.oauth;

import lombok.RequiredArgsConstructor;
import me.byeongyeonkim.springbootdeveloper.domain.User;
import me.byeongyeonkim.springbootdeveloper.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest); // 요청을 바탕으로 유저 정보를 담은 객체 반환
        saveOrUpdate(user);

        return user;
    }

    // 유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String uniqueNickname = generateUniqueNickname(name); // 고유 닉네임 생성

        return userRepository.findByEmail(email)
                .map(entity -> entity.update(name)) // 기존 사용자 업데이트
                .orElseGet(() -> userRepository.save( // 새로운 사용자 저장
                        User.builder()
                                .email(email)
                                .nickname(uniqueNickname)
                                .build()
                ));
    }

    private String generateUniqueNickname(String baseName) {
        String nickname = baseName;
        int counter = 1;
        while (userRepository.existsByNickname(nickname)) {
            nickname = baseName + counter; // 중복 시 숫자 추가
            counter++;
        }
        return nickname;
    }
}
