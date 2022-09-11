package QQServerService;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
    public void run() {     // Keep online state and can be operated by multiple users through the run method

        while(true){ //Thinking that the while loop of QQserver and ServerConnectThread work together 
            // to realize the function of maintaining data transmission of port + thread.

            try {
                System.out.println("Server side and Cient side" + userId + " Keep communicating and read the data..");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    System.out.println(message.getSender() + " Request online user list");
                    String userList = ManageServerThread.getOnlineUser();
                    // Load all the information into the message class
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(userList);
                    message2.setGetter(message.getSender());

                    // Get the output stream and send it
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + " exit");
                    ManageServerThread.removeOneThread(message.getSender());
                    socket.close();
                    break;
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {

                    // If the recipient of the message is not online, save it first.
                    if(! ManageServerThread.isOnline(message.getGetter())){
                        arrayList.add(message);
                        for (int i = 0; i < arrayList.size(); i++) {
                            System.out.println(arrayList.get(i).getContent());
                        }
                        QQServer.getOfflineDb().put(message.getGetter(),arrayList);

                    }else {

                        // Get the thread of the corresponding recipient first
                        String getter = message.getGetter();
                        ServerConnectThread serverConnectThread = ManageServerThread.getServerConnectThread(getter);
                        // Get the sender's thread


                        ObjectOutputStream oos =
                                new ObjectOutputStream(serverConnectThread.getSocket().getOutputStream());

                        oos.writeObject(message);   // If the customer is not online, you can save it to the database to leave an offline message
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
//                    message.setContent("sorry，The user was not found");
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
                     System.out.println("Not to deal with for the time being～");
                 }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
