package com.qiwenshare.stock.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.qiwenshare.common.anno.MyLog;
import com.qiwenshare.common.result.RestResult;
import com.qiwenshare.common.util.security.JwtUser;
import com.qiwenshare.common.util.security.SessionUtil;
import com.qiwenshare.stock.api.IUserService;
import com.qiwenshare.stock.component.JwtComp;
import com.qiwenshare.stock.domain.user.UserBean;
import com.qiwenshare.stock.dto.user.RegisterDTO;
import com.qiwenshare.stock.vo.user.UserLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Tag(name = "user", description = "该接口为用户接口，主要做用户登录，注册和校验token")
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;
    @Resource
    JwtComp jwtComp;

    public static Map<String, String> verificationCodeMap = new HashMap<>();


    public static final String CURRENT_MODULE = "用户管理";

    @Operation(summary = "用户注册", description = "注册账号", tags = {"user"})
    @PostMapping(value = "/register")
    @MyLog(operation = "用户注册", module = CURRENT_MODULE)
    @ResponseBody
    public RestResult<String> addUser(@RequestBody RegisterDTO registerDTO) {
        RestResult<String> restResult = null;
        UserBean userBean = new UserBean();
        BeanUtil.copyProperties(registerDTO, userBean);
        restResult = userService.registerUser(userBean);

        return restResult;
    }

    @Operation(summary = "用户登录", description = "用户登录认证后才能进入系统", tags = {"user"})
    @GetMapping("/login")
    @MyLog(operation = "用户登录", module = CURRENT_MODULE)
    @ResponseBody
    public RestResult<UserLoginVo> userLogin(
            @Parameter(description = "登录手机号") String telephone,
            @Parameter(description = "登录密码") String password){
        RestResult<UserLoginVo> restResult = new RestResult<UserLoginVo>();
        String salt = userService.getSaltByTelephone(telephone);
        String hashPassword = new SimpleHash("MD5", password, salt, 1024).toHex();

        UserBean result = userService.selectUserByTelephoneAndPassword(telephone, hashPassword);
        if (result == null) {
            return RestResult.fail().message("手机号或密码错误！");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("userId", result.getUserId());
        String token = "";
        try {
            token = jwtComp.createJWT(JSON.toJSONString(param));
        } catch (Exception e) {
            log.info("登录失败：{}", e);
            return RestResult.fail().message("创建token失败！");
        }
        UserBean sessionUserBean = userService.findUserInfoByTelephone(telephone);
        if (sessionUserBean.getAvailable() != null && sessionUserBean.getAvailable() == 0) {
            return RestResult.fail().message("用户已被禁用");
        }
        UserLoginVo userLoginVo = new UserLoginVo();
        BeanUtil.copyProperties(sessionUserBean, userLoginVo);
        userLoginVo.setToken("Bearer " + token);
        restResult.setData(userLoginVo);
        restResult.setSuccess(true);
        restResult.setCode(200001);
        return restResult;

    }


    @Operation(summary = "检查用户登录信息", description = "验证token的有效性", tags = {"user"})
    @GetMapping("/checkuserlogininfo")
    @ResponseBody
    public RestResult<UserLoginVo> checkUserLoginInfo() {
        UserLoginVo userLoginVo = new UserLoginVo();
        JwtUser sessionUserBean = SessionUtil.getSession();

        if (sessionUserBean != null && !"anonymousUser".equals(sessionUserBean.getUsername())) {

            UserBean user = userService.getById(sessionUserBean.getUserId());
            BeanUtil.copyProperties(user, userLoginVo);
            return RestResult.success().data(userLoginVo);

        } else {
            return RestResult.fail().message("用户暂未登录");
        }

    }

}
