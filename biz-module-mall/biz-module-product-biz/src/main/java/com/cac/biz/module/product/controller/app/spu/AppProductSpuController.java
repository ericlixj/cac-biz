package com.cac.biz.module.product.controller.app.spu;

import cn.hutool.core.collection.CollUtil;
import com.cac.biz.framework.common.pojo.CommonResult;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.module.member.api.level.MemberLevelApi;
import com.cac.biz.module.member.api.level.dto.MemberLevelRespDTO;
import com.cac.biz.module.member.api.user.MemberUserApi;
import com.cac.biz.module.member.api.user.dto.MemberUserRespDTO;
import com.cac.biz.module.product.controller.app.spu.vo.AppProductSpuDetailRespVO;
import com.cac.biz.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import com.cac.biz.module.product.controller.app.spu.vo.AppProductSpuRespVO;
import com.cac.biz.module.product.dal.dataobject.sku.ProductSkuDO;
import com.cac.biz.module.product.dal.dataobject.spu.ProductSpuDO;
import com.cac.biz.module.product.enums.spu.ProductSpuStatusEnum;
import com.cac.biz.module.product.service.history.ProductBrowseHistoryService;
import com.cac.biz.module.product.service.sku.ProductSkuService;
import com.cac.biz.module.product.service.spu.ProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.cac.biz.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cac.biz.framework.common.pojo.CommonResult.success;
import static com.cac.biz.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.cac.biz.module.product.enums.ErrorCodeConstants.SPU_NOT_ENABLE;
import static com.cac.biz.module.product.enums.ErrorCodeConstants.SPU_NOT_EXISTS;

@Tag(name = "用户 APP - 商品 SPU")
@RestController
@RequestMapping("/product/spu")
@Validated
public class AppProductSpuController {

    @Resource
    private ProductSpuService productSpuService;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private ProductBrowseHistoryService productBrowseHistoryService;

    @Resource
    private MemberLevelApi memberLevelApi;
    @Resource
    private MemberUserApi memberUserApi;

    @GetMapping("/list-by-ids")
    @Operation(summary = "获得商品 SPU 列表")
    @Parameter(name = "ids", description = "编号列表", required = true)
    public CommonResult<List<AppProductSpuRespVO>> getSpuList(@RequestParam("ids") Set<Long> ids) {
        List<ProductSpuDO> list = productSpuService.getSpuList(ids);
        if (CollUtil.isEmpty(list)) {
            return success(Collections.emptyList());
        }

        // 拼接返回
        list.forEach(spu -> spu.setSalesCount(spu.getSalesCount() + spu.getVirtualSalesCount()));
        List<AppProductSpuRespVO> voList = BeanUtils.toBean(list, AppProductSpuRespVO.class);
        // 处理 vip 价格
        MemberLevelRespDTO memberLevel = getMemberLevel();
        voList.forEach(vo -> vo.setVipPrice(calculateVipPrice(vo.getPrice(), memberLevel)));
        return success(voList);
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品 SPU 分页")
    public CommonResult<PageResult<AppProductSpuRespVO>> getSpuPage(@Valid AppProductSpuPageReqVO pageVO) {
        PageResult<ProductSpuDO> pageResult = productSpuService.getSpuPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty(pageResult.getTotal()));
        }

        // 拼接返回
        pageResult.getList().forEach(spu -> spu.setSalesCount(spu.getSalesCount() + spu.getVirtualSalesCount()));
        PageResult<AppProductSpuRespVO> voPageResult = BeanUtils.toBean(pageResult, AppProductSpuRespVO.class);
        // 处理 vip 价格
        MemberLevelRespDTO memberLevel = getMemberLevel();
        voPageResult.getList().forEach(vo -> vo.setVipPrice(calculateVipPrice(vo.getPrice(), memberLevel)));
        return success(voPageResult);
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得商品 SPU 明细")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<AppProductSpuDetailRespVO> getSpuDetail(@RequestParam("id") Long id) {
        // 获得商品 SPU
        ProductSpuDO spu = productSpuService.getSpu(id);
        if (spu == null) {
            throw exception(SPU_NOT_EXISTS);
        }
        if (!ProductSpuStatusEnum.isEnable(spu.getStatus())) {
            throw exception(SPU_NOT_ENABLE, spu.getName());
        }
        // 获得商品 SKU
        List<ProductSkuDO> skus = productSkuService.getSkuListBySpuId(spu.getId());

        // 增加浏览量
        productSpuService.updateBrowseCount(id, 1);
        // 保存浏览记录
        productBrowseHistoryService.createBrowseHistory(getLoginUserId(), id);

        // 拼接返回
        spu.setSalesCount(spu.getSalesCount() + spu.getVirtualSalesCount());
        AppProductSpuDetailRespVO spuVO = BeanUtils.toBean(spu, AppProductSpuDetailRespVO.class)
                .setSkus(BeanUtils.toBean(skus, AppProductSpuDetailRespVO.Sku.class));
        // 处理 vip 价格
        MemberLevelRespDTO memberLevel = getMemberLevel();
        spuVO.setVipPrice(calculateVipPrice(spuVO.getPrice(), memberLevel));
        return success(spuVO);
    }

    private MemberLevelRespDTO getMemberLevel() {
        Long userId = getLoginUserId();
        if (userId == null) {
            return null;
        }
        MemberUserRespDTO user = memberUserApi.getUser(userId);
        if (user.getLevelId() == null || user.getLevelId() <= 0) {
            return null;
        }
        return memberLevelApi.getMemberLevel(user.getLevelId());
    }

    /**
     * 计算会员 VIP 优惠价格
     *
     * @param price 原价
     * @param memberLevel 会员等级
     * @return 优惠价格
     */
    public Integer calculateVipPrice(Integer price, MemberLevelRespDTO memberLevel) {
        if (memberLevel == null || memberLevel.getDiscountPercent() == null) {
            return 0;
        }
        Integer newPrice = price * memberLevel.getDiscountPercent() / 100;
        return price - newPrice;
    }

}
