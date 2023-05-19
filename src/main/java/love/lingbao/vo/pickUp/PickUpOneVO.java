package love.lingbao.vo.pickUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import love.lingbao.entity.User;
import love.lingbao.entity.pickUp.PickUpClassify;
import love.lingbao.entity.pickUp.PickUpGoods;
import love.lingbao.entity.pickUp.PickUpImage;
import love.lingbao.entity.pickUp.PickUpTag;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickUpOneVO {
    private Integer id;
    private User user;
    private PickUpGoods pickUpGoods;
    private List<PickUpImage> pickUpImages;
    private List<PickUpTag> pickUpTags;
    private List<PickUpClassify> pickUpClassifies;
}
