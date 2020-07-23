package com.qiwenshare.stock.mapper;


import com.qiwenshare.common.domain.TableQueryBean;
import com.qiwenshare.stock.domain.Permission;
import com.qiwenshare.stock.domain.Role;
import com.qiwenshare.stock.domain.UserBean;

import java.util.List;

public interface UserMapper {
    int insertUser(UserBean userBean);

    int insertUserRole(long userId, long roleid);

    UserBean selectUser(UserBean userBean);

    List<UserBean> selectAdminUserList();

    /**
     * 通過id得到用戶信息
     *
     * @param userId
     * @return
     */
    UserBean selectUserById(long userId);

    /**
     * 通過openid得到用戶信息
     * @param openid
     * @return
     */
    UserBean selectUserByopenid(String openid);

    /**
     * 批量删除用户信息
     *
     * @param userBean
     */
    void deleteUserInfo(UserBean userBean);

    /**
     * 修改用戶信息
     *
     * @param userBean
     */
    void updateUserInfo(UserBean userBean);

    UserBean selectUserByUserName(UserBean userBean);

    void updateEmail(UserBean userBean);
    void updateTelephone(UserBean userBean);
    void updataImageUrl(UserBean userBean);

    UserBean selectUserByUserNameAndPassword(UserBean userBean);

    UserBean selectUserByTelephone(UserBean userBean);

    List<UserBean> selectAllUserList();

    List<UserBean> selectUserListByCondition(TableQueryBean tableQueryBean);

    List<Role> selectRoleList();

    List<Permission> selectPermissionListByCondition(TableQueryBean tableQueryBean);

    int selectUserCountByCondition(TableQueryBean tableQueryBean);

    List<Permission> getPermissionByRole(int roleid);
}
