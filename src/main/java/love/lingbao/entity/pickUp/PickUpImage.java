package love.lingbao.entity.pickUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickUpImage {
    private Integer id; //int auto_increment comment '主键id'
    private Integer pickUpGoodsId; //int          not null comment '发布失物信息表的id（发布失物信息表的外键）',
    private String url; //varchar(255) not null comment '失物有关信息的图片链接',

}
