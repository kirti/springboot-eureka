package com.appsdeveloperblog.api.gateway;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory< AuthorizationHeaderFilter.Config>{
	
	@Autowired 
	Environment environment;
	
	public AuthorizationHeaderFilter() {
		super(Config.class);
	}
	
	
	public static class Config{
	    // Put configuration properties here 
	}

	@Override
	public GatewayFilter apply(Config config) {
		// TODO Auto-generated method stub
		return (exchange, chain)->{
			ServerHttpRequest request= exchange.getRequest();
			
			if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				
				return onError(exchange,"No authorization header",HttpStatus.UNAUTHORIZED);
			}
			
			String authorizationHeader =request.getHeaders().get("Authorization").get(0);
			String jwt = authorizationHeader.replace("Bearer","");
			
			if(!isJwtvalid(jwt)) {
				return onError(exchange,"JWT Token is not valid",HttpStatus.UNAUTHORIZED);
			}
			
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String string, HttpStatus httpStatus) {
		// TODO Auto-generated method stub
		ServerHttpResponse response= exchange.getResponse();
		response.setStatusCode(httpStatus);
		
		return response.setComplete();
		
	}
	
	private boolean isJwtvalid(String jwt) {
		
		boolean isValid= true;
		
		String subject = null;
		
		//SecretKey key = Jwts.SIG.HS256.key().build();
		
		//JwtParser jwtParser = Jwts.parser().verifyWith(key).build();
	
				 
		try {
			
			
			subject = getUserNameFromJwtToken(jwt.trim());
		
		}catch(SignatureException error) {
			  System.out.println("Locally computed signature: " + error.getLocalizedMessage());
			  
			
			isValid = false ;
		}
		
		

		if (subject == null || subject.isEmpty()) {
			isValid = false;
		}
		
	
		
		return isValid;
	}
	
	 private SecretKey getSigningKey() {
		   String key  = "hfgry463hf746hf573ydh475fhy5739hfgry463hf746hf573ydh475fhy5739hfgry463hf746hf573ydh475fhy5739";
	        byte[] keyBytes = Decoders.BASE64.decode(key);
	        return Keys.hmacShaKeyFor(keyBytes);
	 }
	
	 public String getUserNameFromJwtToken(String token) {
	        return Jwts.parser()
	            .verifyWith(getSigningKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload()
	            .getSubject();

	    }
	

	
}
