package com.cac.biz.module.bpm.controller.admin.definition;

import com.cac.biz.framework.common.enums.CommonStatusEnum;
import com.cac.biz.framework.common.pojo.CommonResult;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.module.bpm.controller.admin.definition.vo.category.BpmCategoryPageReqVO;
import com.cac.biz.module.bpm.controller.admin.definition.vo.category.BpmCategoryRespVO;
import com.cac.biz.module.bpm.controller.admin.definition.vo.category.BpmCategorySaveReqVO;
import com.cac.biz.module.bpm.dal.dataobject.definition.BpmCategoryDO;
import com.cac.biz.module.bpm.service.definition.BpmCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static com.cac.biz.framework.common.pojo.CommonResult.success;
import static com.cac.biz.framework.common.util.collection.CollectionUtils.convertList;

@Tag(name = "管理后台 - BPM 流程分类")
@RestController
@RequestMapping("/bpm/category")
@Validated
public class BpmCategoryController {

    @Resource
    private BpmCategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "创建流程分类")
    @PreAuthorize("@ss.hasPermission('bpm:category:create')")
    public CommonResult<Long> createCategory(@Valid @RequestBody BpmCategorySaveReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新流程分类")
    @PreAuthorize("@ss.hasPermission('bpm:category:update')")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody BpmCategorySaveReqVO updateReqVO) {
        categoryService.updateCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除流程分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:category:delete')")
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得流程分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('bpm:category:query')")
    public CommonResult<BpmCategoryRespVO> getCategory(@RequestParam("id") Long id) {
        BpmCategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, BpmCategoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得流程分类分页")
    @PreAuthorize("@ss.hasPermission('bpm:category:query')")
    public CommonResult<PageResult<BpmCategoryRespVO>> getCategoryPage(@Valid BpmCategoryPageReqVO pageReqVO) {
        PageResult<BpmCategoryDO> pageResult = categoryService.getCategoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BpmCategoryRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获取流程分类的精简信息列表", description = "只包含被开启的分类，主要用于前端的下拉选项")
    public CommonResult<List<BpmCategoryRespVO>> getCategorySimpleList() {
        List<BpmCategoryDO> list = categoryService.getCategoryListByStatus(CommonStatusEnum.ENABLE.getStatus());
        list.sort(Comparator.comparingInt(BpmCategoryDO::getSort));
        return success(convertList(list, category -> new BpmCategoryRespVO().setId(category.getId())
                .setName(category.getName()).setCode(category.getCode())));
    }

}