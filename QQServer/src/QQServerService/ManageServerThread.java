package QQServerService;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Carson
 * @Version
 */
public class ManageServerThread {

    // 思考：怎么把这种线程管理方式变成线程池模式？牵涉端口类多线程用不到线程池吗？

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


        // 遍历集合，取出key
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
