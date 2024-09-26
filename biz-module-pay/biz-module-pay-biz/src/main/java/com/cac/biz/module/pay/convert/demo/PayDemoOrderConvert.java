package com.cac.biz.module.pay.convert.demo;

import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.module.pay.controller.admin.demo.vo.order.PayDemoOrderCreateReqVO;
import com.cac.biz.module.pay.controller.admin.demo.vo.order.PayDemoOrderRespVO;
import com.cac.biz.module.pay.dal.dataobject.demo.PayDemoOrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 示例订单 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface PayDemoOrderConvert {

    PayDemoOrderConvert INSTANCE = Mappers.getMapper(PayDemoOrderConvert.class);

    PayDemoOrderDO convert(PayDemoOrderCreateReqVO bean);

    PayDemoOrderRespVO convert(PayDemoOrderDO bean);

    PageResult<PayDemoOrderRespVO> convertPage(PageResult<PayDemoOrderDO> page);

}
