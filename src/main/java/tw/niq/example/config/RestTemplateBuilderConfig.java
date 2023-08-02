package tw.niq.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {
	
	@Value("${rest.template.rootUrl}")
	String rootUrl;
	
	@Value("${rest.template.username}")
	String username;
	
	@Value("${rest.template.password}")
	String password;

	@Bean
	OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
		
		var oAuth2AuthorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials()
				.build();
		
		var oAuth2AuthorizedClientManager = 
				new AuthorizedClientServiceOAuth2AuthorizedClientManager(
						clientRegistrationRepository, 
						oAuth2AuthorizedClientService);
		
		oAuth2AuthorizedClientManager.setAuthorizedClientProvider(oAuth2AuthorizedClientProvider);
		
		return oAuth2AuthorizedClientManager;
	}
	
	@Bean
	RestTemplateBuilder restTemplateBuilder(
			RestTemplateBuilderConfigurer restTemplateBuilderConfigurer, 
			OAuthClientInterceptor oAuthClientInterceptor) {
		
		assert rootUrl != null;

		return restTemplateBuilderConfigurer.configure(new RestTemplateBuilder())
//				.basicAuthentication(username, password)
				.additionalInterceptors(oAuthClientInterceptor)
				.uriTemplateHandler(new DefaultUriBuilderFactory(rootUrl));
	}

}
