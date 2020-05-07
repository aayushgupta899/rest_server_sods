package com.restserversoda.spring;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Test {

    static UserModel user;
    static List<UserModel> userModelList;
    public static void readUsers()
    {
        Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy HH:mm:ss").create();
        try {
            FileReader reader = new FileReader("data.json");
            List<UserModel> list = gson.fromJson(reader, new TypeToken<List<UserModel>>(){}.getType());
            reader.close();
            System.out.println(list.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addUsers()
    {
        userModelList = new ArrayList<>();
        user = new UserModel("amnesia123", UUID.randomUUID().toString(), new ArrayList<>(), 21, "m", "na", "student", 45.6);
        userModelList.add(user);
        user = new UserModel("kakashi899", UUID.randomUUID().toString(), new ArrayList<>(), 24, "m", "na", "student", 39.8);
        userModelList.add(user);
        user = new UserModel("polka146", UUID.randomUUID().toString(), new ArrayList<>(), 29, "f", "na", "teacher", 5.6);
        userModelList.add(user);
        user = new UserModel("penguin666", UUID.randomUUID().toString(), new ArrayList<>(), 22, "f", "na", "teacher", 12.65);
        userModelList.add(user);
        user = new UserModel("timbuktu999", UUID.randomUUID().toString(), new ArrayList<>(), 25, "m", "asthma", "student", 32.89);
        userModelList.add(user);
        user = new UserModel("nigeria111", UUID.randomUUID().toString(), new ArrayList<>(), 35, "f", "asthma", "teacher", 2.6);
        userModelList.add(user);
        user = new UserModel("india887", UUID.randomUUID().toString(), new ArrayList<>(), 32, "m", "asthma", "student", 76.2);
        userModelList.add(user);
        user = new UserModel("babushka324", UUID.randomUUID().toString(), new ArrayList<>(), 33, "f", "asthma", "teacher", 42.1);
        userModelList.add(user);
        user = new UserModel("bamboozle126", UUID.randomUUID().toString(), new ArrayList<>(), 31, "m", "na", "student", 55.9);
        userModelList.add(user);
        user = new UserModel("chikara212", UUID.randomUUID().toString(), new ArrayList<>(), 23, "f", "na", "teacher", 65.6);
        userModelList.add(user);
        Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy HH:mm:ss").create();
        try {
            String json = gson.toJson(userModelList);
            FileWriter writer = new FileWriter("data.json");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //addUsers();
        readUsers();
    }

}
