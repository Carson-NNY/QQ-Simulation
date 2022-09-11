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


public class QQServer {

    private ServerSocket ss= null;
    // create a collection to store multiple users. If one of the users logs in, it will be judged to be legal.
    private static HashMap<String,User> validUsers = new HashMap<>();
    private  static ConcurrentHashMap<String, ArrayList<Message>> offlineDb = new ConcurrentHashMap<>();


    static {    // The static code block is initialized only once when you start the program, boost efficiencies!
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("400",new User("400","123456"));
        validUsers.put("500",new User("500","123456"));
        validUsers.put("600",new User("600","123456"));
    }

    // Verify whether the user is valid
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
            System.out.println("The Server port is monitoring in port 9999");
            ss = new ServerSocket(9999);
            Socket socket;
            new Thread(new MultipleMessagesThread()).start();   // The thread on which the system sends the message

            while (true) {  // This will keep the listening to port and accept multiple Client connections!
                socket = ss.accept();   

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();
                // Create a message object ready to reply to the client
                Message message =new Message();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());


                if(checkUser(user.getUserId(), user.getPasswd()))
                {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    oos.writeObject(message);

                    Iterator<String> iterator = offlineDb.keySet().iterator();

                    // Check if there are offline messages
                    while(iterator.hasNext()){
                        String id = iterator.next().toString();
                        if((user.getUserId().equals(id))){
                            for (int i = 0; i <offlineDb.get(id).size() ; i++) {
                                ObjectOutputStream oos2 = new ObjectOutputStream(socket.getOutputStream());
                                oos2.writeObject(offlineDb.get(id).get(i));
                                System.out.println("Sent an offline message to: "+user.getUserId());
                                //offlineDb.get(id).remove(i);
                            }
                        }
                    }

                    // Create a thread that communicates with the client, and the thread should hold the socket object
                    ServerConnectThread serverConnectThread
                            = new ServerConnectThread(socket, user.getUserId());
                    serverConnectThread.start();

                    // Create a collection of socket to manage multiple threads
                    ManageServerThread.addThread(user.getUserId(), serverConnectThread);

                }else{ // log in failed
                    System.out.println("user id："+ user.getUserId()+" verify failed");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    // If the login fails, close the socket .
                    socket.close();
                }

            }   // end while

        } catch (Exception e) {
            e.printStackTrace();
        } finally { // When Server exits the while loop, it means that socket is no longer listening, so close the resource (ServerSocket)
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
