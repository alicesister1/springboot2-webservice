package com.directori.book.springboot.config.oauth;

import com.directori.book.springboot.config.oauth.dto.OAuthAttributes;
import com.directori.book.springboot.config.oauth.dto.SessionUser;
import com.directori.book.springboot.domain.user.User;
import com.directori.book.springboot.domain.user.UserRepository;
import java.util.Collections;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;
  private final HttpSession httpSession;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    // 현재 로그인 진행 중인 서비스를 구분하는 코드, 추후 네이버, 카카오, 구글 로그인 구분하기 위해 사용됨
    String registrationId = userRequest.getClientRegistration().getRegistrationId();

    /**
     * OAuth2 로그인 진행시 키 값 (= PK). 구글의 경우 기본 코드 "sub",
     * 네이버 카카오 등은 기본 지원 않 함추후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용됨
     */
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    /**
     * OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
     */
    OAuthAttributes attributes = OAuthAttributes
        .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

    User user = saveOrUpdate(attributes);
    // 세션에 사용자 정보 저장
    httpSession.setAttribute("user", new SessionUser(user));

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
        attributes.getAttributes(),
        attributes.getNameAttributeKey());

  }

  private User saveOrUpdate(OAuthAttributes attributes) {
    User user = userRepository.findByEmail(attributes.getEmail())
        .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
        .orElse(attributes.toEntity());

    return userRepository.save(user);
  }
}
