package love.lingbao.entity.lose;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoseOne extends LoseGoods{
    private List<LoseImage> loseImages;
    private List<LoseTag> loseTags;
    private List<LoseClassify> loseClassifies;
}
