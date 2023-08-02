package tw.niq.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
					.anyRequest().authenticated())
//			.httpBasic(Customizer.withDefaults())
			.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()))
			.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
		
		return http.build();
	}
	
}
