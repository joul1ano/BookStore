package com.bookstore.auth;

import com.bookstore.config.JwtService;
import com.bookstore.enums.Role;
import com.bookstore.exceptions.AuthException;
import com.bookstore.exceptions.UsernameAlreadyExistsException;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.ShoppingCartItem;
import com.bookstore.model.User;
import com.bookstore.repository.ShoppingCartRepository;
import com.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws UsernameAlreadyExistsException {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new UsernameAlreadyExistsException();

        var user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phoneNumber(request.getPhone())
                .role(Role.USER)
                .totalAmountSpent(0)
                .createdAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        var cart = ShoppingCart.builder().user(user).itemCount(0).totalCost(0).lastUpdatedAt(LocalDateTime.now()).build();
        cartRepository.save(cart);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword())
            );
        }catch(UsernameNotFoundException e){
            throw new AuthException("Username not found"); //??? axreiasto
        }catch (BadCredentialsException e){
            throw new AuthException(e.getMessage());
        }

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthException("Username does not exist"));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
