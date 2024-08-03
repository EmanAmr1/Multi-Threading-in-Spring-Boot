package com.spring.multithreading.service;

import com.spring.multithreading.entity.User;
import com.spring.multithreading.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    Logger logger = LoggerFactory.getLogger(UserService.class);



    @Async  // allowing it to execute in a separate thread managed by Spring's TaskExecutor
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception{

       long startTime = System.currentTimeMillis();

       List<User> users = parseCsvFile(file); // parse the CSV file and convert its contents into a list of User objects.

       logger.info("saving list of users of size {}" , users.size() ,"" +Thread.currentThread().getName()); // Logs an informational message indicating the number of users being saved.

        users=userRepo.saveAll(users); // Saves the list of User objects into the database using the saveAll method provided by userRepo.

       long endTime = System.currentTimeMillis();

        logger.info("Total time {}",(endTime-startTime));

        return CompletableFuture.completedFuture(users);  //Wraps the users list in a CompletableFuture and returns it.
                                                         // This allows the caller to asynchronously wait for the operation to complete.
    }



    @Async
    public CompletableFuture<List<User>> findAllUsers (){
        logger.info("get list  of user by"+Thread.currentThread().getName());
        List<User> users = userRepo.findAll();
        return CompletableFuture.completedFuture(users);
    }



    private List<User> parseCsvFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();  //Initializes an empty list to store User objects parsed from the CSV file.

       //Uses a BufferedReader to read the contents of the CSV file line by line.
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String line;  //Initializes a string line to store each line of the CSV file.

            // Read each line from the CSV file
            while ((line = br.readLine()) != null) {
                // Split the line by commas to get individual data elements
                String[] data = line.split(",");
                if (data.length >= 3) { // Ensure there are enough elements in the array
                    User user = new User();
                    // Assign values to the User object
                    user.setName(data[0]);
                    user.setEmail(data[1]);
                    user.setGender(data[2]);
                    users.add(user); // Add User object to the list
                } else {
                    // Handle cases where the CSV line doesn't have enough elements
                    // You can log an error or throw an exception depending on your requirements
                    throw new IllegalArgumentException("Invalid CSV format: each line must contain at least 3 elements");
                }
            }
            return users; // Return the list of parsed User objects
        } catch (IOException e) {
            // Handle IO exceptions
            logger.error("Failed to parse CSV file", e);
            throw new Exception("Failed to parse CSV file", e);
        }
    }

}

