package love.lingbao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.R;
import love.lingbao.service.lose.LoseGoodsService;
import love.lingbao.service.pickUp.PickUpGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
@CrossOrigin
@ResponseBody
public class CommonController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private LoseGoodsService loseGoodsService;
    @Autowired
    private PickUpGoodsService pickUpGoodsService;

    /**
     * 图片上传到本地
     * @param file 图片文件
     * @return
     */
    @PostMapping("/uploadImage")
    public String uploadImage(MultipartFile file) {
        log.info("/common/uploadImage post -> uploadImage: file = {}; 将图片保存在本地，并返回存储路径", file);
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("static/images")).getPath();//本地资源路径
        String uid = (String) httpSession.getAttribute("user");
        path = path + "/" + uid + "/";
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString() + suffix;
        try {
            file.transferTo(new File(path, Objects.requireNonNull(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path + fileName;//保存成功后返回路径
    }

    /**
     * 传
     * @return
     */
    @PostMapping("/getLoseGoods")
    public void getLoseGoods() {
        log.info("/common/getLoseGoods post -> getLoseGoods; 将loseGoods的部分数据传递");


        //return R.success();
    }

    /**
     * 模糊查询失物和拾物中的信息，比对成功后排序返回
     * @return
     */
    @GetMapping("/values")
    public R<String[]> getValues(){
        /*if(value.length() > 0 && value.charAt(value.length() - 1) == '='){
            value = value.substring(0, value.length() - 1);
        }
        log.info(value);*/
        String[] ss = new String[]{
                "三星",
                "三星手机",
                "三星S7"
        };
        return R.success(ss);
    }
}
