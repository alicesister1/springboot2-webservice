package com.directori.book.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  @GetMapping("/")
  public String index() {

    // mustache start는 컨트롤러에서 문자열을 반환할 때 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됨
    // 기본 경로는 src/main/resources/templates
    return "index"; // same as src/main/resources/templates/index.mustache
  }

  @GetMapping("/posts/save")
  public String postsSave() {
    return "posts-save";
  }
}
