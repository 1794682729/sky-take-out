package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.annotation.AutoFile;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) values " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFile(value = OperationType.INSERT)
    void addCategory(Category category);

    Page<Category> selectQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @AutoFile(value = OperationType.UPDATE)
    void update(Category category);

    @Select("select * from  category where id=#{id}")
    Category getCategoryById(Long id);

    @Delete("delete from category where id=#{id}")
    void deleteCategoryById(Long id);

    List<Category> selectByType(Integer type);
}
