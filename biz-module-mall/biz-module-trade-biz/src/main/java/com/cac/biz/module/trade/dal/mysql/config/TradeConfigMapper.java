package com.cac.biz.module.trade.dal.mysql.config;

import com.cac.biz.framework.mybatis.core.mapper.BaseMapperX;
import com.cac.biz.module.trade.dal.dataobject.config.TradeConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易中心配置 Mapper
 *
 * @author owen
 */
@Mapper
public interface TradeConfigMapper extends BaseMapperX<TradeConfigDO> {

}
