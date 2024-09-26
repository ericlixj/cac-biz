package com.cac.biz.module.trade.controller.app.brokerage;

import com.cac.biz.framework.common.pojo.CommonResult;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.security.core.annotations.PreAuthenticated;
import com.cac.biz.module.trade.controller.app.brokerage.vo.withdraw.AppBrokerageWithdrawCreateReqVO;
import com.cac.biz.module.trade.controller.app.brokerage.vo.withdraw.AppBrokerageWithdrawPageReqVO;
import com.cac.biz.module.trade.controller.app.brokerage.vo.withdraw.AppBrokerageWithdrawRespVO;
import com.cac.biz.module.trade.convert.brokerage.BrokerageWithdrawConvert;
import com.cac.biz.module.trade.dal.dataobject.brokerage.BrokerageWithdrawDO;
import com.cac.biz.module.trade.service.brokerage.BrokerageWithdrawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.cac.biz.framework.common.pojo.CommonResult.success;
import static com.cac.biz.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 分销提现")
@RestController
@RequestMapping("/trade/brokerage-withdraw")
@Validated
@Slf4j
public class AppBrokerageWithdrawController {

    @Resource
    private BrokerageWithdrawService brokerageWithdrawService;

    @GetMapping("/page")
    @Operation(summary = "获得分销提现分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppBrokerageWithdrawRespVO>> getBrokerageWithdrawPage(AppBrokerageWithdrawPageReqVO pageReqVO) {
        PageResult<BrokerageWithdrawDO> pageResult = brokerageWithdrawService.getBrokerageWithdrawPage(
                BrokerageWithdrawConvert.INSTANCE.convert(pageReqVO, getLoginUserId()));
        return success(BrokerageWithdrawConvert.INSTANCE.convertPage03(pageResult));
    }

    @PostMapping("/create")
    @Operation(summary = "创建分销提现")
    @PreAuthenticated
    public CommonResult<Long> createBrokerageWithdraw(@RequestBody @Valid AppBrokerageWithdrawCreateReqVO createReqVO) {
        return success(brokerageWithdrawService.createBrokerageWithdraw(getLoginUserId(), createReqVO));
    }

}
