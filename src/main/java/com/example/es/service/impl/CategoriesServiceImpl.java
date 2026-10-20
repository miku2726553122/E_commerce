package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Categories;
import com.example.es.service.CategoriesService;
import com.example.es.mapper.CategoriesMapper;
import com.example.es.utils.AliOssUtils;
import com.example.es.utils.Response;
import com.example.es.vo.admin.AddCategoryReqVO;
import com.example.es.vo.admin.AddCategoryRspVO;
import com.example.es.vo.admin.FindAllCategoriesRspVO;
import com.example.es.vo.admin.UpdateCategoriesReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 86159
 * @description 针对表【categories(商品分类表)】的数据库操作Service实现
 * @createDate 2026-10-19 10:21:26
 */
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories>
        implements CategoriesService {

    @Autowired
    private CategoriesMapper categoriesMapper;

    @Autowired
    private AliOssUtils aliOssUtils;

    @Override
    public Response selectAllCategories() {
        List<Categories> list = categoriesMapper.selectAllCategories();

        List<FindAllCategoriesRspVO> vos = null;
        //DO转VO
        vos = list.stream().map(item -> FindAllCategoriesRspVO.builder()
                .id(item.getId())
                .parentId(item.getParentId())
                .categoryName(item.getCategoryName())
                .status(item.getStatus())
                .build()).toList();
        return Response.success(vos);
    }
    /**
     * 添加分类
     * @param addCategoryReqVO
     * @return
     */

    @Override
    public Response addCategory(AddCategoryReqVO addCategoryReqVO) {


        // VO 转实体（categoryImage 已经是 OSS 的 URL）
        Categories category = Categories.builder()
                .parentId(addCategoryReqVO.getParentId())
                .categoryName(addCategoryReqVO.getCategoryName())
                .categoryImage(addCategoryReqVO.getCategoryImage())  // 直接使用传入的URL
                .sortOrder(addCategoryReqVO.getSortOrder())
                .status(1)  // 默认启用
                .build();

        //分类是否存在
        Categories existCategory = categoriesMapper.selectOne(new QueryWrapper<Categories>()
                .eq("category_name", addCategoryReqVO.getCategoryName())
                .eq("parent_id", addCategoryReqVO.getParentId()));

        if (existCategory != null) {
            return Response.fail("分类已存在");
        }

        // 插入数据库
        categoriesMapper.insert(category);

        //实体 转 VO
        AddCategoryRspVO vos = null;
        vos = AddCategoryRspVO.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .status(category.getStatus())
                .build();

        return Response.success(vos);
    }

    /**
     * 更新分类
     * @param id
     * @param updateCategoriesReqVO
     * @return
     */
    @Override
    public Response updateCategory(Long id, UpdateCategoriesReqVO updateCategoriesReqVO) {
        //VO 转实体
        Categories category = Categories.builder()
                .id(id)
                .categoryName(updateCategoriesReqVO.getCategoryName())
                .status(updateCategoriesReqVO.getStatus())
                .sortOrder(updateCategoriesReqVO.getSortOrder())
                .updatedAt(LocalDateTime.now())
                .build();

        categoriesMapper.updateById(category);

        return Response.success();
    }


    /**
     * 删除分类
     * @param id
     * @return
     */
    @Override
    public Response deleteCategory(Long id) {
        //判断分类下是否有子分类
        List<Categories> categoriesList = categoriesMapper.selectList(new QueryWrapper<Categories>()
                .eq("parent_id", id));
        if (categoriesList != null && !categoriesList.isEmpty()) {
            // 有子分类，不允许删除
            return Response.fail("分类下有子分类，不允许删除");
        }

        categoriesMapper.deleteById(id);

        return Response.success();
    }
}




