package love.lingbao.service.pickUp.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.pickUp.PickUpGoods;
import love.lingbao.mapper.PickUp.PickUpGoodsMapper;
import love.lingbao.service.pickUp.PickUpGoodsService;
import org.springframework.stereotype.Service;


@Service
public class PickUpGoodsServiceImpl extends ServiceImpl<PickUpGoodsMapper,PickUpGoods> implements PickUpGoodsService{
}
