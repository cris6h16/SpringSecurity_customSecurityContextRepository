package org.cris6h16.SpringSecurity_customSecurityContextRepository.Security.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cris6h16.SpringSecurity_customSecurityContextRepository.Security.SecurityContextRepository.FileSecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PrincipalController {
    private SecurityContextRepository securityContextRepository = new FileSecurityContextRepository();
    private AuthenticationManager authenticationManager;

    @Autowired
    public PrincipalController(@Qualifier("myAuthenticationManager") AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    @ResponseBody
    public void login(@RequestBody LoginRequest loginRequest,
                      HttpServletRequest request,
                      HttpServletResponse response) {

        //...........................only an example, make sure decouple your code.....................................


        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.username(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.getContextHolderStrategy().setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    public record LoginRequest(String username, String password) {
    }
}
