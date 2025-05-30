package dev.abreu.bankapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
		info = @Info(
				contact = @Contact(
						name = "Devin Abreu",
						email = "devinabreu10@gmail.com",
						url = "https://www.linkedin.com/in/devin-abreu/"
				),
				description = "OpenApi documentation for Spring security",
				title = "OpenApi specification - RP0 Bank",
				version = "1.0",
				license = @License(
						name = "License name",
						url = "https://some-url.com"
				),
				termsOfService = "Terms of service"
		),
		servers = {
				@Server(
						description = "Local ENV",
						url = "http://localhost:8080"
				),
				@Server(
						description = "Local Docker ENV",
						url = "http://localhost:8088"
				),
				@Server(
						description = "Prod ENV",
						url = "https://rp0-bankapp-api-v1.onrender.com"
				)
		},
		security = {
				@SecurityRequirement(
						name = "bearerAuth"
				)
		}
)
@SecurityScheme(
		name = "bearerAuth",
		description = "JWT auth description",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig { } // http://localhost:8080/swagger-ui/index.html
