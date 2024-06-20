package spring.oauth2.keycloak.spring_oauth2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.oauth2.keycloak.spring_oauth2.entities.DTOs.UserDTO;
import spring.oauth2.keycloak.spring_oauth2.service.IKeycloakService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('admin_client')")
public class KeycloakController {

    @Autowired
    private IKeycloakService keycloakService;

    @GetMapping("/search")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(keycloakService.findAll());
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> findUserById(@PathVariable(value = "username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(keycloakService.searchUserByUsername(username));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(keycloakService.creatUser(userDTO));
    }
    
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO ) {
        keycloakService.updateUser(userId, userDTO);
        Map<String,String> body = new HashMap<>();
        body.put("message", "User update successfull!!");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") String userId){
        keycloakService.deleteUser(userId);
        Map<String,String> body = new HashMap<>();
        body.put("message", "User deleted!!");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body);
    }
    

}
