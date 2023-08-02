package tw.niq.example.config;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientInterceptor implements ClientHttpRequestInterceptor {
	
	private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
	
	private final Authentication authentication;
	
	private final ClientRegistration clientRegistration;

	public OAuthClientInterceptor(
			OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
			ClientRegistrationRepository clientRegistrationRepository) {
		this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
		this.authentication = createPrincipal();
		this.clientRegistration = clientRegistrationRepository.findByRegistrationId("oidcclient");
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		
		OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId(clientRegistration.getRegistrationId())
				.principal(createPrincipal())
				.build();
		
		OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientManager.authorize(oAuth2AuthorizeRequest);
		
		if (isNull(oAuth2AuthorizedClient)) {
			throw new IllegalStateException("Missing credentials");
		}
		
		request.getHeaders().add(
				HttpHeaders.AUTHORIZATION, 
				"Bearer " + oAuth2AuthorizedClient.getAccessToken().getTokenValue());

		return execution.execute(request, body);
	}
	
	private Authentication createPrincipal() {
		return new Authentication() {

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return clientRegistration.getClientId();
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				// TODO Auto-generated method stub
				return Collections.emptySet();
			}

			@Override
			public Object getCredentials() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getDetails() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isAuthenticated() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

}
