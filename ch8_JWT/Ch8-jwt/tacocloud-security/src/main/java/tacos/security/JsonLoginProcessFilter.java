package tacos.security;

import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Order()
public class JsonLoginProcessFilter extends AbstractAuthenticationProcessingFilter {

    private static final String CONTENT_TYPE = "application/json";
    private static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    private static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private static final String DEFAULT_FILTER_PROCESSES_URL = "/login";
    private final ObjectMapper objectMapper;

    public JsonLoginProcessFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        super(DEFAULT_FILTER_PROCESSES_URL, authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        
    	log.info("+attemptAuthentication");
    	if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        Map<String, String> parameter = objectMapper.readValue(request.getInputStream(), Map.class);
        String username = parameter.get(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = parameter.get(SPRING_SECURITY_FORM_PASSWORD_KEY);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        
        ///////////////////////////////////////////////////////////////////////////////////
        Authentication auth = this.getAuthenticationManager().authenticate(authRequest);
        ///////////////////////////////////////////////////////////////////////////
        
        log.info("-attemptAuthentication");
        return auth;
        
        /////////////////////////////////////////////////////////////////
        //return this.getAuthenticationManager().authenticate(authRequest);
        //////////////////////////////////////////////////////////////////
    }
}