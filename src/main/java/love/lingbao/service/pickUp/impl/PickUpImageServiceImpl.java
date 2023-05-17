package love.lingbao.service.pickUp.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.pickUp.PickUpImage;
import love.lingbao.mapper.PickUp.PickUpImageMapper;
import love.lingbao.service.pickUp.PickUpImageService;
import org.springframework.stereotype.Service;

@Service
public class PickUpImageServiceImpl extends ServiceImpl<PickUpImageMapper, PickUpImage> implements PickUpImageService {
}
