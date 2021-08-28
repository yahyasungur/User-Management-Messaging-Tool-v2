package com.yahya.springwebservice;

import io.spring.guides.gs_spring_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import java.util.Objects;

@Endpoint
public class WebServiceEndpoint {
    //islogin ve login büyük ihtimal çalışmayacak çünkü hepsi için ayrı bir client handler oluşturmuyorum.
    private boolean isLogin = false;
    private boolean adminLogin = false;
    private String login = null;

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-spring-web-service";

    private UserRepository userRepository;
    private MessageRepository messageRepository;

    @Autowired
    public WebServiceEndpoint(UserRepository userRepository, MessageRepository messageRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "loginRequest")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest request){
        LoginResponse response = new LoginResponse();
        if (userRepository.checkUser(request.getEmail(),request.getPassword())){
            response.setResponse("You have successfully logged in.");
            isLogin = true;
            login = request.getEmail();
        }else{
            response.setResponse("Incorrect email or password.");
        }
        return response;
    }


    //logoff operation 'ı gerceklestir.
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "logoffRequest")
    @ResponsePayload
    public LogoffResponse logoff(@RequestPayload LogoffRequest request){
        LogoffResponse response = new LogoffResponse();
        response.setResponse("You have successfully logged out.");
        this.isLogin = false;
        this.adminLogin = false;
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "messageRequest")
    @ResponsePayload
    public MessageResponse message(@RequestPayload MessageRequest request){
        MessageResponse response = new MessageResponse();
        if (isLogin){
            if (userRepository.isThereAnyUser(request.getMessage().getTo())){
                messageRepository.addMessage(login,request.getMessage().getTo(),request.getMessage().getBody());
                response.setResponse("Sent.");
            }else{
                response.setResponse("There is no such user with this e-mail.");
            }

        }else{
            response.setResponse("You cannot send a message without logging in. Please login first.");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "inboxRequest")
    @ResponsePayload
    public InboxResponse inbox(@RequestPayload InboxRequest request){
        InboxResponse response = new InboxResponse();
        if (isLogin){
            //verilen çıktıyı '|' lardan ayır öyle yazdır.
            response.setResponse(messageRepository.read_inbox(login));
        }else{
            response.setResponse("You cannot see the inbox without logging in. Please login first.");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "outboxRequest")
    @ResponsePayload
    public OutboxResponse outbox(@RequestPayload OutboxRequest request){
        OutboxResponse response = new OutboxResponse();
        if (isLogin){
            //verilen çıktıyı '|' lardan ayır öyle yazdır.
            response.setResponse(messageRepository.read_outbox(login));
        }else{
            response.setResponse("You cannot see the outbox without logging in. Please login first.");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "adminLoginRequest")
    @ResponsePayload
    public AdminLoginResponse adminLogin(@RequestPayload AdminLoginRequest request){
        AdminLoginResponse response = new AdminLoginResponse();
        if (userRepository.checkAdmin(request.getEmail(),request.getPassword())){
            response.setResponse("You have successfully logged in as --ADMIN--.");
            this.adminLogin = true;
            this.isLogin = true;
            login = request.getEmail();
        }else{
            response.setResponse("Incorrect email or password.");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createRequest")
    @ResponsePayload
    public CreateResponse create(@RequestPayload CreateRequest request){
        CreateResponse response = new CreateResponse();
        if (this.adminLogin){
            if (!messageRepository.isThereAnyMessageContainsMail(request.getUser().getEmail())){
                userRepository.addUser(request.getUser());
                response.setResponse("Created.");
            } else{
                response.setResponse("This e-mail address is in use, please try another one.");
            }
        }else{
            response.setResponse("This command requires admin privileges, please login as --ADMIN--");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "readRequest")
    @ResponsePayload
    public ReadResponse read(@RequestPayload ReadRequest request){
        ReadResponse response = new ReadResponse();
        if (this.adminLogin){
            //verilen çıktıyı '|' lardan ayır öyle yazdır.
            response.setResponse(userRepository.readUsers());
        }else{
            response.setResponse("This command requires admin privileges, please login as --ADMIN--");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateRequest")
    @ResponsePayload
    public UpdateResponse update(@RequestPayload UpdateRequest request){
        UpdateResponse response = new UpdateResponse();
        if (this.adminLogin){
            if (userRepository.contains(request.getEmailOfUpdated())){
                if (!Objects.equals(request.getEmailOfUpdated(), request.getUser().getEmail())){
                    messageRepository.updateMessages(request.getEmailOfUpdated(),request.getUser().getEmail());
                }
                userRepository.updateUser(request.getEmailOfUpdated(),request.getUser());
                response.setResponse("Updated.");
            }else{
                response.setResponse("No such user found.");
            }
        }else{
            response.setResponse("This command requires admin privileges, please login as --ADMIN--");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteRequest")
    @ResponsePayload
    public DeleteResponse delete(@RequestPayload DeleteRequest request){
        DeleteResponse response = new DeleteResponse();
        if (this.adminLogin){
            if (userRepository.contains(request.getEmail())){
                userRepository.deleteUser(request.getEmail());
                response.setResponse("Deleted.");
            }else{
                response.setResponse("No such user found.");
            }
        }else{
            response.setResponse("This command requires admin privileges, please login as --ADMIN--");
        }
        return response;
    }
}
