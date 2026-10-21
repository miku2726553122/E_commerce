package com.example.es.service;

import com.example.es.entity.Categories;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.AddCategoryReqVO;
import com.example.es.vo.admin.UpdateCategoriesReqVO;

/**
* @author 86159
* @description 针对表【categories(商品分类表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface CategoriesService extends IService<Categories> {

    Response selectAllCategories();

    Response addCategory(AddCategoryReqVO addCategoryReqVO);

    Response updateCategory(Long id, UpdateCategoriesReqVO updateCategoriesReqVO);

    Response deleteCategory(Long id);

    Response findAllCategories();
}
