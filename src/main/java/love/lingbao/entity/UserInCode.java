package love.lingbao.entity;


import lombok.Data;

@Data
public class UserInCode extends User{
    private String code;        //前端验证码

}
