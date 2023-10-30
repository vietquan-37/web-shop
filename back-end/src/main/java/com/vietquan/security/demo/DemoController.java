package com.vietquan.security.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hehe")
@CrossOrigin(origins = "http://localhost:4200")
public class DemoController {
    @GetMapping("/demo-controller")
    public ResponseEntity<String>sayHello(){
        return ResponseEntity.ok("Hello from secured endpoint");
    }

}
