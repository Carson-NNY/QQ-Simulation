package qqclient.view;

import qqclient.service.FileClientService;
import qqclient.service.MessageClientService;
import qqclient.service.UserClientService;
import qqclient.utils.Utility;

import java.util.Scanner;

/**
 * @author Carson
 * 大量知识点，反复复习！！
 *
 */
public class QQView {
    private boolean loop = true;
    private String key = "";    // 接收用户键盘的输入
    private  UserClientService userClientService = new UserClientService();  // 用于登陆服务器和注册
    private MessageClientService messageClientService = new MessageClientService();
    private    FileClientService fileClientService  = new FileClientService();

    // OOP思想： 老韩的思路！绝了，多复习：
    // 1: 利用UserClientService类创建了一个user的对象，并且在类的方法中利用输出流发送出这个对象
    // 2： 再得到输入流来判断是否user登陆成功，如果成功的话就创建一个线程（保持通信），并且把这个线程
    // 放入一个线程的集合类，以便后期实现多线程

    public static void main(String[] args) {
        // 利用main方法来测试
        new QQView().mainMenu();
        System.out.println("退出系统成功");
    }

    private void mainMenu() {

        while (loop){
            System.out.println("================欢迎登陆网络通信系统================");
            System.out.println("\t\t 1 登陆系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入: ");

            key = Utility.readString(1);
            switch (key){
                case "1":
                    System.out.println("请输入用户号： ");
                    String userId = Utility.readString(20);
                    System.out.println(" 请输入密码： ");
                    String userPwd = Utility.readString(20);

                    // 先梳理逻辑～
                    if(userClientService.checkUser(userId,userPwd))
                    // 在参数里面创建了线程，开启start（）进行持续的通信，并且把单个线程放进了集合里，而且把
                        //里面的公有字段（User，Socket）设置好了，妙！
                    {
                        System.out.println("================欢迎用户： "+userId+" 登陆成功================");
                        // 进入二级菜单
                        while(loop){
                            System.out.println("\n================网络通信系统二级菜单================");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择： ");
                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    // 注意： 必须要用userClientService，因为设置的全局变量，所有的属性
                                    // 都在里面！
                                    userClientService.onlineFriendList();

                                    break;
                                case "2":
                                    System.out.print("请输入想说的话： ");
                                    String content = Utility.readString(100);
                                    messageClientService.groupChat(content,userId);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的对象： ");
                                    String getter  = Utility.readString(50);
                                    System.out.print("请输入想说的话： ");
                                    String sentence = Utility.readString(100);
                                    messageClientService.privateChat(sentence,userId,getter);

                                    //userClientService.privateChat();
                                    break;
                                case "4":
                                    System.out.println("请输入你想把文件发送给的用户");
                                     getter = Utility.readString(60);
                                    System.out.println("输入这条语句（被发送文件的路径）: /Users/carson/Downloads/java.bili/QQClient/src/IMG_4427.JPG");
                                    String scr = Utility.readString(100);
                                    System.out.println("输入这条语句把文件发送到这里: /Users/carson/Downloads/java.bili/QQClient/src/qqclient/view/发送过来的第二个哈哈.MOV");
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
                        System.out.println("登陆服务器失败");
                    }

                    break;
                case "9" :
                    loop = false;
                    break;
            }

        }


    }




}
