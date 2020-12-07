package no.bouvet.sandvika.activityboard.utils;

import no.bouvet.sandvika.activityboard.domain.Activity;
import no.bouvet.sandvika.activityboard.domain.weather.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class WeatherUtil {
    private static String BASE_URL = "https://api.darksky.net/forecast/47e94f7f05778e2791d856944c298954/";
    private static String OPTIONS = "?units=si&exclude=hourly,daily";

    @Autowired
    RestTemplate restTemplateService;

    public Weather getWeatherForActivity(Activity activity) {
        double[] startLatLong = activity.getStartLatLng();
        String url = BASE_URL + startLatLong[0] + "," + startLatLong[1] + "," + activity.getStartDateLocal().getTime()/1000L + OPTIONS;
        System.out.println("Getting weather with: " + url);
        Weather weather = restTemplateService.getForObject(url, Weather.class);
        return weather;

    }


}
