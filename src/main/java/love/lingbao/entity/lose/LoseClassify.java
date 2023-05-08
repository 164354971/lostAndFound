package love.lingbao.entity.lose;

import lombok.Data;

@Data
public class LoseClassify {
    private Integer id; //int auto_increment comment '主键id'
    private Integer loseGoodsId; //int not null comment '发布失物信息表的id（发布失物信息表的外键）',
    private Integer classifyId; //int not null comment '物品分类表的id（物品分类表的外键）',
    /*constraint fk_lose_classify_classify_id
    foreign key (classify_id) references goods_classify (id),
    constraint fk_lose_classify_lose_goods_id
    foreign key (lose_goods_id) references lose_goods (id)*/
}
