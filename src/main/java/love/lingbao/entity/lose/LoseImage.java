package love.lingbao.entity.lose;

import lombok.Data;

@Data
public class LoseImage {
    private Integer id; //int auto_increment comment '主键id'
    private Integer loseGoodsId; //int          not null comment '发布失物信息表的id（发布失物信息表的外键）',
    private String url; //varchar(255) not null comment '失物有关信息的图片链接',

    public LoseImage(Integer id, Integer loseGoodsId, String url) {
        this.id = id;
        this.loseGoodsId = loseGoodsId;
        this.url = url;
    }

    /*constraint fk_lose_image_lose_goods_id
    foreign key (lose_goods_id) references lose_goods (id)*/
}
