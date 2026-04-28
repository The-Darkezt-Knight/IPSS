package deviate.capstone.ipss.app.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import deviate.capstone.ipss.auth.entity.User;
import deviate.capstone.ipss.auth.repository.UserRepository;
import deviate.capstone.ipss.auth.service.JwtService;
import deviate.capstone.ipss.shared.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.jwtService = Objects.requireNonNull(jwtService);
    }

    /*
        * before the request reaches any controller. It validates the token, extracts the user's
 * identity, and registers the authentication into the {@link SecurityContextHolder} so
 * that Spring Security can enforce role-based authorization downstream.
 *
 * <h3>Filter Flow:</h3>
 * <pre>
 *  Incoming Request
 *       │
 *       ▼
 *  [1] Check Authorization header
 *       │
 *       ├── Missing or not "Bearer " → pass through unauthenticated
 *       │
 *       ▼
 *  [2] Validate JWT token
 *       │
 *       ├── Invalid → 401 Unauthorized ("Invalid token")
 *       │
 *       ▼
 *  [3] Extract email from token
 *       │
 *       ▼
 *  [4] Check if SecurityContext already has authentication
 *       │
 *       ├── Already authenticated → skip to [6]
 *       │
 *       ▼
 *  [5] Load user from DB, verify account is active
 *       │
 *       ├── User not found  → UsernameNotFoundException
 *       ├── Account inactive → 401 Unauthorized ("Account inactive")
 *       │
 *       ▼
 *      Build authorities list → ["ROLE_<user_role>"]
 *      Build UsernamePasswordAuthenticationToken (principal, null, authorities)
 *      Attach request details (IP, session) via WebAuthenticationDetailsSource
 *      Set authentication into SecurityContextHolder
 *       │
 *       ▼
 *  [6] filterChain.doFilter() → proceed to next filter / controller
    */

    
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {


        // [1] Retrieve the Authorization header from the incoming request.
        //     If absent or not prefixed with "Bearer ", skip authentication
        //     and pass the request down the filter chain as unauthenticated.
        String header = request.getHeader("AUTHORIZATION");
        if(header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // [2] Strip the "Bearer " prefix (7 characters) to isolate the raw JWT.
        //     Validate the token's signature and expiration via JwtService.
        //     Respond with 401 immediately if the token is invalid or expired.
        String token = header.substring(7);
        if(!jwtService.isTokenValid(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        // [3] Extract the subject (email) embedded in the JWT claims.
        //     This will be used as the principal to identify the user.
        String email = jwtService.extractEmail(token);


        // [4] Only proceed with authentication if the SecurityContext does not
        //     already hold an Authentication object. This prevents redundant
        //     DB lookups if the request was already authenticated earlier in the chain.
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // [5a] Load the user from the database using the extracted email.
            //      Throws UsernameNotFoundException if no matching user is found.
            User user = userRepository.findByGovtEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("USER", email));

            // [5b] Reject the request if the user's account has been deactivated,
            //      even if the JWT itself is still technically valid.
            if(!user.isActive()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Account unauthorized");
                return;
            }
            
            // [5c] Build the granted authorities list from the user's assigned role.
            //      Spring Security requires the "ROLE_" prefix for hasRole() checks.
            var authority = List.of(new SimpleGrantedAuthority(user.getRole().name()));
            
            // [5d] Construct an authenticated token using the three-argument constructor,
            //      which marks isAuthenticated() as true. Credentials are null since
            //      the JWT has already proven the user's identity.
            var auth = new UsernamePasswordAuthenticationToken(email, null, authority);
            
            // [5e] Attach extra request metadata (client IP address, session ID)
            //      to the token for auditing and security monitoring purposes.
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // [5f] Register the authentication into the SecurityContextHolder.
            //      From this point, Spring Security treats this request as authenticated
            //      and will enforce @PreAuthorize / hasRole() rules accordingly.
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // [6] Authentication is complete. Pass the request to the next filter
        //     or ultimately to the target controller.
        filterChain.doFilter(request, response);
    }
}
