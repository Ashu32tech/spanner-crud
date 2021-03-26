package com.infogain.gcp.poc.component;

import com.infogain.gcp.poc.model.PNRModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RestClientUtil {

    /*@Autowired
    private WebClient webClient;

    public void callPostAPI(PNRModel resultPNRModel, String endpoint) {
        log.info("Calling publish API");
        Mono<String> resultMono = webClient.post().uri(endpoint).body(Mono.just(resultPNRModel), PNRModel.class).retrieve().bodyToMono(String.class);
        resultMono.subscribe(System.out::println);
        log.info("Called publish API");
    }

    public void callGetAPI(String endpoint){
        Mono<String> resultMono = webClient.get().uri("endpoint")
                .retrieve().bodyToMono(String.class);
        resultMono.subscribe(System.out::println);
    }*/

}