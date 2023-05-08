package love.lingbao.entity;

import lombok.Data;

@Data
public class GoodsClassify {
    private Integer id;   //int auto_increment comment '主键id'
    private String name; //varchar(8) not null comment '标签名'
}
