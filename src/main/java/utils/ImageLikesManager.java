package utils;
import java.io.*;
import java.util.*;

import datahandler.FileHandler;

public class ImageLikesManager {

    private final String likesFilePath = "data/likes.txt";
    private final FileHandler fileHandler = new FileHandler();

    public void likeImage(String username, String imageID) throws IOException {
        Map<String, Set<String>> likesMap = readLikes();
        if (!likesMap.containsKey(imageID)) {
            likesMap.put(imageID, new HashSet<>());
        }
        Set<String> users = likesMap.get(imageID);
        if (users.add(username)) {
            saveLikes(likesMap);
        }
    }

    private Map<String, Set<String>> readLikes() throws IOException {
        Map<String, Set<String>> likesMap = new HashMap<>();
        List<String> lines = fileHandler.readLinesFromFile(likesFilePath);
        for (String line : lines) {
            String[] parts = line.split(":");
            String imageID = parts[0];
            Set<String> users = new HashSet<>(Arrays.asList(parts[1].split(",")));
            likesMap.put(imageID, users);
        }
        return likesMap;
    }

    private void saveLikes(Map<String, Set<String>> likesMap) throws IOException {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : likesMap.entrySet()) {
            String line = entry.getKey() + ":" + String.join(",", entry.getValue());
            content.append(line).append(System.lineSeparator());
        }
        fileHandler.appendLineToFile(likesFilePath, content.toString());
    }
}
