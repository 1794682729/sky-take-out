package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品分类")
    public Result addCategory(@RequestBody  CategoryDTO categoryDTO) {
        log.info("新增菜品信息+{}",categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("菜品分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("菜品分页查询：{}",categoryPageQueryDTO);
        PageResult pageResult= categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用或者禁用菜品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("调整禁用或者启用")
    public Result StartOrStop(@PathVariable("status") Integer status,Long id) {
        log.info("菜品启用和禁用：{}",status);
        categoryService.StartOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("根据id查询菜品信息")
    public Result<Category> getCategoryById(@PathVariable("id") Long id) {

        Category category=categoryService.getCategoryById(id);
        return Result.success(category);
    }
    /**
     * 修改菜品信息
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改菜品信息{}",categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }
    @DeleteMapping()
    @ApiOperation("删除菜品")
    public Result deleteCategory( Long id) {
        log.info("删除的菜品id是:{}",id);
        categoryService.deleteCategory(id);
        return  Result.success();
    }

}
