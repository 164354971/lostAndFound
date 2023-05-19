package love.lingbao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.R;
import love.lingbao.dto.LosePage;
import love.lingbao.dto.PickUpPage;
import love.lingbao.entity.User;
import love.lingbao.entity.pickUp.PickUpGoods;
import love.lingbao.entity.pickUp.PickUpImage;
import love.lingbao.entity.pickUp.PickUpTag;
import love.lingbao.service.UserService;
import love.lingbao.vo.pickUp.PickUpOneVO;
import love.lingbao.service.pickUp.PickUpClassifyService;
import love.lingbao.service.pickUp.PickUpGoodsService;
import love.lingbao.service.pickUp.PickUpImageService;
import love.lingbao.service.pickUp.PickUpTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pickUp")
@CrossOrigin
@ResponseBody
public class PickUpController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private PickUpGoodsService pickUpGoodsService;
    @Autowired
    private PickUpImageService pickUpImageService;
    @Autowired
    private PickUpTagService pickUpTagService;
    @Autowired
    private PickUpClassifyService pickUpClassifyService;

    @Autowired
    private UserService userService;

    /**
     * 拾物信息分页查询
     * @param pickUpPage 页数，页大小，查找字符串
     * @return
     */
    @PostMapping("/page")
    public R<Page<PickUpOneVO>> page(@RequestBody PickUpPage pickUpPage){
        //response.setHeader("Access-Control-Allow-Origin", address);
        log.info("/pickUp post -> page: pickUpPage = {};拾物分页查询", pickUpPage);
        log.info(String.valueOf((Integer) httpSession.getAttribute("user")));
        Page<PickUpGoods> pageInfo = new Page<>(pickUpPage.getPage(), pickUpPage.getPageSize());
        //1.获取拾物信息体的分页
        LambdaQueryWrapper<PickUpGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(pickUpPage.getName()), PickUpGoods::getGoodsName, pickUpPage.getName());
        queryWrapper.like(!StringUtils.isEmpty(pickUpPage.getName()), PickUpGoods::getGoodsInfo, pickUpPage.getName());
        queryWrapper.like(!StringUtils.isEmpty(pickUpPage.getName()), PickUpGoods::getGoodsPos, pickUpPage.getName());
        queryWrapper.orderByDesc(PickUpGoods::getUpdateTime);
        pickUpGoodsService.page(pageInfo, queryWrapper);

        //2.将信息体其他的附带条件一起添加进来，如图片、标签等
        List<PickUpGoods> pickUpGoodsList = pageInfo.getRecords();
        List<PickUpOneVO> pickUpOneVOList = new ArrayList<>();
        for (PickUpGoods pickUpGoods : pickUpGoodsList) {
            //2.1 new一个pickUpOne，并将主信息体PickUpGoods放进去
            PickUpOneVO pickUpOne = new PickUpOneVO();
            pickUpOne.setPickUpGoods(pickUpGoods);
            //2.2 pickUpGoods 的id
            Integer pickUpGoodsId = pickUpOne.getPickUpGoods().getId();
            pickUpOne.setId(pickUpGoodsId);
            //2.2.1 获取该id对应的图片信息
            LambdaQueryWrapper<PickUpImage> pickUpImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pickUpImageLambdaQueryWrapper.eq(PickUpImage::getPickUpGoodsId, pickUpGoodsId);
            //2.2.2 存进pickUpOne里
            pickUpOne.setPickUpImages(pickUpImageService.list(pickUpImageLambdaQueryWrapper));
            //2.2.3 获取该id对应的标签信息
            LambdaQueryWrapper<PickUpTag> pickUpTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pickUpTagLambdaQueryWrapper.eq(PickUpTag::getPickUpGoodsId, pickUpGoodsId);
            //2.2.4 存进pickUpOne里
            pickUpOne.setPickUpTags(pickUpTagService.list(pickUpTagLambdaQueryWrapper));
            //2.2.5 将用户信息加入
            User user = userService.getById(pickUpOne.getPickUpGoods().getUid());
            pickUpOne.setUser(user);

            //2.3 pickUpOne放进pickUpOneVOList里
            pickUpOneVOList.add(pickUpOne);
        }
        Page<PickUpOneVO> pickUpOneVOPage = new Page<>(pickUpPage.getPage(), pickUpOneVOList.size());
        pickUpOneVOPage.setRecords(pickUpOneVOList);
        return R.success(pickUpOneVOPage);
    }
}
