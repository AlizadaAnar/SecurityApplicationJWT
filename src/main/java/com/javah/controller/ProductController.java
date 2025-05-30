package com.javah.controller;

import com.javah.dto.Product;
import com.javah.entity.UserInfo;
import com.javah.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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


    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserInfo> addNewUser(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(service.addUser(userInfo));
    }

    @GetMapping("/all")
    public List<Product> getAllTheProducts() {
        return service.getProducts();
    }

    @GetMapping("/productName")
    public Product getProductByName(@RequestParam String name) {
        return service.getProductByName(name);
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
