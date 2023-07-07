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
	
	@Bean
	RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer restTemplateBuilderConfigurer) {
		
		assert rootUrl != null;
		
		RestTemplateBuilder restTemplateBuilder = restTemplateBuilderConfigurer.configure(new RestTemplateBuilder());
		
		DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory(rootUrl);
		
		return restTemplateBuilder.uriTemplateHandler(defaultUriBuilderFactory);
	}

}
