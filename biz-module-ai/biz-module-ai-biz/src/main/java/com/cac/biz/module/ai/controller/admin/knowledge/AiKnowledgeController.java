package com.cac.biz.module.ai.controller.admin.knowledge;

import com.cac.biz.framework.common.pojo.CommonResult;
import com.cac.biz.framework.common.pojo.PageParam;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.module.ai.controller.admin.knowledge.vo.knowledge.AiKnowledgeCreateMyReqVO;
import com.cac.biz.module.ai.controller.admin.knowledge.vo.knowledge.AiKnowledgeRespVO;
import com.cac.biz.module.ai.controller.admin.knowledge.vo.knowledge.AiKnowledgeUpdateMyReqVO;
import com.cac.biz.module.ai.dal.dataobject.knowledge.AiKnowledgeDO;
import com.cac.biz.module.ai.service.knowledge.AiKnowledgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.cac.biz.framework.common.pojo.CommonResult.success;
import static com.cac.biz.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - AI 知识库")
@RestController
@RequestMapping("/ai/knowledge")
@Validated
public class AiKnowledgeController {

    @Resource
    private AiKnowledgeService knowledgeService;

    @GetMapping("/my-page")
    @Operation(summary = "获取【我的】知识库分页")
    public CommonResult<PageResult<AiKnowledgeRespVO>> getKnowledgePageMy(@Validated PageParam pageReqVO) {
        PageResult<AiKnowledgeDO> pageResult = knowledgeService.getKnowledgePageMy(getLoginUserId(), pageReqVO);
        return success(BeanUtils.toBean(pageResult, AiKnowledgeRespVO.class));
    }

    @PostMapping("/create-my")
    @Operation(summary = "创建【我的】知识库")
    public CommonResult<Long> createKnowledgeMy(@RequestBody @Valid AiKnowledgeCreateMyReqVO createReqVO) {
        return success(knowledgeService.createKnowledgeMy(createReqVO, getLoginUserId()));
    }

    @PutMapping("/update-my")
    @Operation(summary = "更新【我的】知识库")
    public CommonResult<Boolean> updateKnowledgeMy(@RequestBody @Valid AiKnowledgeUpdateMyReqVO updateReqVO) {
        knowledgeService.updateKnowledgeMy(updateReqVO, getLoginUserId());
        return success(true);
    }

}
