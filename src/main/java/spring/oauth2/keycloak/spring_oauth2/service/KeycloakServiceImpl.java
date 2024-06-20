package spring.oauth2.keycloak.spring_oauth2.service;

import java.util.*;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import spring.oauth2.keycloak.spring_oauth2.entities.DTOs.UserDTO;
import spring.oauth2.keycloak.spring_oauth2.util.KeycloakProvider;


@Service
public class KeycloakServiceImpl implements IKeycloakService{

    private static final Logger log = LoggerFactory.getLogger(Slf4j.class);

    /**
     * Metodo para listar todos los usuarios de Keycloak
     * @return 
     */
    @Override
    public List<UserRepresentation> findAll() {
        return KeycloakProvider.getRealmResource()
                    .users()
                    .list();
    }


    /**
     * Metodo para buscar usuarios por nombre de usuario
     * @return
     */
    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                    .users()
                    .searchByUsername(username, true);
    }


    /**
     * Metodo para crear usuario en keycloak
     * @return String
     */
    @Override
    public String creatUser(UserDTO userDTO) {
        int status = 0;
        UsersResource usersResource = KeycloakProvider.getUserResource();//Datos del usuario de keycloak
        UserRepresentation userRepresentation = new UserRepresentation();//Representacion de usuario de keycloak
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response =  usersResource.create(userRepresentation);//ejecutamos el create de keycloak y Almacenamos el response 
        status = response.getStatus();//Obtenemos el codigo de estado del reponse

        /*
         * si se ha creado bien vamos a asignar la contraseña al usuario de keycloak
         */
        if(status == 201){//Si es 201 es que se ha creado correctamente, porque el codigo 201 es de CREATE
            String path = response.getLocation().getPath();
            System.out.println(path);
            String userId = path.substring(path.lastIndexOf("/")+1);//Obtenemos el id del path con substring

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();// Esto es la representacion de credenciales/contraseñas en keycloak
            credentialRepresentation.setTemporary(false);//Decimos que no es una contraseña temporal
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);//Asignamos el tipo de contraseña
            credentialRepresentation.setValue(userDTO.getPassword());

            usersResource.get(userId).resetPassword(credentialRepresentation);//Obtenemos el usuario mediante id de keycloak y reseteamos la contraseña y la sustituimos por la nueva


            /*
             * ahora vamos a asignar los roles al usuario de kaycloak
             */
            RealmResource realmResource = KeycloakProvider.getRealmResource();
            List<RoleRepresentation> roleRepresentations = new ArrayList<>();

            if(userDTO.getRoles() == null || userDTO.getRoles().isEmpty()){
                roleRepresentations = List.of(realmResource.roles().get("user").toRepresentation());
            }else{
                roleRepresentations = realmResource.roles()
                            .list()
                            .stream()
                            .filter(role -> userDTO.getRoles()//creamos un filtro y obtenemos todos los roles del usuario 
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))//Si el roleName del usuario coincide con el roleName que hay en keycloak entonces lo obtenemos
                            .toList();
            }

            realmResource.users()//Aqui es donde asignamos los roles al usuario de keycloak
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(roleRepresentations);

            return "User created successfully!!";


        }else if(status == 409){//Si el codigo es 409 es de CONFLICT y lo mas probable es que el usuario ya exista
            log.error("User alredy exist!!");
            return "User alredy exist!!";
        }else{//Si el error es cualquier que no sea los anteriores posiblemente sea error de servidor
            log.error("Error create user!!");
            return "Error create user, please contact with the adminitrator!!"; 
        }
    }


    /**
     * Método para eliminar usuario por id
     * @param userId
     */
    @Override
    public void deleteUser(String userId) {
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.update(userRepresentation);
    }

}
