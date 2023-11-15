package com.tong.controller;

import com.tong.common.BaseResponse;
import com.tong.common.Code;
import com.tong.exception.BusinessException;
import com.tong.model.domain.User;
import com.tong.model.domain.request.UserLoginRequest;
import com.tong.model.domain.request.UserRegisterRequest;
import com.tong.service.UserService;
import com.tong.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.tong.constant.UserConstant.ADMIN_ROLE;
import static com.tong.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author Tong
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(Code.PARAMS_ERROR, "请求为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String nkuCode = userRegisterRequest.getNkuCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, nkuCode)) {
            throw new BusinessException(Code.PARAMS_ERROR, "必填项为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, nkuCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(Code.PARAMS_ERROR, "请求为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(Code.PARAMS_ERROR, "必填项为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if(request == null){
            throw new BusinessException(Code.PARAMS_ERROR, "请求为空");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(Code.NOT_LOGIN, "未登陆");
        }
        // 若直接返回currentUser信息并没有更新，需要去数据库取当前信息
        Long id = currentUser.getId();
        User user = userService.getById(id);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if(!isAdmin(request)){
            throw new BusinessException(Code.NO_AUTH, "无管理员权限");
        }
        List<User> userList = userService.searchUsers(username);
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if(!isAdmin(request)){
            throw new BusinessException(Code.NO_AUTH, "无管理员权限");
        }
        if(id <= 0){
            throw new BusinessException(Code.PARAMS_ERROR, "id必须为正整数");
        }
        boolean result = userService.deleteUser(id);
        return ResultUtils.success(result);
    }

    public boolean isAdmin(HttpServletRequest request){
        //获取用户角色
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        //仅管理员可查询
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
