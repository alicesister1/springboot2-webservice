package com.directori.book.springboot.service.posts;

import com.directori.book.springboot.domain.posts.Posts;
import com.directori.book.springboot.domain.posts.PostsRepository;
import com.directori.book.springboot.web.dto.PostsResponseDto;
import com.directori.book.springboot.web.dto.PostsSaveRequestDto;
import com.directori.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

  private final PostsRepository postsRepository;

  @Transactional
  public Long save(PostsSaveRequestDto requestDto) {
    return postsRepository.save(requestDto.toEntity()).getId();
  }

  @Transactional
  public Long update(Long id, PostsUpdateRequestDto requestDto) {
    Posts posts = postsRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

    /**
     * update가 posts에 있는 이유는 객체지향에서 객체의 상태 변경은 그 객체가 해야할 일이기 때문
     * JPA에서는 엔티티를 조회하면 해당 엔티티의 조회 상태 그대로 스냅샷을 만들어놓습니다.
     * 그리고 트랜잭션이 끝나는 시점에는 이 스냅샷과 비교해서 다른점이 있다면 Update Query를 데이터베이스로 전달
     */
    posts.update(requestDto.getTitle(), requestDto.getContent());

    return id;
  }

  public PostsResponseDto findById(Long id) {
    Posts entity = postsRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

    return new PostsResponseDto(entity);
  }
}
