package com.directori.book.springboot.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.directori.book.springboot.domain.posts.Posts;
import com.directori.book.springboot.domain.posts.PostsRepository;
import com.directori.book.springboot.web.dto.PostsResponseDto;
import com.directori.book.springboot.web.dto.PostsSaveRequestDto;
import com.directori.book.springboot.web.dto.PostsUpdateRequestDto;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

// JPA 기능 테스트를 위해 WebMvcTest 대신 SpringBootTest, TestRestTemplate 사용

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  PostsRepository postsRepository;

  @After
  public void tearDown() throws Exception {
    postsRepository.deleteAll();
  }

  @Test
  public void Posts_등록된다() throws Exception {
    //given
    String title = "title";
    String content = "content";
    PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
        .title(title)
        .content(content)
        .author("ykh")
        .build();

    String url = "http://localhost:" + port + "/api/v1/posts";

    //when
    ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isGreaterThan(0L);

    List<Posts> all = postsRepository.findAll();
    assertThat(all.get(0).getTitle()).isEqualTo(title);
    assertThat(all.get(0).getContent()).isEqualTo(content);
  }

  @Test
  public void Posts_수정된다() throws Exception {
    //given
    Posts savedPosts = postsRepository.save(Posts.builder()
    .title("title")
    .content("content")
    .author("author")
    .build());

    Long updateId = savedPosts.getId();
    String expectedTitle = "title2";
    String expectedContent = "content2";

    PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
        .title(expectedTitle)
        .content(expectedContent)
        .build();

    String url = "http://localhost:" + port + "api/v1/posts/" + updateId;

    HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

    //when
    ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isGreaterThan(0L);

    List<Posts> all = postsRepository.findAll();
    assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
    assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
  }

  @Test
  public void Posts_조회된다() throws Exception {
    //given
    Posts savePosts = postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

    String url = "http://localhost:"+ port + "/api/v1/posts/" + savePosts.getId();

    HttpEntity<String> requestEntity = new HttpEntity<>("");

    //when
    ResponseEntity<PostsResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, PostsResponseDto.class);

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
