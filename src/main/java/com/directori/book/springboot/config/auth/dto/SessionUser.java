package com.directori.book.springboot.config.auth.dto;

import com.directori.book.springboot.domain.user.User;
import java.io.Serializable;
import lombok.Getter;

/**
 * 세션에 사용자 정보를 저장하기 위한 Dto 클래스
 * 인증된 사용자 정보만 필요하기 때문에 name, email, picture만 필드로 선언
 * User 클래스를 사용하지 않는 이유는 세션에 저장하기 위해서 직렬화를 구현해야 하지만
 * Entity클래스는 OneToMany, ManyToMany와 같이 자식 엔티티를 갖고있다면 직렬화 대상에 자식들까지 포함되어
 * 성능 이슈, 부수 효과가 발생할 확률이 높기 때문에
 * 직렬화 기능을 가진 세션 Dto 클래스를 하나 추가로 만드는 것이 이후 운영 및 유지보수에 이득ㅎ
 * */

@Getter
public class SessionUser implements Serializable {
  private String name;
  private String email;
  private String picture;

  public SessionUser(User user) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.picture = user.getPicture();
  }
}
