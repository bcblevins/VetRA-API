package com.bcb.vetra;

import com.bcb.vetra.services.vmsintegration.ezyvet.AuthRequestBody;
import com.bcb.vetra.services.vmsintegration.ezyvet.EzyVetIntegration;
import com.bcb.vetra.services.vmsintegration.ezyvet.Token;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Main class for the VetRA application.
 */
@SpringBootApplication
public class VetraApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(VetraApplication.class, args);

        //EzyVetIntegration ezyVetIntegration = context.getBean(EzyVetIntegration.class);
        //ezyVetIntegration.updateDB();

    }

    public static void testApiCall() {
        String apiAuthUrl = "https://api.trial.ezyvet.com/v1/oauth/access_token";
        AuthRequestBody authRequestBody = new AuthRequestBody(
                System.getenv("PARTNER_ID"),
                System.getenv("CLIENT_ID"),
                System.getenv("CLIENT_SECRET"),
                System.getenv("GRANT_TYPE"),
                System.getenv("SCOPE")
        );


		WebClient.Builder builder = WebClient.builder();
		Token token = builder.build()
				.post()
				.uri(apiAuthUrl)
				.bodyValue(authRequestBody)
				.retrieve()
				.bodyToMono(Token.class)
				.block();

        System.out.println(token.toString());

    }
}
