package com.example.javaclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.javaclient.wsdl.*;

import java.util.Objects;

@SpringBootApplication
public class JavaClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaClientApplication.class, args);
	}

	@Bean
	CommandLineRunner lookup(JavaClient quoteClient){
		return args -> {
			String email;
			String password;
			String adminEmail;

			if (args.length == 3 && Objects.equals(args[0], "login")) {
				email = args[1];
				password = args[2];
				LoginResponse response = quoteClient.login(email,password);
				System.err.println(response.getResponse());
				return;
			}
			else if (args.length == 3 && Objects.equals(args[0], "adminlogin")){
				adminEmail = args[1];
				password = args[2];
				AdminLoginResponse response = quoteClient.adminLogin(adminEmail,password);
				System.err.println(response.getResponse());
				return;
			}
			else if (args.length == 7 && Objects.equals(args[0],"create")){
				User user = new User();
				user.setName(args[1]);
				user.setSurname(args[2]);
				user.setBirthdate(args[3]);
				user.setGender(args[4]);
				user.setEmail(args[5]);
				user.setPassword(args[6]);

				CreateResponse response = quoteClient.create(user);
				System.err.println(response.getResponse());
				return;
			}
			else if (args.length > 3 && Objects.equals(args[0],"msg")){
				Message message = new Message();
				message.setTo(args[1]);
				String str = "";
				for (int i = 2;i < args.length ; i++){
					str += args[i];
				}
				message.setBody(str);

				MessageResponse response = quoteClient.message(message);
				System.err.println(response.getResponse());
				return;
			}
			System.err.println("There is no such command.");
		};
	}

}
