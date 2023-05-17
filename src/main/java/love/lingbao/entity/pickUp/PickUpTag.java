package love.lingbao.entity.pickUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickUpTag {
    private Integer id; //int auto_increment comment '主键id'
    private Integer pickUpGoodsId; //int         not null comment '发布失物信息表的id（发布失物信息表的外键）',
    private String tag; //varchar(50) not null comment '失物有关信息的标签',
}
