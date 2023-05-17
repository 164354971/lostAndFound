package love.lingbao.entity.pickUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickUpClassify {
    private Integer id; //int auto_increment comment '主键id'
    private Integer pickUpGoodsId; //int not null comment '发布失物信息表的id（发布失物信息表的外键）',
    private Integer classifyId; //int not null comment '物品分类表的id（物品分类表的外键）',
}
