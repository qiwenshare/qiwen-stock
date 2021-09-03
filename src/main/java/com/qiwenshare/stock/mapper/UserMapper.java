package com.qiwenshare.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiwenshare.stock.domain.UserBean;

public interface UserMapper extends BaseMapper<UserBean> {
    int insertUser(UserBean userBean);

    int insertUserRole(long userId, long roleId);



}
