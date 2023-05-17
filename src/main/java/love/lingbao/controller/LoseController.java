package love.lingbao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.R;
import love.lingbao.dto.LosePage;
import love.lingbao.entity.lose.LoseGoods;
import love.lingbao.entity.lose.LoseImage;
import love.lingbao.vo.lose.LoseOneVO;
import love.lingbao.entity.lose.LoseTag;
import love.lingbao.service.lose.LoseClassifyService;
import love.lingbao.service.lose.LoseGoodsService;
import love.lingbao.service.UserService;
import love.lingbao.service.lose.LoseImageService;
import love.lingbao.service.lose.LoseTagService;
import love.lingbao.utils.MultiRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/lose")
@CrossOrigin
@ResponseBody
public class LoseController {

    @Value("${lingbao.address-dev}")
    private String address;

    @Autowired
    private HttpSession httpSession;

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
     * 添加失物信息
     * @param loseGoods 失物信息体
     * @param urls 上传的图片链接数组
     * @param tags 上传的标签数组
     * @return
     */
    @PostMapping("/add")
    public R<String> add(HttpServletRequest request, HttpServletResponse response, @MultiRequestBody LoseGoods loseGoods, @MultiRequestBody String[] urls, @MultiRequestBody String[] tags){
        //response.setHeader("Access-Control-Allow-Origin", address);
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
        queryWrapper.eq(LoseGoods::getCreateTime, localDateTime).eq(LoseGoods::getUid, httpSession.getAttribute("user"));
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
     * @param losePage 页数，页大小，查找字符串
     * @return
     */
    @PostMapping("/page")
    public R<Page<LoseOneVO>> page(HttpServletRequest request, HttpServletResponse response, @RequestBody LosePage losePage){
        //response.setHeader("Access-Control-Allow-Origin", address);
        log.info("/lose post -> page: losePage = {}; 失物分页查询", losePage);
        log.info(String.valueOf((Integer) httpSession.getAttribute("user")));
        Page<LoseGoods> pageInfo = new Page<>(losePage.getPage(), losePage.getPageSize());
        //1.获取失物信息体的分页
        LambdaQueryWrapper<LoseGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(losePage.getName()), LoseGoods::getGoodsName, losePage.getName());
        queryWrapper.like(!StringUtils.isEmpty(losePage.getName()), LoseGoods::getGoodsInfo, losePage.getName());
        queryWrapper.like(!StringUtils.isEmpty(losePage.getName()), LoseGoods::getGoodsPos, losePage.getName());
        queryWrapper.orderByDesc(LoseGoods::getUpdateTime);
        loseGoodsService.page(pageInfo, queryWrapper);
        //2.将信息体其他的附带条件一起添加进来，如图片、标签等
        List<LoseGoods> loseGoodsList = pageInfo.getRecords();
        log.info(loseGoodsList.toString());
        List<LoseOneVO> loseOneVOlist = new ArrayList<>();
        for (LoseGoods loseGoods : loseGoodsList) {
            //2.1 new一个loseOne，并将主信息体loseGoods放进去
            LoseOneVO loseOne = new LoseOneVO();
            loseOne.setLoseGoods(loseGoods);
            //2.2 获取loseGood 的id
            Integer loseGoodsId = loseOne.getLoseGoods().getId();
            loseOne.setId(loseGoodsId);
            //2.2.1 获取该id对应的图片信息
            LambdaQueryWrapper<LoseImage> loseImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
            loseImageLambdaQueryWrapper.eq(LoseImage::getLoseGoodsId, loseGoodsId);
            //2.2.2 存进loseOne里
            loseOne.setLoseImages(loseImageService.list(loseImageLambdaQueryWrapper));
            //2.2.3 获取该id对应的标签信息
            LambdaQueryWrapper<LoseTag> loseTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            loseTagLambdaQueryWrapper.eq(LoseTag::getLoseGoodsId, loseGoodsId);
            //2.2.4 存进loseOne里
            loseOne.setLoseTags(loseTagService.list(loseTagLambdaQueryWrapper));

            //2.3 losePage放进loseOneVOlist里
            loseOneVOlist.add(loseOne);
        }
        Page<LoseOneVO> loseOneVOPage = new Page<>(losePage.getPage(), loseOneVOlist.size());
        loseOneVOPage.setRecords(loseOneVOlist);
        return R.success(loseOneVOPage);
    }

    /**
     * 失物信息删除
     * @param loseGoods 失物信息体
     * @return
     */
    @DeleteMapping
    public R<String> remove(HttpServletRequest request, @RequestBody LoseGoods loseGoods){
        log.info("/lose delete -> remove: loseGoods = {}; 失物信息删除", loseGoods);
        //如果删除的失物信息对应的用户ID与当前登录用户的ID不同，抛异常
        if(request.getSession().getAttribute("user") != loseGoods.getUid()){
            throw new RuntimeException("用户权限异常");
        }
        //1.匹配成功后开始删除，首先删除附表里的对应数据
        //1.1 获得失物信息体id
        Integer loseGoodsId = loseGoods.getId();
        //1.1.1 获取失物信息体对应的标签们
        LambdaQueryWrapper<LoseTag> loseTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        loseTagLambdaQueryWrapper.eq(LoseTag::getLoseGoodsId, loseGoodsId);
        loseTagService.remove(loseTagLambdaQueryWrapper);
        //1.1.2 获取失物信息体对应的图片们
        LambdaQueryWrapper<LoseImage> loseImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        loseImageLambdaQueryWrapper.eq(LoseImage::getLoseGoodsId, loseGoodsId);
        loseImageService.remove(loseImageLambdaQueryWrapper);

        //1.2 删除失物信息体本身
        loseGoodsService.removeById(loseGoodsId);
        return R.success("失物 " + loseGoods.getGoodsName() + " 删除成功");
    }
}
