package com.directori.book.springboot.web;

import com.directori.book.springboot.config.auth.LoginUser;
import com.directori.book.springboot.config.auth.dto.SessionUser;
import com.directori.book.springboot.service.posts.PostsService;
import com.directori.book.springboot.web.dto.PostsResponseDto;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

  private final PostsService postsService;
  private final HttpSession httpSession;

  /**
   *  mustache starter는 컨트롤러에서 문자열을 반환할 때,
   *  경로와 파일 확장자는 자동으로 지정됨
   *  기본 경로는 src/main/resources/templates
   */
  /*
  @RequestMapping("/")
  public String home(Model model) {
    model.addAttribute("message", "Hello, World!");
    return "index";
  }
  */

  @GetMapping("/")
  public String index(Model model, @LoginUser SessionUser user) {
    model.addAttribute("posts", postsService.findAllDesc());

    if (user != null) {
      model.addAttribute("userName", user.getName());
    }
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
