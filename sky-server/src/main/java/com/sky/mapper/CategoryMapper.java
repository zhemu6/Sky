package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
    * @author:  lushihao
    * @version: 1.0
    * create:   2025-05-20   14:05
    */
@Mapper
public interface CategoryMapper {
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQuery);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    void update(Category category);
    List<Category> list(Integer type);
}
