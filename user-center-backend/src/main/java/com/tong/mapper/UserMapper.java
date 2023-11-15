package com.tong.mapper;

import com.tong.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tong
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-11-06 17:09:32
* @Entity com.tong.model.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




