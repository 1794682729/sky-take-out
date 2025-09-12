package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        //设置初始状态为禁用
        category.setStatus(StatusConstant.DISABLE);
        //设置创建的时间
        category.setCreateTime(LocalDateTime.now());
        //设置修改的时间
        category.setUpdateTime(LocalDateTime.now());
        //设置创建人的id
        category.setCreateUser(BaseContext.getCurrentId());
        //设置修改人的id
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.addCategory(category);
    }
}
