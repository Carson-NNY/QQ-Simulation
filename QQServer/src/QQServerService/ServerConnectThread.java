package QQServerService;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Carson
 * 该类的一个对象和某个客户端保持通信
 */
public class ServerConnectThread extends Thread {

    private Socket socket;
    private String userId;
    ArrayList<Message> arrayList = new ArrayList<>();


    public ServerConnectThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {     // 通过run方法实现保持在线并且可以多个用户操作

        while(true){ //思考QQserver和ServerConnectThread的while循环一起作用实现了端口+线程的保持数据传输功能

            try {
                System.out.println("Server端和Cient端" + userId + " 保持通信，读取数据中..");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    System.out.println(message.getSender() + " 请求在线用户列表");
                    String userList = ManageServerThread.getOnlineUser();
                    // 把所有信息装进message类
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(userList);
                    message2.setGetter(message.getSender());

                    // 获得输出流并且发送
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + " 退出");
                    ManageServerThread.removeOneThread(message.getSender());
                    socket.close();
                    break;
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {

                    // 如果息=消息的接收方没在线，先存起来
                    if(! ManageServerThread.isOnline(message.getGetter())){
                        arrayList.add(message);
                        for (int i = 0; i < arrayList.size(); i++) {
                            System.out.println(arrayList.get(i).getContent());
                        }
                        QQServer.getOfflineDb().put(message.getGetter(),arrayList);

                    }else {

                        // 先拿到对应的接受者的线程
                        String getter = message.getGetter();
                        ServerConnectThread serverConnectThread = ManageServerThread.getServerConnectThread(getter);
                        // 拿到发送者的线程

                        //猜测离线文件： 开个线程，while true一直等着用户上线，上线了就break然后发送过去

                        ObjectOutputStream oos =
                                new ObjectOutputStream(serverConnectThread.getSocket().getOutputStream());

                        oos.writeObject(message);   // 如果客户不在线，可以保存到数据库实现离线留言
                    }
//                     if(ManageServerThread.getServerConnectThread(userId2) !=null){
//                         OutputStream outputStream =
//                                 ManageServerThread.getServerConnectThread(userId2).socket.getOutputStream();
//                         ObjectOutputStream oos = new ObjectOutputStream(outputStream);
//
//                         message.setMesType(MessageType.MESSAGE_COMM_MES);
//                         message.setSender(message.getSender());
//                         message.setContent(userId2+ ":"+content);
//                         oos.writeObject(message);

//                } else {
//                    message.setContent("sorry，未找到该用户");
//                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                    oos.writeObject(message);
//                }


            }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_GroupCHAT)){
                    String[] users = ManageServerThread.getOnlineUser().split(" ");
                    for (int i = 0; i < users.length; i++) {
                        ServerConnectThread thread = ManageServerThread.getServerConnectThread(users[i]);
                        ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(message);
                    }
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_FileSending)){

                    if(! ManageServerThread.isOnline(message.getGetter())){
                        arrayList.add(message);
                        for (int i = 0; i < arrayList.size(); i++) {
                            System.out.println(arrayList.get(i).getContent());
                        }
                        QQServer.getOfflineDb().put(message.getGetter(),arrayList);

                    }else {
                    String getter = message.getGetter();
                    ObjectOutputStream oos =
                            new ObjectOutputStream(ManageServerThread.getServerConnectThread(getter).getSocket().getOutputStream());
                    oos.writeObject(message);
                    }
                }

                 else {
                     System.out.println("暂不处理～");
                 }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
