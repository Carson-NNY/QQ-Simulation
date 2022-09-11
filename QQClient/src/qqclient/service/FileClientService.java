package qqclient.service;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.*;
import java.util.Scanner;

/**
 * @author Carson
 * @Version
 */
public class FileClientService {

    public void fileTransfer(String src, String dest, String senderId,String getterId){

        Message message = new Message();
        message.setScrPath(src);
        message.setDestPath(dest);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setMesType(MessageType.MESSAGE_CLIENT_FileSending);

        FileInputStream fileInputStream =null;
        // 跟之前IO流用法不一样，这里没用老韩给的工具包，而是自己进行读取的一个方式，记下！
        byte[] bytes = new byte[(int)new File(src).length()]; // 这样能确定byte正好是文件的大小
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(bytes);    // 这里把源文件的数据写入了bytes

            message.setBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 如果创建成功，要记得关流
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(senderId+" 给 "+getterId+" 发送文件 "+src +" 到对面的电脑目录 "+dest);

        // 发出还是通过Message对象发送出去
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageThread.getClientServiceThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
