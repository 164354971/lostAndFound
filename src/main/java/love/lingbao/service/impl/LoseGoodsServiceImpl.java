package love.lingbao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.LoseGoods;
import love.lingbao.mapper.LoseGoodsMapper;
import love.lingbao.service.LoseGoodsService;
import org.springframework.stereotype.Service;

@Service
public class LoseGoodsServiceImpl extends ServiceImpl<LoseGoodsMapper, LoseGoods> implements LoseGoodsService {
}
