package love.lingbao.service.lose.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.lose.LoseGoods;
import love.lingbao.mapper.lose.LoseGoodsMapper;
import love.lingbao.service.lose.LoseGoodsService;
import org.springframework.stereotype.Service;

@Service
public class LoseGoodsServiceImpl extends ServiceImpl<LoseGoodsMapper, LoseGoods> implements LoseGoodsService {
}
