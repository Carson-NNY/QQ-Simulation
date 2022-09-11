package qqCommon;

public interface MessageType {
    String MESSAGE_LOGIN_SUCCESS = "1";
    String MESSAGE_LOGIN_FAIL = "2";
    String MESSAGE_COMM_MES = "3";  // general messages
    String MESSAGE_GET_ONLINE_FRIEND = "4"; // Request to return user list
    String MESSAGE_RET_ONLINE_FRIEND = "5";// Return to the list of online users
    String MESSAGE_CLIENT_EXIT = "6";
    String MESSAGE_CLIENT_GroupCHAT = "7";
    String MESSAGE_CLIENT_FileSending = "8";
    String MESSAGE_CLIENT_AdsMessages = "9";

}
