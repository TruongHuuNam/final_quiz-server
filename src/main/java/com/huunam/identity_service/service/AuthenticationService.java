package com.huunam.identity_service.service;

import com.huunam.identity_service.dto.request.AuthenticationRequest;
import com.huunam.identity_service.dto.request.IntrospectRequest;
import com.huunam.identity_service.dto.request.LogoutRequest;
import com.huunam.identity_service.dto.request.RefreshRequest;
import com.huunam.identity_service.dto.response.AuthenticationResponse;
import com.huunam.identity_service.dto.response.IntrospectResponse;
import com.huunam.identity_service.entity.InvalidatedToken;
import com.huunam.identity_service.entity.User;
import com.huunam.identity_service.exception.AppException;
import com.huunam.identity_service.exception.ErrorCode;
import com.huunam.identity_service.repository.InvalidatedTokenRepository;
import com.huunam.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;

    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal //khong inject sign key vao constructor
    @Value("${jwt.signerKey}") //get signerKey from yaml file
    protected String SIGNER_KEY; //get value from @Value annotation

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        }
        catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)  //return true and verified and expire time
                .build(); //use builder pattern

    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)); //find user

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); //hash password
        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword()); //compare password user input with password store in DB has hashed
        if(!authenticated)
            throw new AppException(ErrorCode.USER_UNAUTHENTICATED); //throw errorCode

        var token = generateToken(user); //call generate token function

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request ) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jti = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // -kiểm tra hiệu lực token
        var signedJwt = verifyToken(request.getToken());
        // Get jwt id and expiry time in ClaimsSet
        var jti = signedJwt.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJwt.getJWTClaimsSet().getExpirationTime();
        // build token
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        var username = signedJwt.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_UNAUTHENTICATED));

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes()); //get signerKey
        SignedJWT signedJWT = SignedJWT.parse(token);  //convert into token
        //check if token expired
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        //verify token
        var verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date()))){  //check if not verified
            throw new AppException(ErrorCode.USER_UNAUTHENTICATED);
        }

        if(invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.USER_UNAUTHENTICATED);
        return signedJWT;
    }

    //define generateToken function
    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);//use algorithm HS512
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder() //set claimsSet
                //define claim basic and qualified (tieu chuan)
                .subject(user.getUsername())
                .issuer("huunam09")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() // expired in 1 hour
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); //set payload and change to Json object
        JWSObject jwsObject = new JWSObject(header,payload);//needs 2 params are header and payload

        //Sign token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); //need a secret key 32 bytes string
            return jwsObject.serialize(); //change to byte then send to sever
        } catch (JOSEException e) {
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }
    }
    private String buildScope(User user){   //Build scope tu 1 user (Scope la list user)
        StringJoiner stringJoiner = new StringJoiner(" "); //quy uoc(convention) phan cach nhau boi mot dau cach
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getName()); //add prefix la ROLE_
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

}
