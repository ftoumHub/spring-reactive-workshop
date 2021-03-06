package ch.open.temperaturedashboard;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Controller
public class MeasurementController {

    private final WebClient webClient;

    private long count;

    public MeasurementController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/")
    public String getMeasurementsView() {
        return "measurements";
    }

    @GetMapping(value = "/measurements/feed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<Measurement> measurementsStream() {
        count++;
        return webClient
                .get()
                .uri("/measurements")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(Measurement.class)
                .log("feed-"+count);
    }



}
