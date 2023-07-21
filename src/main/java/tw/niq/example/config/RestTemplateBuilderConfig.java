package tw.niq.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer restTemplateBuilderConfigurer) {
		
		assert rootUrl != null;

		return restTemplateBuilderConfigurer.configure(new RestTemplateBuilder())
				.basicAuthentication(username, password)
				.uriTemplateHandler(new DefaultUriBuilderFactory(rootUrl));
	}

}
