package com.cac.biz.module.promotion.controller.admin.kefu.vo.message;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.cac.biz.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 客服消息分页 Request VO")
@Data
@ToString(callSuper = true)
public class KeFuMessagePageReqVO extends PageParam {

    @Schema(description = "会话编号", example = "12580")
    private Long conversationId;

}