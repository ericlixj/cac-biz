package com.cac.biz.module.promotion.controller.admin.combination.vo.recrod;

import com.cac.biz.framework.common.pojo.PageParam;
import com.cac.biz.framework.common.validation.InEnum;
import com.cac.biz.module.promotion.enums.combination.CombinationRecordStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.cac.biz.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 拼团记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CombinationRecordReqPageVO extends PageParam {

    @Schema(description = "活动状态", example = "1")
    @InEnum(CombinationRecordStatusEnum.class)
    private Integer status;

    @Schema(description = "团长编号", example = "1024")
    private Long headId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
