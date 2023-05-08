package love.lingbao.entity.lose;

import lombok.Data;

@Data
public class LoseTag {
    private Integer id; //int auto_increment comment '主键id'
    private Integer loseGoodsId; //int         not null comment '发布失物信息表的id（发布失物信息表的外键）',
    private String tag; //varchar(50) not null comment '失物有关信息的标签',

    public LoseTag(Integer id, Integer loseGoodsId, String tag) {
        this.id = id;
        this.loseGoodsId = loseGoodsId;
        this.tag = tag;
    }

    /*constraint fk_lose_tag_lose_goods_id
    foreign key (lose_goods_id) references lose_goods (id)*/
}
