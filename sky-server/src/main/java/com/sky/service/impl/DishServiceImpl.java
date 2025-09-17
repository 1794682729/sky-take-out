package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品的口味数据
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveAndFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //向菜品表插入一条数据
        dishMapper.insert(dish);

        Long id = dish.getId();


        //向口味表插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Page<DishVO> page=dishMapper.selectPageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //根据传入的id查询返回选中的菜品集合
       List<Dish> list= dishMapper.selectByIds(ids);

       //循环遍历返回的菜品集合，判断当前菜品是否是起售中，如果是起售中，抛出异常禁止删除，如果未起售则放行
       for (Dish dish : list) {
           if (dish.getStatus()== StatusConstant.ENABLE){
               throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
           }
       }
       //判断菜品是否被套餐关联

       List<Long> setmealIds= setmealDishMapper.selectByDishId(ids);
       if (setmealIds != null && setmealIds.size()>0){
           throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
       }

//       删除菜品
        dishMapper.deletBatch(ids);

//       删除菜品关联的口味信息

        dishFlavorMapper.deletBatch(ids);

    }

    //根据id查询菜品和对应的口味数据

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        DishVO dishVO = new DishVO();
        Dish dish=dishMapper.selectById(id);
        BeanUtils.copyProperties(dish,dishVO);
        List<DishFlavor> flavors=dishFlavorMapper.selectByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
//        //删除原有菜品的口味
        Long id=dishDTO.getId();
        dishFlavorMapper.deletDishId(id);

        //向口味表中插入新菜品的口味信息
        List<DishFlavor> flavors=dishDTO.getFlavors();
        if (flavors != null && flavors.size()>0){
//            flavors.forEach(dishFlavor -> {
//                dishFlavor.setDishId(id);
//            });
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(id);
            }
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 起售或者停售
     *
     * @param status
     */
    @Override
    public void StartOrStop(Integer status,Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.update(dish);
    }
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }
}
