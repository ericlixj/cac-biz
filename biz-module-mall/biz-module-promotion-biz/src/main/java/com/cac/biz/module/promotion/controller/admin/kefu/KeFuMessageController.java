package com.cac.biz.module.promotion.controller.admin.kefu;

import com.cac.biz.framework.common.enums.UserTypeEnum;
import com.cac.biz.framework.common.pojo.CommonResult;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.module.promotion.controller.admin.kefu.vo.message.KeFuMessagePageReqVO;
import com.cac.biz.module.promotion.controller.admin.kefu.vo.message.KeFuMessageRespVO;
import com.cac.biz.module.promotion.controller.admin.kefu.vo.message.KeFuMessageSendReqVO;
import com.cac.biz.module.promotion.dal.dataobject.kefu.KeFuMessageDO;
import com.cac.biz.module.promotion.service.kefu.KeFuMessageService;
import com.cac.biz.module.system.api.user.AdminUserApi;
import com.cac.biz.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

import static com.cac.biz.framework.common.pojo.CommonResult.success;
import static com.cac.biz.framework.common.util.collection.CollectionUtils.convertSet;
import static com.cac.biz.framework.common.util.collection.CollectionUtils.filterList;
import static com.cac.biz.framework.common.util.collection.MapUtils.findAndThen;
import static com.cac.biz.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 客服消息")
@RestController
@RequestMapping("/promotion/kefu-message")
@Validated
public class KeFuMessageController {

    @Resource
    private KeFuMessageService messageService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/send")
    @Operation(summary = "发送客服消息")
    @PreAuthorize("@ss.hasPermission('promotion:kefu-message:send')")
    public CommonResult<Long> sendKeFuMessage(@Valid @RequestBody KeFuMessageSendReqVO sendReqVO) {
        sendReqVO.setSenderId(getLoginUserId()).setSenderType(UserTypeEnum.ADMIN.getValue()); // 设置用户编号和类型
        return success(messageService.sendKefuMessage(sendReqVO));
    }

    @PutMapping("/update-read-status")
    @Operation(summary = "更新客服消息已读状态")
    @Parameter(name = "conversationId", description = "会话编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:kefu-message:update')")
    public CommonResult<Boolean> updateKeFuMessageReadStatus(@RequestParam("conversationId") Long conversationId) {
        messageService.updateKeFuMessageReadStatus(conversationId, getLoginUserId(), UserTypeEnum.ADMIN.getValue());
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得客服消息分页")
    @PreAuthorize("@ss.hasPermission('promotion:kefu-message:query')")
    public CommonResult<PageResult<KeFuMessageRespVO>> getKeFuMessagePage(@Valid KeFuMessagePageReqVO pageReqVO) {
        // 获得数据
        PageResult<KeFuMessageDO> pageResult = messageService.getKeFuMessagePage(pageReqVO);

        // 拼接数据
        PageResult<KeFuMessageRespVO> result = BeanUtils.toBean(pageResult, KeFuMessageRespVO.class);
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertSet(filterList(result.getList(),
                item -> UserTypeEnum.ADMIN.getValue().equals(item.getSenderType())), KeFuMessageRespVO::getSenderId));
        result.getList().forEach(item-> findAndThen(userMap, item.getSenderId(),
                user -> item.setSenderAvatar(user.getAvatar())));
        return success(result);
    }

}