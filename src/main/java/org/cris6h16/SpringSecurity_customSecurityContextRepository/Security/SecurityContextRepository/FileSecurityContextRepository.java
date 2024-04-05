package org.cris6h16.SpringSecurity_customSecurityContextRepository.Security.SecurityContextRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
is only an example by me, cris6h16.
this should be for Horizontal Scaling:
    - Storing the security context in a cache or database enables horizontal scaling of your application.
 */

public class FileSecurityContextRepository implements SecurityContextRepository {
    private final Path filePath = Paths.get("security-context.txt");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        try {
            if (Files.exists(filePath)) {
                String contextStr = Files.readString(filePath);
                return deserializeSecurityContext(contextStr);
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        return SecurityContextHolder.createEmptyContext();
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        try {
            String contextStr = serializeSecurityContext(context);
            Files.writeString(filePath, contextStr);
        } catch (IOException e) {
            System.out.printf("Failed to save the security context to file %s%n", filePath.toAbsolutePath());
            e.printStackTrace(); 
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return Files.exists(filePath);
    }

    private SecurityContext deserializeSecurityContext(String contextStr) throws IOException {
        return objectMapper.readValue(contextStr, SecurityContext.class);
    }

    private String serializeSecurityContext(SecurityContext context) throws JsonProcessingException {
        return objectMapper.writeValueAsString(context);
    }
}
