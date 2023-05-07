package love.lingbao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.R;
import love.lingbao.entity.LoseGoods;
import love.lingbao.service.LoseGoodsService;
import love.lingbao.utils.MultiRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lose")
public class LoseController {

    @Autowired
    private LoseGoodsService loseGoodsService;

    /**
     * 添加失物信息
     * @param loseGoods 失物信息体
     * @return
     */
    @PostMapping
    public R<String> add(@MultiRequestBody LoseGoods loseGoods){
        log.info("/lose post -> add: loseGoods = {}; 添加失物信息", loseGoods.toString());
        if(loseGoods.getGoodsName().length() > 20)return R.error("失物名称长度不可超过20个字符");
        if(loseGoods.getGoodsName().equals(""))return R.error("失物名称不能为空");
        //更新时间
        loseGoods.setCreateTime(LocalDateTime.now());
        loseGoods.setUpdateTime(LocalDateTime.now());;
        //保存失物信息
        loseGoodsService.save(loseGoods);
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
    public R<Page<LoseGoods>> find(int page, int pageSize, String name){
        log.info("/lose get -> find: page = {}, pageSize = {}, name = {}; 失物分页查询", page, pageSize, name);
        Page<LoseGoods> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<LoseGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), LoseGoods::getGoodsName, name);
        queryWrapper.like(!StringUtils.isEmpty(name), LoseGoods::getGoodsInfo, name);
        queryWrapper.like(!StringUtils.isEmpty(name), LoseGoods::getGoodsPos, name);
        queryWrapper.orderByDesc(LoseGoods::getUpdateTime);
        loseGoodsService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /*@DeleteMapping
    public R<String> remove(){

    }*/
}
