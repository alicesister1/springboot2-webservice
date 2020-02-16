package com.directori.book.springboot.config.auth.dto;

import com.directori.book.springboot.domain.user.Role;
import com.directori.book.springboot.domain.user.User;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * {@link org.springframework.security.oauth2.client.userinfo.OAuth2UserService}를 통해 가져온
 * {@link org.springframework.security.oauth2.core.user.OAuth2User}의 attributes를 담을 클래스
 */

@Getter
public class OAuthAttributes {
  private Map<String, Object> attributes;
  private String nameAttributeKey;
  private String name;
  private String email;
  private String picture;

  @Builder
  public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
      String name, String email, String picture) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.picture = picture;
  }

  /**
   * OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해야 함
   */
  public static OAuthAttributes of(String registrationId, String userNameAttributeName,
      Map<String, Object> attributes) {
    return ofGoogle(userNameAttributeName, attributes);
  }

  private static OAuthAttributes ofGoogle(String userNameAttributeName,
      Map<String, Object> attributes){
    return OAuthAttributes.builder()
        .name((String) attributes.get("name"))
        .email((String) attributes.get("email"))
        .picture((String) attributes.get("picture"))
        .attributes(attributes)
        .nameAttributeKey(userNameAttributeName)
        .build();
  }

  /**
   * User 엔티티를 생성함
   * OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입할 때
   * 가입할 때의 기본 권한을 GUEST로 주기 위해서 role 빌더 값에는 Role.GUEST를 사용함
   * */
  public User toEntity() {
    return User.builder()
        .name(name)
        .email(email)
        .picture(picture)
        .role(Role.GUEST)
        .build();
  }
}
