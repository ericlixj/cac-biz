package com.cac.biz.module.member.api.point;

import cn.hutool.core.lang.Assert;
import com.cac.biz.module.member.enums.point.MemberPointBizTypeEnum;
import com.cac.biz.module.member.service.point.MemberPointRecordService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static com.cac.biz.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cac.biz.module.member.enums.ErrorCodeConstants.POINT_RECORD_BIZ_NOT_SUPPORT;

/**
 * 用户积分的 API 实现类
 *
 * @author owen
 */
@Service
@Validated
public class MemberPointApiImpl implements MemberPointApi {

    @Resource
    private MemberPointRecordService memberPointRecordService;

    @Override
    public void addPoint(Long userId, Integer point, Integer bizType, String bizId) {
        Assert.isTrue(point > 0);
        MemberPointBizTypeEnum bizTypeEnum = MemberPointBizTypeEnum.getByType(bizType);
        if (bizTypeEnum == null) {
            throw exception(POINT_RECORD_BIZ_NOT_SUPPORT);
        }
        memberPointRecordService.createPointRecord(userId, point, bizTypeEnum, bizId);
    }

    @Override
    public void reducePoint(Long userId, Integer point, Integer bizType, String bizId) {
        Assert.isTrue(point > 0);
        MemberPointBizTypeEnum bizTypeEnum = MemberPointBizTypeEnum.getByType(bizType);
        if (bizTypeEnum == null) {
            throw exception(POINT_RECORD_BIZ_NOT_SUPPORT);
        }
        memberPointRecordService.createPointRecord(userId, -point, bizTypeEnum, bizId);
    }

}
