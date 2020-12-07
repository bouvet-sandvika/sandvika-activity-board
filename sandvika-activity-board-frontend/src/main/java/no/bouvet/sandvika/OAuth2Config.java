package no.bouvet.sandvika;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
//@EnableGlobalMethodSecurityhodSecurity(jsr250Enabled = true)
public class OAuth2Config extends WebSecurityConfigurerAdapter {

    private final AthleteRepository athleteRepository;
    private final ObjectMapper objectMapper;

    public OAuth2Config(AthleteRepository athleteRepository, ObjectMapper objectMapper) {
        this.athleteRepository = athleteRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
//                .antMatchers("/**").permitAll()
//                .antMatchers(HttpMethod.PUT, "/club*").permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                .oauth2Login()
                    .tokenEndpoint()
                        .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                    .clientRegistrationRepository(clientRegistrationRepository());
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new MongoDbTokenStore(clientRegistrationRepository(), athleteRepository, objectMapper);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.stravaClientRegistration());
    }

    private ClientRegistration stravaClientRegistration() {
        return ClientRegistration.withRegistrationId("strava")
                .clientId("3034")
                .clientSecret("506d1d0ed30af56b74a458a26419dd6ead8e910d")
                .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("read,activity:read")
                .authorizationUri("https://www.strava.com/oauth/authorize?approval_prompt=auto")
                .tokenUri("https://www.strava.com/oauth/token")
                .userInfoUri("https://www.strava.com/api/v3/athlete")
                .clientName("Strava")
                .userNameAttributeName("id")
                .build();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(){
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), new CustomOAuth2AccessTokenResponseHttpMessageConverter()));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        accessTokenResponseClient.setRestOperations(restTemplate);

        return accessTokenResponseClient;
    }
}