package com.cac.biz.module.erp.controller.admin.stock.vo.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - ERP 其它入库单新增/修改 Request VO")
@Data
public class ErpStockInSaveReqVO {

    @Schema(description = "入库编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11756")
    private Long id;

    @Schema(description = "供应商编号", example = "3113")
    private Long supplierId;

    @Schema(description = "入库时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "入库时间不能为空")
    private LocalDateTime inTime;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "附件 URL", example = "https://www.iocoder.cn/1.doc")
    private String fileUrl;

    @Schema(description = "入库项列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "入库项列表不能为空")
    @Valid
    private List<Item> items;

    @Data
    public static class Item {

        @Schema(description = "入库项编号", example = "11756")
        private Long id;

        @Schema(description = "仓库编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3113")
        @NotNull(message = "仓库编号不能为空")
        private Long warehouseId;

        @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3113")
        @NotNull(message = "产品编号不能为空")
        private Long productId;

        @Schema(description = "产品单价", example = "100.00")
        private BigDecimal productPrice;

        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100.00")
        @NotNull(message = "产品数量不能为空")
        private BigDecimal count;

        @Schema(description = "备注", example = "随便")
        private String remark;

    }

}