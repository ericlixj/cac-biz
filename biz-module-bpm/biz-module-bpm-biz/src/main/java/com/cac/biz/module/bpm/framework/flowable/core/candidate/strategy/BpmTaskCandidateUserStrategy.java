package com.cac.biz.module.bpm.framework.flowable.core.candidate.strategy;

import com.cac.biz.framework.common.util.string.StrUtils;
import com.cac.biz.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.cac.biz.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.cac.biz.module.system.api.user.AdminUserApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 用户 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateUserStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER;
    }

    @Override
    public void validateParam(String param) {
        adminUserApi.validateUserList(StrUtils.splitToLongSet(param));
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        return StrUtils.splitToLongSet(param);
    }

}