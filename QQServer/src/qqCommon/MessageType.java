package qqCommon;

/**
 * @author Carson
 * @Version
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCESS = "1";
    String MESSAGE_LOGIN_FAIL = "2";
    String MESSAGE_COMM_MES = "3";  // 普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4"; // 请求返回用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5";// 返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6";
    String MESSAGE_CLIENT_GroupCHAT = "7";
    String MESSAGE_CLIENT_FileSending = "8";
    String MESSAGE_CLIENT_AdsMessages = "9";

}
