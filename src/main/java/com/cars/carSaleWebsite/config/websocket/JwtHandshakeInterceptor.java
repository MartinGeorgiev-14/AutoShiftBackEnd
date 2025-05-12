package com.cars.carSaleWebsite.config.websocket;

import com.cars.carSaleWebsite.config.security.JWTGenerator;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor
{
    private final JWTGenerator jwtGenerator;

    public JwtHandshakeInterceptor(JWTGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }



    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = getTokenFromRequest(request);

//        System.out.println("request: " + request.getHeaders());
//        System.out.println("response: " + response);
//        System.out.println("wsHandler: " + wsHandler);
//        System.out.println("attributes: "  + attributes);
//        System.out.println("token: "  + token);

        System.out.println("validation: " + jwtGenerator.validateToken(token));
        System.out.println("validation2: " + token != null);

        if(token != null && jwtGenerator.validateToken(token)){
            Authentication auth = jwtGenerator.getAuthentication(token);
            attributes.put("principal", token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Context: " + SecurityContextHolder.getContext());
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2 && pair[0].equals("token")) {
                    return pair[1];
                }
            }
        }
        return null;
    }

}
