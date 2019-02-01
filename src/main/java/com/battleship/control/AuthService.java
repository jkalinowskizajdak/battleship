package com.battleship.control;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;


public class AuthService {

    public static final String SET_AUTH_HEADER = "Set-Auth-Token";
    public static final String AUTH_HEADER = "Auth-Token";
    private static final String DOMAIN_BATTLESHIP = "battleship";
    private static final Config config = new Config();
    private static AuthService instance;

    private AuthService() {

    }

    public static AuthService getInstance() {
        if (instance == null) {
            synchronized (AuthService.class) {
                if (instance == null) {
                    instance = new AuthService();
                }
            }
        }
        return instance;
    }

    public String validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(config.getSecretProperty()))
                .withIssuer(DOMAIN_BATTLESHIP)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    public String generateTokenForPlayer(String playerId) {
        String token = JWT.create()
                .withIssuer(DOMAIN_BATTLESHIP)
                .withSubject(playerId)
                .sign(Algorithm.HMAC256(config.getSecretProperty()));
        return token;
    }

}
