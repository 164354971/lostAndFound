package love.lingbao.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;         //int auto_increment comment '主键id'
    private String username;    //varchar(16)                            not null comment '用户名',
    private String password;    //varchar(32)           default 'e10adc3949ba59abbe56e057f20f883e' not null comment '登录密码',
    private String phone;       //varchar(11)                            not null comment '手机号',
    private String name;        //varchar(16)                            not null comment '昵称',
    private String gender;      //enum ('男', '女', '保密') default '男'      not null comment '性别'

    private LocalDateTime createTime;   //创建时间

    private LocalDateTime updateTime;   //资料更新时间
}
