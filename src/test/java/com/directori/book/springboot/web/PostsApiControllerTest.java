package com.directori.book.springboot.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.directori.book.springboot.domain.posts.Posts;
import com.directori.book.springboot.domain.posts.PostsRepository;
import com.directori.book.springboot.web.dto.PostsResponseDto;
import com.directori.book.springboot.web.dto.PostsSaveRequestDto;
import com.directori.book.springboot.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.After;
import org.junit.Before;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  /**
   * 매 번 테스트 시작 전 MockMvc 인스턴스 생성*/
  @Before
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @After
  public void tearDown() throws Exception {
    postsRepository.deleteAll();
  }

  @Test
  @WithMockUser(roles = "USER")
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
//    ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
    mvc.perform(post(url)
    .contentType(MediaType.APPLICATION_JSON_UTF8)
    .content(new ObjectMapper().writeValueAsString(requestDto)))
        .andExpect(status().isOk());

    //then
//    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertThat(responseEntity.getBody()).isGreaterThan(0L);
    List<Posts> all = postsRepository.findAll();
    assertThat(all.get(0).getTitle()).isEqualTo(title);
    assertThat(all.get(0).getContent()).isEqualTo(content);
  }

  @Test
  @WithMockUser(roles = "USER")
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
//    ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
    mvc.perform(put(url)
    .contentType(MediaType.APPLICATION_JSON_UTF8)
    .content(new ObjectMapper().writeValueAsString(requestDto)))
        .andExpect(status().isOk());

    //then
//    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertThat(responseEntity.getBody()).isGreaterThan(0L);
    List<Posts> all = postsRepository.findAll();
    assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
    assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
  }

  @Test
  @WithMockUser(roles = "USER")
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
//    ResponseEntity<PostsResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, PostsResponseDto.class);
    mvc.perform(get(url)
    .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
//    .content(new ObjectMapper().writeValueAsString(requestEntity)))

    //then
//    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

  }
}
