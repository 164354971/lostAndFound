package love.lingbao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    List<User> findAll();
}
