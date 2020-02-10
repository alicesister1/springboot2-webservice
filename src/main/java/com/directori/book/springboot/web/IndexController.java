package com.directori.book.springboot.web;

import com.directori.book.springboot.service.posts.PostsService;
import com.directori.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

  private final PostsService postsService;

//  @GetMapping("/")
//  public String index() {
//
//    // mustache start는 컨트롤러에서 문자열을 반환할 때 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됨
//    // 기본 경로는 src/main/resources/templates
//    return "index"; // same as src/main/resources/templates/index.mustache
//  }
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("posts", postsService.findAllDesc());
    return "index";
  }

  @GetMapping("/posts/save")
  public String postsSave() {
    return "posts-save";
  }

  @GetMapping("/posts/update/{id}")
  public String postsUpdate(@PathVariable Long id, Model model) {
    PostsResponseDto dto = postsService.findById(id);
    model.addAttribute("post", dto);

    return "posts-update";
  }
}
