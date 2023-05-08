package love.lingbao.service.lose.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.entity.lose.LoseClassify;
import love.lingbao.mapper.lose.LoseClassifyMapper;
import love.lingbao.service.lose.LoseClassifyService;
import org.springframework.stereotype.Service;

@Service
public class LoseClassifyServiceImpl extends ServiceImpl<LoseClassifyMapper, LoseClassify> implements LoseClassifyService {
}
