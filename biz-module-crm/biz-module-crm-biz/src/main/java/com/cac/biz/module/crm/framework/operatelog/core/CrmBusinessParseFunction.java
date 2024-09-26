package com.cac.biz.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import com.cac.biz.module.crm.dal.dataobject.business.CrmBusinessDO;
import com.cac.biz.module.crm.service.business.CrmBusinessService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * CRM 商机的 {@link IParseFunction} 实现类
 *
 * @author HUIHUI
 */
@Component
@Slf4j
public class CrmBusinessParseFunction implements IParseFunction {

    public static final String NAME = "getBusinessById";

    @Resource
    private CrmBusinessService businessService;

    @Override
    public boolean executeBefore() {
        return true; // 先转换值后对比
    }

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }
        CrmBusinessDO businessDO = businessService.getBusiness(Long.parseLong(value.toString()));
        return businessDO == null ? "" : businessDO.getName();
    }

}
