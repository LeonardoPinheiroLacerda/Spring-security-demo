package com.leonardo.springsecuritydemo.security.jwt.filters;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonardo.springsecuritydemo.dtos.CredentialsDTO;
import com.leonardo.springsecuritydemo.security.jwt.JwtConfig;
import com.leonardo.springsecuritydemo.security.jwt.TokenGenerator;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor

/**
 * Responsável por autenticar o usuário e senha dos usuários.
 */
public class UsernameAndPasswordAuthentication extends UsernamePasswordAuthenticationFilter{
    
    private final AuthenticationProvider authenticationProvider;
    private final JwtConfig jwtConfig;
    private final TokenGenerator tokenGenerator;
    private final SecretKey secretKey;

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {

            //Converte o body enviado pela requisição para um objeto do tipo AppUserCredentialsDTO
            CredentialsDTO authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), CredentialsDTO.class);

            //Cria um objeto de autenticação com o nome de usuário e senha enviados pelo usuário
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword());

            //Checa se os dados são válidos
            Authentication authenticate = authenticationProvider.authenticate(authentication);

            return authenticate;

        } catch (IOException e) {
            throw new AuthenticationCredentialsNotFoundException("As credênciais não estão em um formato válido.", e);
        }

    }

    //Executado se a chamada do método attemptAuthentication receber um resultado positivo
    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response, 
        FilterChain chain,
        Authentication authResult) throws IOException, ServletException {
        
        //Cria o token
        String token = tokenGenerator.generate(authResult, jwtConfig, secretKey);
        
        //Anexa o token nos headers da resposta
        response.addHeader(JwtConfig.AUTHORIZATION_HEADER, token);
    }


    //Executado se a chamada do método attemptAuthentication receber um resultado negativo
    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {

        //Altera o status da resposta para 401
        response.setStatus(401);
    }

}
