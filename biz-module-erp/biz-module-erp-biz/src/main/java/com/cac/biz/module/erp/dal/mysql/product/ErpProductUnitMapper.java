package com.cac.biz.module.erp.dal.mysql.product;

import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cac.biz.framework.mybatis.core.mapper.BaseMapperX;
import com.cac.biz.module.erp.controller.admin.product.vo.unit.ErpProductUnitPageReqVO;
import com.cac.biz.module.erp.dal.dataobject.product.ErpProductUnitDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ERP 产品单位 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpProductUnitMapper extends BaseMapperX<ErpProductUnitDO> {

    default PageResult<ErpProductUnitDO> selectPage(ErpProductUnitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpProductUnitDO>()
                .likeIfPresent(ErpProductUnitDO::getName, reqVO.getName())
                .eqIfPresent(ErpProductUnitDO::getStatus, reqVO.getStatus())
                .orderByDesc(ErpProductUnitDO::getId));
    }

    default ErpProductUnitDO selectByName(String name) {
        return selectOne(ErpProductUnitDO::getName, name);
    }

    default List<ErpProductUnitDO> selectListByStatus(Integer status) {
        return selectList(ErpProductUnitDO::getStatus, status);
    }

}