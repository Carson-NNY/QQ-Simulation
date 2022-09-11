package qqclient.service;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.*;
import java.util.Scanner;

public class FileClientService {

    public void fileTransfer(String src, String dest, String senderId,String getterId){

        Message message = new Message();
        message.setScrPath(src);
        message.setDestPath(dest);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setMesType(MessageType.MESSAGE_CLIENT_FileSending);

        FileInputStream fileInputStream =null;
        byte[] bytes = new byte[(int)new File(src).length()]; // This makes sure that byte is exactly the size of the file.
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(bytes);    // Here, the data from the source file is written to the bytes

            message.setBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // If the creation is successful, remember to close the stream
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(senderId+" send a file to "+getterId +" to the directory: "+dest);

        //  send out via Message object
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageThread.getClientServiceThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
