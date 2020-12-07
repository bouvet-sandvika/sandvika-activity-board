package no.bouvet.sandvika;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.bouvet.sandvika.activityboard.domain.Athlete;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MongoDbTokenStore implements OAuth2AuthorizedClientService {
    private final Map<String, OAuth2AuthorizedClient> authorizedClients = new ConcurrentHashMap<>();
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AthleteRepository athleteRepository;
    private final ObjectMapper objectMapper;

    public MongoDbTokenStore(ClientRegistrationRepository clientRegistrationRepository, AthleteRepository athleteRepository, ObjectMapper objectMapper) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.athleteRepository = athleteRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");

        ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (registration == null) {
            return null;
        }
        return (T) this.authorizedClients.get(this.getIdentifier(registration, principalName));
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        Assert.notNull(authorizedClient, "authorizedClient cannot be null");
        Assert.notNull(principal, "principal cannot be null");

        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;

            Athlete athlete = objectMapper.convertValue(oAuth2AuthenticationToken.getPrincipal().getAttributes(), Athlete.class);

            if (athleteRepository.existsById(athlete.getId())) {
                athlete = athleteRepository.findById(athlete.getId());
            }

            athlete.setToken(authorizedClient.getAccessToken().getTokenValue());
            athlete.setTokenExpires(authorizedClient.getAccessToken().getExpiresAt());
            athlete.setRefreshToken(authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null);

            athleteRepository.save(athlete);
        }

        this.authorizedClients.put(this.getIdentifier(
                authorizedClient.getClientRegistration(), principal.getName()), authorizedClient);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");

        ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (registration != null) {
            this.authorizedClients.remove(this.getIdentifier(registration, principalName));
        }
    }

    private String getIdentifier(ClientRegistration registration, String principalName) {
        String identifier = "[" + registration.getRegistrationId() + "][" + principalName + "]";
        return Base64.getEncoder().encodeToString(identifier.getBytes());
    }
}
