package qqclient.service;

import java.util.HashMap;

/**
 * @author Carson
 * @Version
 */
public class ManageThread {

    // 把多线程放入HashMap集合（好管理）, key是userId，value就是线程
    private static HashMap<String, ClientServiceThread> hm = new HashMap<>();

    // 将某个线程加入到集合
    public  static void addThread(String userId, ClientServiceThread thread){
        hm.put(userId,thread);
    }

    // 通过userId得到线程
    public static ClientServiceThread getClientServiceThread(String userId){
        return hm.get(userId);
    }

}
