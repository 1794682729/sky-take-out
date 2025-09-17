package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    /**
     * 新增菜品管理
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){

        dishService.saveAndFlavor(dishDTO);

        return Result.success();
    }


    /**
     * 菜品分页查询
     */
    @ApiOperation("菜品分页查询")
    @GetMapping
              ("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("传入的菜品数据{}",dishPageQueryDTO);
      PageResult pageResult=dishService.pageQuery(dishPageQueryDTO);


        return Result.success(pageResult);
    }


    /**
     * 删除菜品
     */
    @DeleteMapping
    @ApiOperation("删除菜品接口")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品的id{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){

        log.info("查询菜品的id{}",id);
       DishVO dishVO= dishService.getByIdWithFlavor(id);

        return Result.success(dishVO);
    }

    @ApiOperation("修改菜品接口")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品{}",dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    @ApiOperation("起售或者停售")
    @PostMapping("/status/{status}")
    public Result StartOrStop(@PathVariable("status") Integer status ,Long id){
        log.info("起售或者停售status{}",status);
        dishService.StartOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }
}
