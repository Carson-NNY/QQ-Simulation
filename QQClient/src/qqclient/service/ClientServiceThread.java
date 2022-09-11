package qqclient.service;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Carson
 * @Version
 */
public class ClientServiceThread extends Thread {
    // 该线程需要持有 Socket
    private Socket socket;

    // 构造器可以接受Socket的对象
    public ClientServiceThread(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    // 因为线程需要在后台和服务器通信，因此我们while循环
    public void run(){

        while(true){

            System.out.println("客户端通信： 等待读取从服务器发送的消息");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                // 如果服务器没有发送Message对象，线程会卡在这里

                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] s= message.getContent().split(" ");
                    System.out.println("=========当前用户列表==========");
                    for (int i = 0; i < s.length; i++) {
                        System.out.println("用户： "+ s[i]);
                    }

                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){

                    System.out.println(message.getSender()+" 对"+ message.getGetter()
                            +" 说： " +message.getContent());

                }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_GroupCHAT)){
                    System.out.println(message.getSender()+" 对所有人说："+message.getContent());

                } else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_FileSending)){
                    System.out.println(message.getSender() +" 给 "+message.getSender()+" 发文件");
                    byte[] data = message.getBytes();
                    String dest = message.getDestPath();

                    FileOutputStream fileOutputStream = new FileOutputStream(dest);
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                    System.out.println("保存文件成功");
                }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_AdsMessages)){
                    System.out.println("系统对所有人说："+message.getContent());
                }

                else {
                    System.out.println("暂时不处理");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
