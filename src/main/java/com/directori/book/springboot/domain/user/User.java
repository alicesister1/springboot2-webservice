package com.directori.book.springboot.domain.user;

import com.directori.book.springboot.domain.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 담당할 도메인 User 클래스
 */
@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column
  private String picture;

  /**
   * JPA로 데이터베이스로 저장할 때 Enum값을 어떤 형태로 저장할지 설정
   * 디폴트값은 int
   * 숫자로 저장되면 데이터베이스로 확인할 때 그 값이 무슨 코드를 의미하는 지 알 수 없음
   * 때문에 문자열로 저장하도록 설정
   */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Builder
  public User(String name, String email, String picture,
      Role role) {
    this.name = name;
    this.email = email;
    this.picture = picture;
    this.role = role;
  }

  public User update(String name, String picture) {
    this.name = name;
    this.picture = picture;

    return this;
  }

  public String getRoleKey() {
    return this.role.getKey();
  }
}
