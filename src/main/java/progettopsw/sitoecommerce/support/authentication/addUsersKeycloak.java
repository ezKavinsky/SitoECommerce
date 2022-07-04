package progettopsw.sitoecommerce.support.authentication;

/*import org.keycloak.OAuth2Constants;
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


public class main {


    public static void main(String[] args) {
        String usernameAdmin = "";
        String passwordAdmin = "";
        String clientName = "";
        String role = "";
        String[] email = {"email@email.it", "email@email.it"}
        String[] lastName = {"cai", "kddm"};
        String[] password = {"pw1", "pw2"};
        String serverUrl = "";
        String realm = "";
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

        for (int i = 0; i < email.length; i++) {
            // Define user
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(email[i]);
            user.setEmail(email[i]);

            user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

            // Get realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersRessource = realmResource.users();

            // Create user (requires manage-users role)
            Response response = usersRessource.create(user);
            System.out.printf("Repsonse: %s %s%n", response.getStatus(), response.getStatusInfo());
            System.out.println(response.getLocation());
            String userId = CreatedResponseUtil.getCreatedId(response);
            System.out.printf("User created with userId: %s%n", userId);

            // Define password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(true);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password[i]);

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
    }
}
*/