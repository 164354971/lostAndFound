package love.lingbao.entity;

import java.io.Serializable;

public class TempChat implements Serializable {
    private static final long serialVersionUID=1L;
    private Integer id;                         //主键id
    private Integer senderId;                   //发送者
    private Integer getterId;                   //接收者
    private String content;                     //内容
    private String sendTime;                    //发送时间
    private String messageType;                 //消息类型

    public TempChat() {
    }

    public TempChat(Integer id, Integer senderId, Integer getterId, String content, String sendTime, String messageType) {
        this.id = id;
        this.senderId = senderId;
        this.getterId = getterId;
        this.content = content;
        this.sendTime = sendTime;
        this.messageType = messageType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getGetterId() {
        return getterId;
    }

    public void setGetterId(Integer getterId) {
        this.getterId = getterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
