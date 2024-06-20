package spring.oauth2.keycloak.spring_oauth2.util;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;


public class KeycloakProvider {

    private static final String SERVER_URL= "http://localhost:9090";
    private static final String REAL_NAME= "spring-boot-realm-dev";//Don't confuse this with client
    private static final String REALM_MASTER= "master";
    private static final String ADMIN_CLI="admin-cli";
    private static final String USER_CONSOLE="admin";
    private static final String USER_PASSWORD="admin";
    private static final String CLIENT_SECRET="fvj2I7EXOn48TpxuFgxkzhn0NkJ6vFw1";

    public static RealmResource getRealmResource(){
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(USER_PASSWORD)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                   .connectionPoolSize(10)
                   .build())
                .build();

        return keycloak.realm(REAL_NAME);
    }

    public static UsersResource getUserResource(){
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }

}
