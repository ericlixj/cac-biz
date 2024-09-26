package com.cac.biz.module.crm.controller.admin.customer;

import com.cac.biz.framework.common.pojo.CommonResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.module.crm.controller.admin.customer.vo.poolconfig.CrmCustomerPoolConfigRespVO;
import com.cac.biz.module.crm.controller.admin.customer.vo.poolconfig.CrmCustomerPoolConfigSaveReqVO;
import com.cac.biz.module.crm.dal.dataobject.customer.CrmCustomerPoolConfigDO;
import com.cac.biz.module.crm.service.customer.CrmCustomerPoolConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.cac.biz.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - CRM 客户公海配置")
@RestController
@RequestMapping("/crm/customer-pool-config")
@Validated
public class CrmCustomerPoolConfigController {

    @Resource
    private CrmCustomerPoolConfigService customerPoolConfigService;

    @GetMapping("/get")
    @Operation(summary = "获取客户公海规则设置")
    @PreAuthorize("@ss.hasPermission('crm:customer-pool-config:query')")
    public CommonResult<CrmCustomerPoolConfigRespVO> getCustomerPoolConfig() {
        CrmCustomerPoolConfigDO poolConfig = customerPoolConfigService.getCustomerPoolConfig();
        return success(BeanUtils.toBean(poolConfig, CrmCustomerPoolConfigRespVO.class));
    }

    @PutMapping("/save")
    @Operation(summary = "更新客户公海规则设置")
    @PreAuthorize("@ss.hasPermission('crm:customer-pool-config:update')")
    public CommonResult<Boolean> saveCustomerPoolConfig(@Valid @RequestBody CrmCustomerPoolConfigSaveReqVO updateReqVO) {
        customerPoolConfigService.saveCustomerPoolConfig(updateReqVO);
        return success(true);
    }

}
