package com.directori.book.springboot.web.dto;

import com.directori.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Service 클래스와 Controller 클래스에서 사용할 Dto 클래스는 반드시 분리하여 사용해야합니다.

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
  private String title;
  private String content;
  private String author;

  @Builder
  public PostsSaveRequestDto(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }

  public Posts toEntity() {
    return Posts.builder()
        .title(title)
        .content(content)
        .author(author)
        .build();
  }
}
