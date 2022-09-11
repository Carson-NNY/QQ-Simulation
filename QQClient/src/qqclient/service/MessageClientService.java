package qqclient.service;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Carson
 * @Version
 */
public class MessageClientService {

    public void privateChat(String content, String senderId, String getterId){

        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(senderId+" 对"+ getterId+" 说 " +content);
        // 因为对应的sockey端口在线程里，所以找到线程就都能找到端口
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageThread.getClientServiceThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void groupChat(String content,String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_GroupCHAT);
        message.setSender(senderId);
        message.setContent(content);
        System.out.println(senderId+" 对所有人说 " +content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageThread.getClientServiceThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
