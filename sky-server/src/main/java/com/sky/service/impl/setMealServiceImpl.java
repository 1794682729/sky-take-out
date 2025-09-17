package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.setMealMapper;
import com.sky.service.setMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class setMealServiceImpl implements setMealService {
    @Autowired
    private setMealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 添加套餐
     */
    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //向套餐表中添加数据
         setmealMapper.insert(setmeal);
        //保存套餐表和菜品表的关联关系
        Long id = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes!=null && setmealDishes.size()>0) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(id);
            }
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }
}
