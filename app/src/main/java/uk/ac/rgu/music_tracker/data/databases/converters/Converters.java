package uk.ac.rgu.music_tracker.data.databases.converters;

import androidx.room.TypeConverter;
import androidx.room.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {
    // Function to convert a list to a string
    @TypeConverter
    public static String listToString(List<Integer> list) {
        // Creating a string builder
        StringBuilder output = new StringBuilder("");

        // Checking if the input list is empty
        if (list.isEmpty()) {
            return output.toString();
        };

        // Add each list item to the output string
        for (Integer current : list) {
            output.append(current.toString()).append(",");
        };

        // Removing the last comma
        output.deleteCharAt(output.lastIndexOf(","));

        return output.toString();
    };

    // Function to convert a string to a list of strings
    @TypeConverter
    public static ArrayList<Integer> StringToList(String listInString) {
        // Creating the arraylist to be returned
        ArrayList<Integer> output = new ArrayList<>();

        // Checking if the string is empty
        if (listInString.isEmpty()) {
            return output;
        };

        // Checking if the string has only one element in it
        if (!listInString.contains(",")) {
            // Turning the string element into an integer and adding it to the list
            output.add(Integer.parseInt(listInString));
            return output;
        };

        // Creating an array of the song uids
        String[] tempList = listInString.split(",");

        for (String current : tempList) {
            output.add(Integer.parseInt(current));
        };

        return output;
    };
}
