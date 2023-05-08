package love.lingbao.service.lose.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.lose.LoseImage;
import love.lingbao.mapper.lose.LoseImageMapper;
import love.lingbao.service.lose.LoseImageService;
import org.springframework.stereotype.Service;

@Service
public class LoseImageServiceImpl extends ServiceImpl<LoseImageMapper, LoseImage> implements LoseImageService {
}
