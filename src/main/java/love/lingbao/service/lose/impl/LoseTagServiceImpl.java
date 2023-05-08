package love.lingbao.service.lose.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.lose.LoseTag;
import love.lingbao.mapper.lose.LoseTagMapper;
import love.lingbao.service.lose.LoseTagService;
import org.springframework.stereotype.Service;

@Service
public class LoseTagServiceImpl extends ServiceImpl<LoseTagMapper, LoseTag> implements LoseTagService {
}
