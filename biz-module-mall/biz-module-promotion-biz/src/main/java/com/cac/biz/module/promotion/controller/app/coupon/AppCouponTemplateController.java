package com.cac.biz.module.promotion.controller.app.coupon;

import com.cac.biz.framework.common.pojo.CommonResult;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.framework.common.util.object.ObjectUtils;
import com.cac.biz.module.product.api.spu.ProductSpuApi;
import com.cac.biz.module.product.api.spu.dto.ProductSpuRespDTO;
import com.cac.biz.module.promotion.controller.app.coupon.vo.template.AppCouponTemplatePageReqVO;
import com.cac.biz.module.promotion.controller.app.coupon.vo.template.AppCouponTemplateRespVO;
import com.cac.biz.module.promotion.convert.coupon.CouponTemplateConvert;
import com.cac.biz.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import com.cac.biz.module.promotion.enums.common.PromotionProductScopeEnum;
import com.cac.biz.module.promotion.enums.coupon.CouponTakeTypeEnum;
import com.cac.biz.module.promotion.service.coupon.CouponService;
import com.cac.biz.module.promotion.service.coupon.CouponTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

import static com.cac.biz.framework.common.pojo.CommonResult.success;
import static com.cac.biz.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static java.util.Collections.singletonList;

@Tag(name = "用户 App - 优惠劵模板")
@RestController
@RequestMapping("/promotion/coupon-template")
@Validated
public class AppCouponTemplateController {

    @Resource
    private CouponTemplateService couponTemplateService;
    @Resource
    private CouponService couponService;

    @Resource
    private ProductSpuApi productSpuApi;

    @GetMapping("/get")
    @Operation(summary = "获得优惠劵模版")
    @Parameter(name = "id", description = "优惠券模板编号", required = true, example = "1024")
    public CommonResult<AppCouponTemplateRespVO> getCouponTemplate(Long id) {
        CouponTemplateDO template = couponTemplateService.getCouponTemplate(id);
        if (template == null) {
            return success(null);
        }
        // 处理是否可领取
        Map<Long, Boolean> canCanTakeMap = couponService.getUserCanCanTakeMap(getLoginUserId(), singletonList(template));
        return success(BeanUtils.toBean(template, AppCouponTemplateRespVO.class)
                .setCanTake(canCanTakeMap.get(template.getId())));
    }

    @GetMapping("/list")
    @Operation(summary = "获得优惠劵模版列表")
    @Parameters({
            @Parameter(name = "spuId", description = "商品 SPU 编号"), // 目前主要给商品详情使用
            @Parameter(name = "productScope", description = "使用类型"),
            @Parameter(name = "count", description = "数量", required = true)
    })
    public CommonResult<List<AppCouponTemplateRespVO>> getCouponTemplateList(
            @RequestParam(value = "spuId", required = false) Long spuId,
            @RequestParam(value = "productScope", required = false) Integer productScope,
            @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        // 1.1 处理查询条件：商品范围编号
        Long productScopeValue = getProductScopeValue(productScope, spuId);
        // 1.2 处理查询条件：领取方式 = 直接领取
        List<Integer> canTakeTypes = singletonList(CouponTakeTypeEnum.USER.getValue());

        // 2. 查询
        List<CouponTemplateDO> list = couponTemplateService.getCouponTemplateList(canTakeTypes, productScope,
                productScopeValue, count);

        // 3.1 领取数量
        Map<Long, Boolean> canCanTakeMap = couponService.getUserCanCanTakeMap(getLoginUserId(), list);
        // 3.2 拼接返回
        return success(CouponTemplateConvert.INSTANCE.convertAppList(list, canCanTakeMap));
    }

    @GetMapping("/list-by-ids")
    @Operation(summary = "获得优惠劵模版列表")
    @Parameter(name = "ids", description = "优惠券模板编号列表")
    public CommonResult<List<AppCouponTemplateRespVO>> getCouponTemplateList(
            @RequestParam(value = "ids", required = false) Set<Long> ids) {
        // 1. 查询
        List<CouponTemplateDO> list = couponTemplateService.getCouponTemplateList(ids);

        // 2.1 领取数量
        Map<Long, Boolean> canCanTakeMap = couponService.getUserCanCanTakeMap(getLoginUserId(), list);
        // 2.2 拼接返回
        return success(CouponTemplateConvert.INSTANCE.convertAppList(list, canCanTakeMap));
    }

    @GetMapping("/page")
    @Operation(summary = "获得优惠劵模版分页")
    public CommonResult<PageResult<AppCouponTemplateRespVO>> getCouponTemplatePage(AppCouponTemplatePageReqVO pageReqVO) {
        // 1.1 处理查询条件：商品范围编号
        Long productScopeValue = getProductScopeValue(pageReqVO.getProductScope(), pageReqVO.getSpuId());
        // 1.2 处理查询条件：领取方式 = 直接领取
        List<Integer> canTakeTypes = singletonList(CouponTakeTypeEnum.USER.getValue());

        // 2. 分页查询
        PageResult<CouponTemplateDO> pageResult = couponTemplateService.getCouponTemplatePage(
                CouponTemplateConvert.INSTANCE.convert(pageReqVO, canTakeTypes, pageReqVO.getProductScope(), productScopeValue));

        // 3.1 领取数量
        Map<Long, Boolean> canCanTakeMap = couponService.getUserCanCanTakeMap(getLoginUserId(), pageResult.getList());
        // 3.2 拼接返回
        return success(CouponTemplateConvert.INSTANCE.convertAppPage(pageResult, canCanTakeMap));
    }

    /**
     * 获得商品的使用范围编号
     *
     * @param productScope 商品范围
     * @param spuId        商品 SPU 编号
     * @return 商品范围编号
     */
    private Long getProductScopeValue(Integer productScope, Long spuId) {
        // 通用券：没有商品范围
        if (ObjectUtils.equalsAny(productScope, PromotionProductScopeEnum.ALL.getScope(), null)) {
            return null;
        }
        // 品类券：查询商品的品类编号
        if (Objects.equals(productScope, PromotionProductScopeEnum.CATEGORY.getScope()) && spuId != null) {
            ProductSpuRespDTO spu = productSpuApi.getSpu(spuId);
            return spu != null ? spu.getCategoryId() : null;
        }
        // 商品卷：直接返回
        return spuId;
    }

}
