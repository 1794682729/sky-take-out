package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFile;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    @AutoFile(value = OperationType.INSERT)
    void insert(Dish dish);

    @Select("select count(id) from dish where category_id=#{categoryId}")
    void selectCategoryById(Long id);

    Page<DishVO> selectPageQuery(DishPageQueryDTO dishPageQueryDTO);

    List<Dish> selectByIds(List<Long> ids);

    void deletBatch( List<Long> dishIds);
}
