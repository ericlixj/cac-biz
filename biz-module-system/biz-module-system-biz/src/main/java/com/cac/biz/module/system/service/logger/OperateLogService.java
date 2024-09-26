package com.cac.biz.module.system.service.logger;

import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.module.system.api.logger.dto.OperateLogCreateReqDTO;
import com.cac.biz.module.system.api.logger.dto.OperateLogPageReqDTO;
import com.cac.biz.module.system.controller.admin.logger.vo.operatelog.OperateLogPageReqVO;
import com.cac.biz.module.system.dal.dataobject.logger.OperateLogDO;

/**
 * 操作日志 Service 接口
 *
 * @author 芋道源码
 */
public interface OperateLogService {

    /**
     * 记录操作日志
     *
     * @param createReqDTO 创建请求
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqDTO pageReqVO);

}
