package com.javatechie.controller;

import com.javatechie.dto.AuthRequest;
import com.javatechie.dto.JwtResponse;
import com.javatechie.dto.Product;
import com.javatechie.dto.RefreshTokenRequest;
import com.javatechie.entity.RefreshToken;
import com.javatechie.entity.UserInfo;
import com.javatechie.service.JwtService;
import com.javatechie.service.ProductService;
import com.javatechie.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;

    public ProductController(ProductService service, JwtService jwtService, RefreshTokenService refreshTokenService,
                             AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserInfo> addNewUser(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(service.addUser(userInfo));
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable int id, Authentication authentication) {
        UserInfo userInfo = service.getUserById(id);
        if (userInfo == null || !userInfo.getName().equals(authentication.getName())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Product> getAllTheProducts() {
        return service.getProducts();
    }

    @GetMapping("/productName")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Product getProductByName(@RequestParam String name) {
        return service.getProductByName(name);
    }

    @PostMapping("/login")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
                    .token(refreshToken.getToken())
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getName());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken()) //returns same token
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }


    //    @GetMapping("/getUserById/{id}")
//    public ResponseEntity<UserInfo> getUserById(@PathVariable int id, Authentication authentication) {
//        UserInfo userInfo = service.getUserById(id);
//        if (userInfo == null || !userInfo.getName().equals(authentication.getName())) {
//            return ResponseEntity.status(403).build();
//        }
//        return ResponseEntity.ok(userInfo);
//    }


    //    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public Product getProductById(@PathVariable int id, Authentication authentication) {
//        Product product = service.getProduct(id);
//        if(product == null || product.getName())
//    }


}
