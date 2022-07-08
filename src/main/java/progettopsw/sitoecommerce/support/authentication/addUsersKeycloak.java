package progettopsw.sitoecommerce.support.authentication;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;


public class addUsersKeycloak {
    static String email;
    String lastName;
    static String password;

    public addUsersKeycloak(String email, String password, String lastName){
        this.email = email;
        this.password = password;
        this.lastName = lastName;
    }

    public static void main(String[] args) {
        String usernameAdmin = "salvatore";
        String passwordAdmin = "piattaforme2022";
        String clientName = "store-flutter";
        String role = "admin";
        String serverUrl = "http://localhost:8080";
        String realm = "store";
        String clientId = clientName;
        String clientSecret = "";

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(usernameAdmin)
                .password(passwordAdmin)
                .build();

            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(email);
            user.setEmail(email);

            user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

            // Get realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersRessource = realmResource.users();

            // Create user (requires manage-users role)
            Response response = usersRessource.create(user);
            System.out.printf("Response: %s %s%n", response.getStatus(), response.getStatusInfo());
            System.out.println(response.getLocation());
            String userId = CreatedResponseUtil.getCreatedId(response);
            System.out.printf("User created with userId: %s%n", userId);

            // Define password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(true);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password);

            UserResource userResource = usersRessource.get(userId);

            // Set password credential
            userResource.resetPassword(passwordCred);

            // Get client
            ClientRepresentation app1Client = realmResource.clients().findByClientId(clientName).get(0);

            // Get client level role (requires view-clients role)
            RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()).roles().get(role).toRepresentation();

            // Assign client level role to user
            userResource.roles().clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));
    }
}//addUsersKeycloak