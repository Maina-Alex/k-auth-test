package com.devfelix.authservice.service;

import com.devfelix.authservice.dto.AccessTokenWrapper;
import com.devfelix.authservice.dto.UniversalResponse;
import com.devfelix.authservice.exception.AuthenticationException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Duration;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenProviderService {
    private final KeyPair keyPair;
    public static final String USER_NAME = "user_name";
    private static final String AUTHORITIES = "authorities";
    private static final String CLIENT_ID = "client_id";
    private static final String JTI = "jti";
    private static final String ATI = "ati";
    private static final String SCOPE = "SCOPE";
    private static final long TOKEN_EXPIRY_TIME_SECONDS= 5*60;

    public AccessTokenWrapper generateToken(String username, long userId) throws JOSEException {
            Calendar calendar = Calendar.getInstance ();
            List<String> simpleGrantedAuthorities = Collections.singletonList ("ADMIN");
            Date now = calendar.getTime ();
            String jti = UUID.randomUUID ().toString ();
            Date expiryDate = new Date (now.getTime () + TOKEN_EXPIRY_TIME_SECONDS);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder ()
                    .claim (USER_NAME, username)
                    .claim("USER_ID",userId)
                    .claim (JTI, jti)
                    .claim (CLIENT_ID, "MOVIE_USER_CLIENT")
                    .claim (AUTHORITIES, simpleGrantedAuthorities)
                    .claim (SCOPE, "AUTH")
                    .audience ("MOVIE_USER")
                    .expirationTime (expiryDate)
                    .notBeforeTime (now)
                    .issueTime (now)
                    .build ();

            JWSHeader jwsHeader = new JWSHeader.Builder (JWSAlgorithm.RS256)
                    .type (JOSEObjectType.JWT)
                    .build ();

            SignedJWT signedJWT = new SignedJWT (jwsHeader, claimsSet);

            RSASSASigner signer = new RSASSASigner (keyPair.getPrivate());

            signedJWT.sign (signer);

            return AccessTokenWrapper.builder ()
                    .accessToken (signedJWT.serialize ())
                    .tokenType (JOSEObjectType.JWT.getType ())
                    .expiresIn (Duration.ofMillis (expiryDate.getTime () - new Date ().getTime ()).toSeconds ())
                    .jti (jti)
                    .message ("Logged in successfully")
                    .status (200)
                    .build ();

    }

    public long  validateToken(String token) throws Exception{
            SignedJWT signedJWT = SignedJWT.parse(token);
            RSASSAVerifier signer = new RSASSAVerifier ((RSAPublicKey)keyPair.getPublic());
            if(!signedJWT.verify(signer)){
                throw new AuthenticationException("Invalid authentication generated");
            }
            return signedJWT.getJWTClaimsSet().getLongClaim("USER_ID");

    }


}
