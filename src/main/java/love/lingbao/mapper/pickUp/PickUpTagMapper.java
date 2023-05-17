package love.lingbao.mapper.PickUp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.lingbao.entity.pickUp.PickUpClassify;
import love.lingbao.entity.pickUp.PickUpTag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PickUpTagMapper extends BaseMapper<PickUpTag> {
}
