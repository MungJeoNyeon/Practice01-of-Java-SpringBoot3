package me.moonjounghyun.springbootdeveloper.config.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Builder
@Slf4j
public class OAuthAttributes {
    private String name;
    private String email;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        log.info("OAuthAttributes registrationId -> " + registrationId);

        switch (registrationId) {
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            case "kakao":
                return ofKakao(userNameAttributeName, attributes);
            default:
                throw new IllegalArgumentException("Unsupported registrationId: " + registrationId);
        }
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        log.info("구글 로그인 요청");
        String name = (String) attributes.get(userNameAttributeName);
        String email = (String) attributes.get("email");
        return OAuthAttributes.builder()
                .name(name)
                .email(email)
                .build();
    }

    @SuppressWarnings("unchecked")
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        log.debug("카카오 로그인 요청");
        try {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount == null) {
                throw new IllegalArgumentException("Invalid Kakao attributes. 'kakao_account' attribute is missing.");
            }

            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
            if (kakaoProfile == null) {
                throw new IllegalArgumentException("Invalid Kakao attributes. 'profile' attribute is missing.");
            }

            String name = (String) kakaoProfile.get("nickname");
            String email = (String) kakaoAccount.get("email");

            return OAuthAttributes.builder()
                    .name(name)
                    .email(email)
                    .build();
        } catch (Exception e) {
            log.error("Error in parsing Kakao attributes", e);
            throw e;
        }
    }
}
