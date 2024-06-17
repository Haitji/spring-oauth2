package spring.oauth2.keycloak.spring_oauth2.service;

import org.keycloak.representations.idm.UserRepresentation;

import spring.oauth2.keycloak.spring_oauth2.entities.DTOs.UserDTO;

import java.util.List;

public interface IKeycloakService {

    List<UserRepresentation> findAll();
    List<UserRepresentation> searchUserByUsername(String username);
    String creatUser(UserDTO userDTO);
    void deleteUser(String userId);
    void updateUser(String userId, UserDTO userDTO);
}
