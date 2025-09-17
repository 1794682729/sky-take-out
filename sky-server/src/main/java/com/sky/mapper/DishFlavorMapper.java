package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);


    void deletBatch(List<Long> ids);
    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> selectByDishId(Long dishId);
    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deletDishId(Long id);
}
