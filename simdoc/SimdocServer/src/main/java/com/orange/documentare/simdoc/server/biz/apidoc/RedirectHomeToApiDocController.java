package com.orange.documentare.simdoc.server.biz.apidoc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectHomeToApiDocController {

  @RequestMapping("/")
  public String home() {
    return "redirect:swagger-ui.html";
  }
}
