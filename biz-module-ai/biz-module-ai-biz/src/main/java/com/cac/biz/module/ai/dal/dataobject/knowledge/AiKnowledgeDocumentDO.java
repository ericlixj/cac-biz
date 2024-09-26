package com.cac.biz.module.ai.dal.dataobject.knowledge;

import com.cac.biz.framework.common.enums.CommonStatusEnum;
import com.cac.biz.framework.mybatis.core.dataobject.BaseDO;
import com.cac.biz.module.ai.enums.knowledge.AiKnowledgeDocumentStatusEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * AI 知识库-文档 DO
 *
 * @author xiaoxin
 */
@TableName(value = "ai_knowledge_document")
@Data
public class AiKnowledgeDocumentDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 知识库编号
     *
     * 关联 {@link AiKnowledgeDO#getId()}
     */
    private Long knowledgeId;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 内容
     */
    private String content;
    /**
     * 文件 URL
     */
    private String url;
    /**
     * token 数量
     */
    private Integer tokens;
    /**
     * 字符数
     */
    private Integer wordCount;
    /**
     * 切片状态
     * <p>
     * 枚举 {@link AiKnowledgeDocumentStatusEnum}
     */
    private Integer sliceStatus;

    /**
     * 状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
