package com.cac.biz.module.promotion.job.coupon;

import cn.hutool.core.util.StrUtil;
import com.cac.biz.framework.quartz.core.handler.JobHandler;
import com.cac.biz.framework.tenant.core.job.TenantJob;
import com.cac.biz.module.promotion.service.coupon.CouponService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 优惠券过期 Job
 *
 * @author owen
 */
@Component
public class CouponExpireJob implements JobHandler {

    @Resource
    private CouponService couponService;

    @Override
    @TenantJob
    public String execute(String param) {
        int count = couponService.expireCoupon();
        return StrUtil.format("过期优惠券 {} 个", count);
    }

}
