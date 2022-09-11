package qqCommon;

import java.io.Serializable;

/**
 * @author Carson
 * @Version
 */
public class Message implements Serializable
    //作为对象流在两端传输，需要实现序列化
{
    private  String sender;
    private String getter;
    private String content;
    private  String sendTime;
    private String mesType;

    private byte[] bytes;
    private int fileLength=0;
    private String destPath;
    private String scrPath;

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public void setScrPath(String scrPath) {
        this.scrPath = scrPath;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getFileLength() {
        return fileLength;
    }

    public String getDestPath() {
        return destPath;
    }

    public String getScrPath() {
        return scrPath;
    }

    private static final long serialVersionUID = 1L;    // 增强稳定性


    public Message(){}

    public String getSender() {
        return sender;
    }

    public String getGetter() {
        return getter;
    }

    public String getContent() {
        return content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
