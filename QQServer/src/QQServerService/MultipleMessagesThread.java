package QQServerService;

import javafx.beans.binding.ObjectBinding;
import qqCommon.Message;
import qqCommon.MessageType;
import utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;


public class MultipleMessagesThread implements Runnable{


    @Override
    public void run() {
        Message message = new Message();
        while(true){
        System.out.println("Please enter the message you want to say (enter exit to exit)");
        String content = Utility.readString(100);
        if("exit".equalsIgnoreCase(content)){   break;}
        message.setMesType(MessageType.MESSAGE_CLIENT_AdsMessages);
        message.setContent(content);
        message.setSendTime(new Date().toString());

 //       String[] users = ManageServerThread.getOnlineUser().split(" ");
//        for (int i = 0; i < users.length; i++) {
//            ServerConnectThread thread = ManageServerThread.getServerConnectThread(users[i]);
//            ObjectOutputStream oos = null;
//            try {
//                oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
//                oos.writeObject(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
            // use another way
            HashMap<String, ServerConnectThread> hm = ManageServerThread.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while(iterator.hasNext()){
                String userId = iterator.next().toString();
                try {
                    ServerConnectThread serverConnectThread = hm.get(userId);
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectThread.getSocket().getOutputStream());
                    oos.writeObject(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
    }

