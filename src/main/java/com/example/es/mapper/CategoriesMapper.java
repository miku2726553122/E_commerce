package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.es.entity.Categories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.es.vo.admin.AddCategoryReqVO;
import com.example.es.vo.admin.AddCategoryRspVO;
import com.example.es.vo.admin.FindAllCategoriesRspVO;
import com.example.es.vo.user.FindProductCategoriesRspVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86159
* @description 针对表【categories(商品分类表)】的数据库操作Mapper
* @createDate 2026-10-19 10:21:26
* @Entity com.example.es.entity.Categories
*/
public interface CategoriesMapper extends BaseMapper<Categories> {

    @Select("SELECT * FROM categories WHERE id = #{id} FOR UPDATE")
    Categories selectByIdForUpdate(@Param("id") Long id);

    default List<Categories> selectAllCategories(){
        return selectList(Wrappers.<Categories>lambdaQuery()
                .ne(Categories::getStatus, 2)
                .orderByDesc(Categories::getCreatedAt)
                );
    }
}




