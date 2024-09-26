package com.cac.biz.module.bpm.framework.flowable.core.candidate.strategy;

import com.cac.biz.framework.common.util.string.StrUtils;
import com.cac.biz.module.bpm.dal.dataobject.definition.BpmUserGroupDO;
import com.cac.biz.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.cac.biz.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.cac.biz.module.bpm.service.definition.BpmUserGroupService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.cac.biz.framework.common.util.collection.CollectionUtils.convertSetByFlatMap;

/**
 * 用户组 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateGroupStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private BpmUserGroupService userGroupService;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER_GROUP;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> groupIds = StrUtils.splitToLongSet(param);
        userGroupService.getUserGroupList(groupIds);
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        Set<Long> groupIds = StrUtils.splitToLongSet(param);
        List<BpmUserGroupDO> groups = userGroupService.getUserGroupList(groupIds);
        return convertSetByFlatMap(groups, BpmUserGroupDO::getUserIds, Collection::stream);
    }

}