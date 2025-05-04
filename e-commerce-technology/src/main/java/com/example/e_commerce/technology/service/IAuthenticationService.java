package com.example.e_commerce.technology.service;

import com.example.e_commerce.technology.model.request.AuthenticationRequest;
import com.example.e_commerce.technology.model.request.IntrospectRequest;
import com.example.e_commerce.technology.model.request.LogoutRequest;
import com.example.e_commerce.technology.model.request.RefreshTokenRequest;
import com.example.e_commerce.technology.model.response.AuthenticationResponse;
import com.example.e_commerce.technology.model.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;


import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
