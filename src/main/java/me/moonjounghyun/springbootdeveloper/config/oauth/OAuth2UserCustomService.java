package me.moonjounghyun.springbootdeveloper.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.moonjounghyun.springbootdeveloper.domain.User;
import me.moonjounghyun.springbootdeveloper.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomService OAuth2User loadUser userRequest -> " + userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        try {
            // OAuth2 로그인 처리 및 사용자 정보 가져오기
            OAuth2User user = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환
            log.info("CustomService OAuth2User loadUser user -> " + user);
            log.info("getAttributes : {}", user.getAttributes());
            OAuthAttributes attributes = OAuthAttributes.of(registrationId, "name", user.getAttributes());
            // 사용자 정보 저장 또는 업데이트
            saveOrUpdate(user);
            return user;
        } catch (OAuth2AuthenticationException e) {
            log.error("OAuth2 authentication error", e);
            throw e;
        }
    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User) {
        log.info("CustomService OAuth2User saveOrUpdate");
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 사용자 정보 추출
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElseGet(() -> createUser(email, name));

        return userRepository.save(user);
    }

    private User createUser(String email, String name) {
        System.out.println("CustomService OAuth2User CreateUser");
        log.info("createUser");
        return User.builder()
                .email(email)
                .nickname(name)
                .build();
    }
}

