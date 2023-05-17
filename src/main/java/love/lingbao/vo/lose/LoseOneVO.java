package love.lingbao.vo.lose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import love.lingbao.entity.lose.LoseClassify;
import love.lingbao.entity.lose.LoseGoods;
import love.lingbao.entity.lose.LoseImage;
import love.lingbao.entity.lose.LoseTag;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoseOneVO {
    private Integer id;
    private LoseGoods loseGoods;
    private List<LoseImage> loseImages;
    private List<LoseTag> loseTags;
    private List<LoseClassify> loseClassifies;
}
