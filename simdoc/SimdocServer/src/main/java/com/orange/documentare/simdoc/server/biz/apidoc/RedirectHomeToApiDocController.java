package com.orange.documentare.simdoc.server.biz.apidoc;

/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectHomeToApiDocController {

  @RequestMapping("/")
  public String home() {
    return "redirect:swagger-ui.html";
  }
}
