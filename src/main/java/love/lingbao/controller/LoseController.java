package love.lingbao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.R;
import love.lingbao.entity.lose.LoseGoods;
import love.lingbao.entity.lose.LoseImage;
import love.lingbao.entity.lose.LosePage;
import love.lingbao.entity.lose.LoseTag;
import love.lingbao.service.lose.LoseClassifyService;
import love.lingbao.service.lose.LoseGoodsService;
import love.lingbao.service.UserService;
import love.lingbao.service.lose.LoseImageService;
import love.lingbao.service.lose.LoseTagService;
import love.lingbao.utils.MultiRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/lose")
public class LoseController {

    @Autowired
    private LoseGoodsService loseGoodsService;
    @Autowired
    private LoseImageService loseImageService;
    @Autowired
    private LoseTagService loseTagService;
    @Autowired
    private LoseClassifyService loseClassifyService;
    @Autowired
    private UserService userService;

    /**
     * 失物图片上传到本地
     * @param file 图片文件
     * @return
     */
    @PostMapping ("/uploadImage")
    public String uploadImage(HttpServletRequest request, MultipartFile file) {
        log.info("/lose/uploadImage post -> uploadImage: file = {}; 将图片保存在本地，并返回存储路径", file);
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("static/images")).getPath();//本地资源路径
        try {
            file.transferTo(new File(path, Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String uid = (String) request.getSession().getAttribute("user");
        return "/images/" + uid + "/" + file.getOriginalFilename();//保存成功后返回路径
    }

    /**
     * 添加失物信息
     * @param loseGoods 失物信息体
     * @param urls 上传的图片链接数组
     * @param tags 上传的标签数组
     * @return
     */
    @PostMapping
    public R<String> add(HttpServletRequest request, @MultiRequestBody LoseGoods loseGoods, @MultiRequestBody String[] urls, @MultiRequestBody String[] tags){
        log.info("/lose post -> add: loseGoods = {}, urls = {}, tags = {}; 添加失物信息", loseGoods.toString(), Arrays.toString(urls), Arrays.toString(tags));
        if(loseGoods.getGoodsName().length() > 20)
            return R.error("失物名称长度不可超过20个字符");
        if(loseGoods.getGoodsName().equals(""))
            return R.error("失物名称不能为空");

        //更新时间
        LocalDateTime localDateTime = LocalDateTime.now();
        loseGoods.setCreateTime(localDateTime);
        loseGoods.setUpdateTime(localDateTime);

        //保存失物信息
        loseGoodsService.save(loseGoods);

        //再通过获取刚发布的失物信息得到主键id，分别给图片、标签和分类入表
        LambdaQueryWrapper<LoseGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LoseGoods::getCreateTime, localDateTime).eq(LoseGoods::getUid, request.getSession().getAttribute("user"));
        LoseGoods lose = loseGoodsService.getOne(queryWrapper);

        for (String url : urls) {
            LoseImage loseImage = new LoseImage(0, lose.getId(), url);
            loseImageService.save(loseImage);
        }

        for (String tag : tags) {
            LoseTag loseTag = new LoseTag(0, lose.getId(), tag);
            loseTagService.save(loseTag);
        }
        return R.success("您的" + loseGoods.getGoodsName() + "失物信息已发布！");
    }

    /**
     * 失物信息分页查询
     * @param page 页数
     * @param pageSize 页面大小
     * @param name 查询的字段（模糊查找）
     * @return
     */
    @GetMapping
    public R<List<LosePage>> find(int page, int pageSize, String name){
        log.info("/lose get -> find: page = {}, pageSize = {}, name = {}; 失物分页查询", page, pageSize, name);
        Page<LoseGoods> pageInfo = new Page<>(page, pageSize);
        //1.获取失物信息体的分页
        LambdaQueryWrapper<LoseGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), LoseGoods::getGoodsName, name);
        queryWrapper.like(!StringUtils.isEmpty(name), LoseGoods::getGoodsInfo, name);
        queryWrapper.like(!StringUtils.isEmpty(name), LoseGoods::getGoodsPos, name);
        queryWrapper.orderByDesc(LoseGoods::getUpdateTime);
        loseGoodsService.page(pageInfo, queryWrapper);

        //2.将信息体其他的附带条件一起添加进来，如图片、标签等
        List<LoseGoods> loseGoods = pageInfo.getRecords();
        List<LosePage> losePages = new ArrayList<>();
        for (LoseGoods loseGood : loseGoods) {
            //2.1 new一个losePage，由父类loseGoods转换
            LosePage losePage = (LosePage) loseGood;
            //2.2 获取loseGood 的id
            Integer loseGoodsId = loseGood.getId();
            //2.2.1 获取该id对应的图片信息
            LambdaQueryWrapper<LoseImage> loseImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
            loseImageLambdaQueryWrapper.eq(LoseImage::getLoseGoodsId, loseGoodsId);
            //2.2.2 存进losePage里
            losePage.setLoseImages(loseImageService.list(loseImageLambdaQueryWrapper));

            //2.2.3 获取该id对应的标签信息
            LambdaQueryWrapper<LoseTag> loseTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            loseTagLambdaQueryWrapper.eq(LoseTag::getLoseGoodsId, loseGoodsId);
            //2.2.4 存进losePage里
            losePage.setLoseTags(loseTagService.list(loseTagLambdaQueryWrapper));

            //2.3 losePage放进losePages里
            losePages.add(losePage);
        }

        return R.success(losePages);
    }

    /*@DeleteMapping
    public R<String> remove(){

    }*/
}
