package com.leonardo.springsecuritydemo.security.configs;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor

/**
 * Componente que irá requisitar o usuário e senha ao fazer uma 
 * tentativa de requisição sem credênciais ou com credênciais invalidas
 */
@Component
public class AppBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint{
    
	private final HttpBasicProperties httpBasicProperties;

    @Override
	public void afterPropertiesSet() {
        setRealmName(httpBasicProperties.getRealm());
		super.afterPropertiesSet();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
