package love.lingbao.service.pickUp.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.pickUp.PickUpClassify;
import love.lingbao.mapper.PickUp.PickUpClassifyMapper;
import love.lingbao.service.pickUp.PickUpClassifyService;
import org.springframework.stereotype.Service;

@Service
public class PickUpClassifyServiceImpl extends ServiceImpl<PickUpClassifyMapper, PickUpClassify> implements PickUpClassifyService {
}
