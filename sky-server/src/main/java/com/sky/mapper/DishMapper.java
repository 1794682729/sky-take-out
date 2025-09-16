package com.sky.mapper;

import com.sky.annotation.AutoFile;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    @AutoFile(value = OperationType.INSERT)
    void insert(Dish dish);

    @Select("select count(id) from dish where category_id=#{categoryId}")
    void selectCategoryById(Long id);
}
