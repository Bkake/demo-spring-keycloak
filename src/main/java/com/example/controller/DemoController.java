package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @GetMapping(path = {"/", "/unsecured"})
  public String noSecuredEndpoint() {
      return "This is an unsecured endpoint payload";
  }

  @GetMapping("/admin")
  public String adminSecuredEndpoint(){
      return "This is an ADMIN endpoint payload";
  }

  @GetMapping("/user")
  public String userSecuredEndpoint(){
      return "This is an USER endpoint payload";
  }
}
