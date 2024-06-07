package com.retail.e_shopify.jwt;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.retail.e_shopify.repository.AccessTokenRepository;
import com.retail.e_shopify.repository.RefreshTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private AccessTokenRepository accessTokenRepository;
	private RefreshTokenRepository refreshTokenRepository;
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String at = null;
		String rt = null;

		Cookie[] cookies = request.getCookies();
		if(cookies!= null) {
			for (Cookie cookie : cookies) {

				if (cookie.getName().equals("rt"))
					rt = cookie.getValue();
				else if (cookie.getName().equals("at"))
					at = cookie.getValue();
			}
		}
		if (at != null && rt != null) {
			if (accessTokenRepository.existsByTokenAndIsBlocked(at, true)
					&& refreshTokenRepository.existsByTokenAndIsBlocked(rt, true))
				throw new RuntimeException();

			String userName = jwtService.getUserName(at);
			String role = jwtService.getRole(at);

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, null,
						Collections.singleton(new SimpleGrantedAuthority(role)));
				token.setDetails(new WebAuthenticationDetails(request));

				SecurityContextHolder.getContext().setAuthentication(token);
			}

		}
		filterChain.doFilter(request, response);
	}
}
