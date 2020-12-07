package no.bouvet.sandvika;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.bouvet.sandvika.activityboard.domain.Athlete;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoRestController {

    private final AthleteRepository athleteRepository;
    private final ObjectMapper objectMapper;

    public UserInfoRestController(AthleteRepository athleteRepository, ObjectMapper objectMapper) {
        this.athleteRepository = athleteRepository;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/user-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public Athlete getUserInfo(Authentication authentication) {
        Athlete athlete = objectMapper.convertValue(((DefaultOAuth2User) authentication.getPrincipal()).getAttributes(), Athlete.class);

        return athleteRepository.findById(athlete.getId());
    }
}
