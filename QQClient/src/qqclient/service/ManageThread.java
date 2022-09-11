package qqclient.service;

import java.util.HashMap;


public class ManageThread {

    // Put multithreading into the HashMap collection (easy to manage).  key is userId,value is thread.
    private static HashMap<String, ClientServiceThread> hm = new HashMap<>();

    // Add a thread to the collection
    public  static void addThread(String userId, ClientServiceThread thread){
        hm.put(userId,thread);
    }

    // Get threads through userId
    public static ClientServiceThread getClientServiceThread(String userId){
        return hm.get(userId);
    }

}
