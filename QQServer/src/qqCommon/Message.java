package qqCommon;

import java.awt.*;
import java.io.Serializable;

public class Message implements Serializable
// as an object stream is transmitted at both ends, it is important to serialize this class. In 
// the transport pipeline, only objects of this class are passed through, which can simplify a lot of code.
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

    private static final long serialVersionUID = 1L;   

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
