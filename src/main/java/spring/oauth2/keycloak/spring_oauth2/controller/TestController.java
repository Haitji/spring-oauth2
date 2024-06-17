package spring.oauth2.keycloak.spring_oauth2.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class TestController {

    @GetMapping("/hello-1")
    @PreAuthorize("hashRole('admin_client')")
    public String helloAdmin() {
        return "Hello Spring Boot with Keycloak -ADMIN";
    }

    @GetMapping("/hello-2")
    @PreAuthorize("hashRole('user_client')")
    public String helloUser() {
        return "Hello Spring Boot with Keycloak -USER";
    }
    

}
