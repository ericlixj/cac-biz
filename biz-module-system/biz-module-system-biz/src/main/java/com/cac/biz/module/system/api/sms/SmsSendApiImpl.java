package com.cac.biz.module.system.api.sms;

import com.cac.biz.module.system.api.sms.dto.send.SmsSendSingleToUserReqDTO;
import com.cac.biz.module.system.service.sms.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 短信发送 API 接口
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class SmsSendApiImpl implements SmsSendApi {

    @Resource
    private SmsSendService smsSendService;

    @Override
    public Long sendSingleSmsToAdmin(SmsSendSingleToUserReqDTO reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getMobile())) {
            log.error("mobile is null, userId = {}", reqDTO.getUserId());
            return null;
        }
        return smsSendService.sendSingleSmsToAdmin(reqDTO.getMobile(), reqDTO.getUserId(), reqDTO.getTemplateCode(), reqDTO.getTemplateParams());
    }

    @Override
    public Long sendSingleSmsToMember(SmsSendSingleToUserReqDTO reqDTO) {
        return smsSendService.sendSingleSmsToMember(reqDTO.getMobile(), reqDTO.getUserId(), reqDTO.getTemplateCode(), reqDTO.getTemplateParams());
    }
}
