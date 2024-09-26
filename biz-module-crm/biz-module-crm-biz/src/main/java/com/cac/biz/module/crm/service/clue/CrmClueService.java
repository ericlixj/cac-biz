package com.cac.biz.module.crm.service.clue;

import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.module.crm.controller.admin.clue.vo.CrmCluePageReqVO;
import com.cac.biz.module.crm.controller.admin.clue.vo.CrmClueSaveReqVO;
import com.cac.biz.module.crm.controller.admin.clue.vo.CrmClueTransferReqVO;
import com.cac.biz.module.crm.dal.dataobject.clue.CrmClueDO;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 线索 Service 接口
 *
 * @author Wanwan
 */
public interface CrmClueService {

    /**
     * 创建线索
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createClue(@Valid CrmClueSaveReqVO createReqVO);

    /**
     * 更新线索
     *
     * @param updateReqVO 更新信息
     */
    void updateClue(@Valid CrmClueSaveReqVO updateReqVO);

    /**
     * 更新线索相关的跟进信息
     *
     * @param id 编号
     * @param contactNextTime 下次联系时间
     * @param contactLastContent 最后联系内容
     */
    void updateClueFollowUp(Long id, LocalDateTime contactNextTime, String contactLastContent);

    /**
     * 删除线索
     *
     * @param id 编号
     */
    void deleteClue(Long id);

    /**
     * 获得线索
     *
     * @param id 编号
     * @return 线索
     */
    CrmClueDO getClue(Long id);

    /**
     * 获得线索列表
     *
     * @param ids 编号
     * @return 线索列表
     */
    List<CrmClueDO> getClueList(Collection<Long> ids, Long userId);

    /**
     * 获得线索分页
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 线索分页
     */
    PageResult<CrmClueDO> getCluePage(CrmCluePageReqVO pageReqVO, Long userId);

    /**
     * 线索转移
     *
     * @param reqVO  请求
     * @param userId 用户编号
     */
    void transferClue(CrmClueTransferReqVO reqVO, Long userId);

    /**
     * 线索转化为客户
     *
     * @param id  线索编号
     * @param userId 用户编号
     */
    void transformClue(Long id, Long userId);

    /**
     * 获得分配给我的、待跟进的线索数量
     *
     * @param userId 用户编号
     * @return 数量
     */
    Long getFollowClueCount(Long userId);

}
