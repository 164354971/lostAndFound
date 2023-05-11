package love.lingbao.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import love.lingbao.entity.User;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends User {
    private String code;        //前端验证码

}
