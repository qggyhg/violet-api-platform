package com.violet.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.violet.api.entity.User;
import org.apache.ibatis.annotations.Mapper;

import javax.xml.ws.soap.MTOM;

/**
 * @Author: violet
 * @Date: 2026/3/14 10:40
 * @ProjectName: Violet_Job
 */
@Mapper
public interface UserMapper extends BaseMapper<User>{
}
