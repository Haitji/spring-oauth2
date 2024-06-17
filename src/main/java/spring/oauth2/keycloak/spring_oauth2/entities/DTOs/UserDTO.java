package spring.oauth2.keycloak.spring_oauth2.entities.DTOs;
import java.util.*;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;


@Value
@RequiredArgsConstructor
@Builder
public class UserDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;
}
