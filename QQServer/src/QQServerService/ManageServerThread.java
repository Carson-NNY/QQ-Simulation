package QQServerService;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;


public class ManageServerThread {

    // Think about it: how to turn this thread management mode into thread pool mode? 
// Do you need a thread pool for multithreading involving port classes?

    private static HashMap<String, ServerConnectThread> hm = new HashMap<>();

    public static void addThread(String userId, ServerConnectThread socket){
        hm.put(userId,socket);
    }

    public static void removeOneThread(String userId){
        hm.remove(userId);
    }

    public static HashMap<String, ServerConnectThread> getHm() {
        return hm;
    }

    public static ServerConnectThread getServerConnectThread(String userId){
        return hm.get(userId);
    }

    public static String getOnlineUser(){


        // Iterate the collection and take out the key
        Iterator<String> iterator = hm.keySet().iterator();
        String userList ="";
        while(iterator.hasNext()){
            userList += iterator.next()+" ";
        }
        return userList;

    }

    public static boolean isOnline(String userId){
        Iterator<String> iterator = hm.keySet().iterator();
        while(iterator.hasNext()){
            if(userId.equals( iterator.next())){
                return true;
            }
        }
        return false;
    }

}
