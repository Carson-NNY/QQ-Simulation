package QQServerService;

import qqCommon.Message;
import qqCommon.MessageType;
import qqCommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Carson
 * 大量知识点，反复复习！！
 */
public class QQServer {

    private ServerSocket ss= null;
    // 创建一个集合存放多个用户，如果是其中的用户登陆，判断为合法的
    private static HashMap<String,User> validUsers = new HashMap<>();
    private  static ConcurrentHashMap<String, ArrayList<Message>> offlineDb = new ConcurrentHashMap<>();


    static {    // static代码块在启动程序时只会初始化一次，节省资源！
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("至尊宝",new User("至尊宝","123456"));
        validUsers.put("紫霞仙子",new User("紫霞仙子","123456"));
        validUsers.put("牛魔王",new User("牛魔王","123456"));
    }

    // 验证用户是否合法
    public boolean checkUser(String userId, String passwd){
        User user = validUsers.get(userId);
        if(user ==null ){
            return false;
        }
        if(! user.getPasswd().equals(passwd))
        {
            return  false;
        }
        return true;
    }

    public static ConcurrentHashMap<String, ArrayList<Message>> getOfflineDb() {
        return offlineDb;
    }

    public QQServer(){

        try {
            System.out.println("Server端在9999端口中监听");
            ss = new ServerSocket(9999);
            Socket socket;
            new Thread(new MultipleMessagesThread()).start();   // 系统推送消息的线程

            while (true) {  // 这样会保持一直监听端口，能够接受多个Client连接！
                socket = ss.accept();   // 妙！

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();
                // 创建message对象准备回复Client端
                Message message =new Message();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());


                if(checkUser(user.getUserId(), user.getPasswd()))
                {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    oos.writeObject(message);

                    Iterator<String> iterator = offlineDb.keySet().iterator();

                    // 检查是否有离线消息
                    while(iterator.hasNext()){
                        String id = iterator.next().toString();
                        if((user.getUserId().equals(id))){
                            for (int i = 0; i <offlineDb.get(id).size() ; i++) {
                                ObjectOutputStream oos2 = new ObjectOutputStream(socket.getOutputStream());
                                oos2.writeObject(offlineDb.get(id).get(i));
                                System.out.println("发送了离线消息给: "+user.getUserId());
                                //offlineDb.get(id).remove(i);
                            }
                        }
                    }

                    // 创建一个线程和客户端保持通讯，而且这个线程应该持有socket对象
                    ServerConnectThread serverConnectThread
                            = new ServerConnectThread(socket, user.getUserId());
                    serverConnectThread.start();
                    // 思考：怎么把这种线程管理方式变成线程池模式？牵涉端口类多线程用不到线程池吗？

                    // 创建一个socket的集合，用于管理多个线程
                    ManageServerThread.addThread(user.getUserId(), serverConnectThread);

                }else{ // 登陆失败
                    System.out.println("用户 id："+ user.getUserId()+" 验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    // 如果登陆失败,socket要关闭
                    socket.close();
                }

            }   // end while

        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 当Server退出了while循环，意味着socket不再监听，所以要关闭资源（ServerSocket）
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
