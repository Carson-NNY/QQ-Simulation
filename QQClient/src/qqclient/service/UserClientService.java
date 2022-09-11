package qqclient.service;

import qqCommon.Message;
import qqCommon.MessageType;
import qqCommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Carson
 *
 * 该类用于完成用户的登陆验证和注册等一系列操作
 */
public class UserClientService {

    // 因为在其他地方要用到user,socket信息，因此把它做成属性
    private User u= new User();
    private Socket socket;

    // 根据id和pwd来判断是否登陆成功
    public boolean checkUser(String userId ,String pwd){
        boolean flag = false;
        u.setUserId(userId);
        u.setPasswd(pwd);

        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发出user的信息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            // 读取server发送回来的信息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message)ois.readObject();

            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)){

                // 一旦创建一个和服务器端保持通信的线程： 创建一个类 ClientServiceThread
                ClientServiceThread cst = new ClientServiceThread(socket);// 这里把9999端口传入
                cst.start();
                // 为了多线程管理，放入集合管理（创建一个线程集合的类）
                ManageThread.addThread(userId,cst);

                flag= true;

            }else{
                // 登陆失败，但是需要关闭socket
                socket.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public void onlineFriendList(){
        Message message = new Message();
        message.setMesType (MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());


        // 获取这个用户对应的输出流
        try {
            OutputStream outputStream =
                    ManageThread.getClientServiceThread(u.getUserId()).getSocket().getOutputStream();
            // 这里能直接用getUseId，不用传参是因为checkUser已经动态绑定了，User的属性被改动了已经

            ObjectOutputStream obs = new ObjectOutputStream(outputStream);
            obs.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());// 给Server指定要退出哪个线程

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+" 退出系统");
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void privateChat(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("请输入想聊天的对象： ");
//        String obj  = scanner.next();
//        System.out.print("请输入想说的话： ");
//        String sentence = scanner.next();
//
//        String fullContent =obj+":"+sentence;
//        Message message = new Message();
//        message.setMesType(MessageType.MESSAGE_CLIENT_PRIVATECHAT);
//
//        message.setContent(fullContent);
//        message.setSender(u.getUserId());
//
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.writeObject(message);
//            System.out.println(u.getUserId()+ " 对"+obj+" 说： "+sentence);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
