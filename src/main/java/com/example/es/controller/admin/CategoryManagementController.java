package com.example.es.controller.admin;


import com.example.es.service.CategoriesService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.AddCategoryReqVO;
import com.example.es.vo.admin.UpdateCategoriesReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "分类管理", description = "分类管理相关接口")
public class CategoryManagementController {

    @Autowired
    public CategoriesService categoriesService;


    @GetMapping("/categories")
    @Operation(summary = "获取所有分类", description = "获取所有分类")
    public Response selectAllCategories() {
        return categoriesService.selectAllCategories();
    }


    @PostMapping("/categories")
    @Operation(summary = "添加分类", description = "添加新分类")
    public Response addCategory(@RequestBody AddCategoryReqVO addCategoryReqVO) {
        return categoriesService.addCategory(addCategoryReqVO);
    }


    @PutMapping("/categories/{id}")
    @Operation(summary = "更新分类", description = "更新分类信息")
    public Response updateCategory(@PathVariable Long id, @RequestBody UpdateCategoriesReqVO updateCategoriesReqVO){
        return categoriesService.updateCategory(id, updateCategoriesReqVO);

    }


    @DeleteMapping("/categories/{id}")
    @Operation(summary = "删除分类", description = "删除分类")
    public Response deleteCategory(@PathVariable Long id){
        return categoriesService.deleteCategory(id);
    }
}
