package com.cac.biz.module.crm.dal.mysql.product;

import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.mybatis.core.mapper.BaseMapperX;
import com.cac.biz.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.cac.biz.module.crm.controller.admin.product.vo.product.CrmProductPageReqVO;
import com.cac.biz.module.crm.dal.dataobject.product.CrmProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CRM 产品 Mapper
 *
 * @author ZanGe丶
 */
@Mapper
public interface CrmProductMapper extends BaseMapperX<CrmProductDO> {

    default PageResult<CrmProductDO> selectPage(CrmProductPageReqVO reqVO) {
        return selectPage(reqVO, new MPJLambdaWrapperX<CrmProductDO>()
                .likeIfPresent(CrmProductDO::getName, reqVO.getName())
                .eqIfPresent(CrmProductDO::getStatus, reqVO.getStatus())
                .orderByDesc(CrmProductDO::getId));
    }

    default CrmProductDO selectByNo(String no) {
        return selectOne(CrmProductDO::getNo, no);
    }

    default Long selectCountByCategoryId(Long categoryId) {
        return selectCount(CrmProductDO::getCategoryId, categoryId);
    }

    default List<CrmProductDO> selectListByStatus(Integer status) {
        return selectList(CrmProductDO::getStatus, status);
    }

}
