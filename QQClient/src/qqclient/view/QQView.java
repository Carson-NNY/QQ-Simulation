package qqclient.view;

import qqclient.service.FileClientService;
import qqclient.service.MessageClientService;
import qqclient.service.UserClientService;
import qqclient.utils.Utility;

import java.util.Scanner;


public class QQView {
    private boolean loop = true;
    private String key = "";    // Receive input from the user
    private  UserClientService userClientService = new UserClientService();  // Used to log in to the server 
    private MessageClientService messageClientService = new MessageClientService();
    private    FileClientService fileClientService  = new FileClientService();

    // OOP concept:： ：
    // 1:  create a user object using the UserClientService class, and send it out using the output stream in the class's methods
    // 2： get the input stream to determine whether the user login is successful, if it is successful, create a thread (keep communication), and put the thread.
// into a collection class of threads for later implementation of multithreading

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("Exit the system successfully");
    }

    private void mainMenu() {

        while (loop){
            System.out.println("================Welcome to the network communication system.================");
            System.out.println("\t\t 1 Log in system");
            System.out.println("\t\t 9 Exit the system");
            System.out.print("Please enter : ");

            key = Utility.readString(1);
            switch (key){
                case "1":
                    System.out.println("Please enter the user ID： ");
                    String userId = Utility.readString(20);
                    System.out.println(" Enter Your password： ");
                    String userPwd = Utility.readString(20);


                    if(userClientService.checkUser(userId,userPwd))
                    // Create a thread in the parameter,  start () for continuous communication, put a single thread into the collection, 
                        // and set the public field (User,Socket) inside
                    {
                        System.out.println("================Welcome user： "+userId+" Log in successfully================");
                        // Go to the secondary menu
                        while(loop){
                            System.out.println("\n================Second-level menu of network communication system================");
                            System.out.println("\t\t 1 Display a list of online users");
                            System.out.println("\t\t 2 Send messages in groups");
                            System.out.println("\t\t 3 Chat in private.");
                            System.out.println("\t\t 4 Send a file");
                            System.out.println("\t\t 9 exit");
                            System.out.println("Please enter your choice： ");
                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    // Note: userClientService must be used, because of global variable, all the properties are in it!
                                    userClientService.onlineFriendList();

                                    break;
                                case "2":
                                    System.out.print("Please enter what you want to say： ");
                                    String content = Utility.readString(100);
                                    messageClientService.groupChat(content,userId);
                                    break;
                                case "3":
                                    System.out.print("Please enter the person you want to chat with： ");
                                    String getter  = Utility.readString(50);
                                    System.out.print("Please enter what you want to say： ");
                                    String sentence = Utility.readString(100);
                                    messageClientService.privateChat(sentence,userId,getter);

                                    //userClientService.privateChat();
                                    break;
                                case "4":
                                    System.out.println("Please enter the user to whom you want to send the file");
                                     getter = Utility.readString(60);
                                    System.out.println("Enter this statement (the path to the file being sent,it is subject to be changed)）: /Users/carson/Downloads/java.bili/QQClient/src/IMG_4427.JPG");
                                    String scr = Utility.readString(100);
                                    System.out.println("Enter this statement to send the file here : /Users/carson/Downloads/java.bili/QQClient/src/qqclient/view/发送过来的第二个哈哈.MOV");
                                    String dest = Utility.readString(100);
                                    fileClientService.fileTransfer(scr,dest,userId,getter);
                                    break;
                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }

                        }
                    }   // end if
                    else{
                        System.out.println("Failed to login to the server");
                    }

                    break;
                case "9" :
                    loop = false;
                    break;
            }

        }


    }




}
