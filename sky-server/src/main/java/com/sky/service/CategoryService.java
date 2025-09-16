package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    //新增菜品分类
    void addCategory(CategoryDTO categoryDTO);

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void StartOrStop(Integer status, Long id);

    void updateCategory(CategoryDTO categoryDTO);

    Category getCategoryById(Long id);

    void deleteCategory(Long id);


    List<Category> selectCategoryByType(Integer type);
}
