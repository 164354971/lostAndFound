package love.lingbao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.lingbao.dto.UserDto;
import love.lingbao.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> findAll();
}
