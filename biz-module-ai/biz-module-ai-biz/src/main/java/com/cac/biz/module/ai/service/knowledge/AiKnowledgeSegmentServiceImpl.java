package com.cac.biz.module.ai.service.knowledge;

import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.module.ai.controller.admin.knowledge.vo.segment.AiKnowledgeSegmentPageReqVO;
import com.cac.biz.module.ai.controller.admin.knowledge.vo.segment.AiKnowledgeSegmentUpdateReqVO;
import com.cac.biz.module.ai.controller.admin.knowledge.vo.segment.AiKnowledgeSegmentUpdateStatusReqVO;
import com.cac.biz.module.ai.dal.dataobject.knowledge.AiKnowledgeSegmentDO;
import com.cac.biz.module.ai.dal.mysql.knowledge.AiKnowledgeSegmentMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AI 知识库分片 Service 实现类
 *
 * @author xiaoxin
 */
@Service
@Slf4j
public class AiKnowledgeSegmentServiceImpl implements AiKnowledgeSegmentService {

    @Resource
    private AiKnowledgeSegmentMapper segmentMapper;

    @Override
    public PageResult<AiKnowledgeSegmentDO> getKnowledgeSegmentPage(AiKnowledgeSegmentPageReqVO pageReqVO) {
        return segmentMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateKnowledgeSegment(AiKnowledgeSegmentUpdateReqVO reqVO) {
        segmentMapper.updateById(BeanUtils.toBean(reqVO, AiKnowledgeSegmentDO.class));
        // TODO @xin 重新向量化
    }

    @Override
    public void updateKnowledgeSegmentStatus(AiKnowledgeSegmentUpdateStatusReqVO reqVO) {
        segmentMapper.updateById(BeanUtils.toBean(reqVO, AiKnowledgeSegmentDO.class));
        // TODO @xin 1.禁用删除向量 2.启用重新向量化
    }
}
