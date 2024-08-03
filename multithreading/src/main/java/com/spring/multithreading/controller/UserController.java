package com.spring.multithreading.controller;

import com.spring.multithreading.entity.User;
import com.spring.multithreading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/users" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile fileItem : files) {
            userService.saveUsers(fileItem);
        }
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @GetMapping(value = "/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
     return userService.findAllUsers().thenApply(ResponseEntity::ok);

    }

    @GetMapping(value = "/getUsersByThread", produces = "application/json") //It specifies that the response should be in JSON format.
    public ResponseEntity getUsers(){
        CompletableFuture<List<User>> users1 = userService.findAllUsers();
        CompletableFuture<List<User>> users2 = userService.findAllUsers();
        CompletableFuture<List<User>> users3 = userService.findAllUsers();
        CompletableFuture.allOf(users1,users2,users3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*
    allOf does not block the current thread.
    It simply ensures that the combined CompletableFuture completes when all individual futures
    (users1, users2, users3) complete.
   join() is then called on this combined future to block the current thread
   until all asynchronous operations (findAllUsers calls) are complete.
    This ensures that the method waits for all user fetching operations to finish before proceeding.

     */
}
