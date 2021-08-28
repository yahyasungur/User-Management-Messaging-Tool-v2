package com.example.javaclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.example.javaclient.wsdl.*;


public class JavaClient extends WebServiceGatewaySupport {
    private static final Logger log = LoggerFactory.getLogger(JavaClient.class);

    public LoginResponse login(String email, String password){
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        log.info("Requesting login for " + email);

        LoginResponse response = (LoginResponse) getWebServiceTemplate().marshalSendAndReceive("http://localhost:8080/ws/UMMT",
                request,new SoapActionCallback("http://spring.io/guides/gs-spring-web-service/LoginRequest"));

        return response;
    }

    public AdminLoginResponse adminLogin(String email, String password){
        AdminLoginRequest request = new AdminLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        log.info("Requesting login for " + email);

        AdminLoginResponse response = (AdminLoginResponse) getWebServiceTemplate().marshalSendAndReceive("http://localhost:8080/ws/UMMT",
                request,new SoapActionCallback("http://spring.io/guides/gs-spring-web-service/AdminLoginRequest"));

        return response;
    }

    public CreateResponse create(User user){
        CreateRequest request = new CreateRequest();
        request.setUser(user);

        log.info("Requesting create for " + user.getEmail());

        CreateResponse response = (CreateResponse) getWebServiceTemplate().marshalSendAndReceive("http://localhost:8080/ws/UMMT",
                request,new SoapActionCallback("http://spring.io/guides/gs-spring-web-service/CreateRequest"));

        return response;
    }

    public MessageResponse message(Message message){
        MessageRequest request = new MessageRequest();
        request.setMessage(message);

        log.info("Requesting message for " + message.getFrom());

        MessageResponse response = (MessageResponse) getWebServiceTemplate().marshalSendAndReceive("http://localhost:8080/ws/UMMT",
                request,new SoapActionCallback("http://spring.io/guides/gs-spring-web-service/MessageRequest"));

        return response;
    }
}
