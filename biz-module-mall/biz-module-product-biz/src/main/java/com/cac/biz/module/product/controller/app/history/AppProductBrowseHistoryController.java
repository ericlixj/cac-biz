package com.cac.biz.module.product.controller.app.history;

import cn.hutool.core.collection.CollUtil;
import com.cac.biz.framework.common.pojo.CommonResult;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.framework.security.core.annotations.PreAuthenticated;
import com.cac.biz.module.product.controller.admin.history.vo.ProductBrowseHistoryPageReqVO;
import com.cac.biz.module.product.controller.app.history.vo.AppProductBrowseHistoryDeleteReqVO;
import com.cac.biz.module.product.controller.app.history.vo.AppProductBrowseHistoryPageReqVO;
import com.cac.biz.module.product.controller.app.history.vo.AppProductBrowseHistoryRespVO;
import com.cac.biz.module.product.dal.dataobject.history.ProductBrowseHistoryDO;
import com.cac.biz.module.product.dal.dataobject.spu.ProductSpuDO;
import com.cac.biz.module.product.service.history.ProductBrowseHistoryService;
import com.cac.biz.module.product.service.spu.ProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.cac.biz.framework.common.pojo.CommonResult.success;
import static com.cac.biz.framework.common.util.collection.CollectionUtils.convertMap;
import static com.cac.biz.framework.common.util.collection.CollectionUtils.convertSet;
import static com.cac.biz.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 商品浏览记录")
@RestController
@RequestMapping("/product/browse-history")
public class AppProductBrowseHistoryController {

    @Resource
    private ProductBrowseHistoryService productBrowseHistoryService;
    @Resource
    private ProductSpuService productSpuService;

    @DeleteMapping(value = "/delete")
    @Operation(summary = "删除商品浏览记录")
    @PreAuthenticated
    public CommonResult<Boolean> deleteBrowseHistory(@RequestBody @Valid AppProductBrowseHistoryDeleteReqVO reqVO) {
        productBrowseHistoryService.hideUserBrowseHistory(getLoginUserId(), reqVO.getSpuIds());
        return success(Boolean.TRUE);
    }

    @DeleteMapping(value = "/clean")
    @Operation(summary = "清空商品浏览记录")
    @PreAuthenticated
    public CommonResult<Boolean> deleteBrowseHistory() {
        productBrowseHistoryService.hideUserBrowseHistory(getLoginUserId(), null);
        return success(Boolean.TRUE);
    }

    @GetMapping(value = "/page")
    @Operation(summary = "获得商品浏览记录分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppProductBrowseHistoryRespVO>> getBrowseHistoryPage(AppProductBrowseHistoryPageReqVO reqVO) {
        ProductBrowseHistoryPageReqVO pageReqVO = BeanUtils.toBean(reqVO, ProductBrowseHistoryPageReqVO.class)
                .setUserId(getLoginUserId())
                .setUserDeleted(false); // 排除用户已删除的（隐藏的）
        PageResult<ProductBrowseHistoryDO> pageResult = productBrowseHistoryService.getBrowseHistoryPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        // 得到商品 spu 信息
        Set<Long> spuIds = convertSet(pageResult.getList(), ProductBrowseHistoryDO::getSpuId);
        Map<Long, ProductSpuDO> spuMap = convertMap(productSpuService.getSpuList(spuIds), ProductSpuDO::getId);
        return success(BeanUtils.toBean(pageResult, AppProductBrowseHistoryRespVO.class,
                vo -> Optional.ofNullable(spuMap.get(vo.getSpuId()))
                        .ifPresent(spu -> vo.setSpuName(spu.getName()).setPicUrl(spu.getPicUrl()).setPrice(spu.getPrice()))));
    }

}
