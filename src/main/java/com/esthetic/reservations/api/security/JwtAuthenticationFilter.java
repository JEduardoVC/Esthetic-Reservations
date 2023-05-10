package com.esthetic.reservations.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.exception.UnauthorizedException;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.RoleRepository;
import com.esthetic.reservations.api.service.impl.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = getJwtFromRequest(request);

        String username = null;
        try {
            username = this.jwtUtil.extractUsername(jwtToken);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new UnauthorizedException("Usuario", "token expirado", "token", jwtToken);
        } catch (Exception exception) {
            throw new EstheticAppException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            // TODO: lanzar excepcion
        }

        UserEntity userDetails = (UserEntity) this.userDetailsService.loadUserByUsername(username);

        String jwtRoles = (String) this.jwtUtil.extractClaim(jwtToken, "roles");

        jwtRoles = jwtRoles.replace("[", "").replace("]", "");
        String[] roles = jwtRoles.split(",");
        for (String roleName : roles) {
            userDetails.addRole(this.roleRepository.findByName(roleName).get());
        }

        if (!this.jwtUtil.validateToken(jwtToken, userDetails)) {
            throw new UnauthorizedException("Usuario", "token no válido", "token", jwtToken);
        }

        UsernamePasswordAuthenticationToken uPasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        uPasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(uPasswordAuthenticationToken);

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean notfilter = antPathMatcher.match("/api/auth/**", request.getServletPath())
        		|| antPathMatcher.match("/api/mail/reestablecer", request.getServletPath())
                || antPathMatcher.match("/app/**", request.getServletPath())
                || antPathMatcher.match("/css/**", request.getServletPath())
                || antPathMatcher.match("/img/**", request.getServletPath())
                || antPathMatcher.match("/js/**", request.getServletPath())
                || antPathMatcher.match("/Inventario/**", request.getServletPath())
                || antPathMatcher.match("/", request.getServletPath());
        return notfilter;
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader == null || !StringUtils.hasText(requestTokenHeader)
                || !requestTokenHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Usuario", "bearer token no encontrado", "token", "");
        }

        // Remove "Bearer "
        return requestTokenHeader.substring(7);

    }

}
