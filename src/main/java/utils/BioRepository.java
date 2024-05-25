package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import database.UserDAOImpl;
import usermanager.User;

public class BioRepository {
    private String bio;
    private final User currentUser;
    public BioRepository(User user){
        this.currentUser = user;
    }

    public String readBio(){
        String bio = currentUser.getBio();
        System.out.println("Bio for " + currentUser.getUsername() + ": " + bio);

        return bio;
    }
}
