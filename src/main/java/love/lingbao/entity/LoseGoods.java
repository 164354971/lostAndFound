package love.lingbao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoseGoods {
    private Integer id;                     //int auto_increment comment '主键id'
    private Integer uid;                    //int         not null comment '发布者ID；用户表丢失物品发布的id（用户表的外键）',
    private String goodsName;              //varchar(20) not null comment '丢失物品的名称，1-20位的非空字段',
    private String goodsInfo;              //text        null comment '简述丢失的物品信息',
    private String goodsPos;               //varchar(20) not null comment '简述丢失的物品的大致位置',
    private LocalDateTime startTime;       //datetime    not null comment '丢失物品的大致开始时间',
    private LocalDateTime endTime;         //datetime    not null comment '丢失物品的大致结束时间',

    private LocalDateTime createTime;   //创建时间

    private LocalDateTime updateTime;   //资料更新时间
    /*constraint fk_lose_goods_uid
    foreign key (uid) references user (id)*/
}
