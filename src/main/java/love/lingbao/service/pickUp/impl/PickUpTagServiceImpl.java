package love.lingbao.service.pickUp.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.pickUp.PickUpTag;
import love.lingbao.mapper.PickUp.PickUpTagMapper;
import love.lingbao.service.pickUp.PickUpTagService;
import org.springframework.stereotype.Service;

@Service
public class PickUpTagServiceImpl extends ServiceImpl<PickUpTagMapper, PickUpTag> implements PickUpTagService {
}
