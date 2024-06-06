package com.bcb.vetra;

import com.bcb.vetra.services.vmsintegration.ezyvet.AuthRequestBody;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Main class for the VetRA application.
 */
@SpringBootApplication
public class VetraApplication {

    public static void main(String[] args) {
        SpringApplication.run(VetraApplication.class, args);
        testApiCall();
    }

    public static void testApiCall() {
        String apiAuthUrl = "https://api.trial.ezyvet.com/v1/oauth/access_token";
        String authCredentials = System.getenv("CREDENTIALS");
        AuthRequestBody authRequestBody = new AuthRequestBody(
                System.getenv("PARTNER_ID"),
                System.getenv("CLIENT_ID"),
                System.getenv("CLIENT_SECRET"),
                System.getenv("GRANT_TYPE"),
                System.getenv("SCOPE")
        );


		WebClient.Builder builder = WebClient.builder();
		String token = builder.build()
				.post()
				.uri(apiAuthUrl)
				.bodyValue(authRequestBody)
				.retrieve()
				.bodyToMono(String.class)
				.block();

        System.out.println(token);

    }
}
