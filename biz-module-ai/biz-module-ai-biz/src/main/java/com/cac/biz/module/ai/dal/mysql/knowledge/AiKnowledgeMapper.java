package com.cac.biz.module.ai.dal.mysql.knowledge;

import com.cac.biz.framework.common.enums.CommonStatusEnum;
import com.cac.biz.framework.common.pojo.PageParam;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.mybatis.core.mapper.BaseMapperX;
import com.cac.biz.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cac.biz.module.ai.dal.dataobject.knowledge.AiKnowledgeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI 知识库基础信息 Mapper
 *
 * @author xiaoxin
 */
@Mapper
public interface AiKnowledgeMapper extends BaseMapperX<AiKnowledgeDO> {

    default PageResult<AiKnowledgeDO> selectPageByMy(Long userId, PageParam pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<AiKnowledgeDO>()
                .eq(AiKnowledgeDO::getUserId, userId)
                .eq(AiKnowledgeDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByDesc(AiKnowledgeDO::getId));
    }
}
