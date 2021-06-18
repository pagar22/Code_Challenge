package com.example.code_challenge;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public class NicheTasksObject {

    String title;
    String description;
    List<String> assignedTo;
    String date;


    public NicheTasksObject(String title, String description, List<String> assignedTo, String date){
        this.title = title;
        this.description = description;
        this.assignedTo = assignedTo;
        this.date = date;
    }

    @Override
    public String toString(){
        return "Department: " + title + '\n'
                + "Task: " + description + '\n'
                + "Assigned to: " + assignedTo.toString()
                .replace("[", "").replace("]","") + '\n'
                + "Deadline: " + date;
    }

    public static void save(Context context, String title){
        Set<String> resultSet = new HashSet<>();
        for(NicheTasksObject object : NicheTasksList.nicheTasks)
            resultSet.add(object.toString());

        SharedPreferences sharedPreferences =
                context.getSharedPreferences("Code_Challenge", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("TaskList"+title, resultSet);
        editor.commit();
    }


    public static NicheTasksObject parser(String string) throws NullPointerException{
        if(!string.equals("")) {
            String[] list1 = string.split("\n");
            String[] requiredResult = new String[4];
            List<String> assignedTo = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (i == 2) {
                    String x = list1[i].split(":")[1].substring(1);
                    assignedTo = Arrays.asList(x.split(","));
                } else
                    requiredResult[i] = list1[i].split(":", 2)[1].substring(1);
            }
            return new NicheTasksObject(requiredResult[0], requiredResult[1], assignedTo, requiredResult[3]);
        }
        return null;
    }

}
