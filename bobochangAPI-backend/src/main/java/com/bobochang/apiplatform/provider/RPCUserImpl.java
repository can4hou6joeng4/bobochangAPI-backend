package com.bobochang.apiplatform.provider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bobochang.apicommon.common.ErrorCode;
import com.bobochang.apicommon.model.entity.User;
import com.bobochang.apiplatform.exception.ThrowUtils;
import com.bobochang.apiplatform.service.UserService;
import com.bobochang.apirpc.User.RPCUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 2023/5/31 - 10:41
 *
 * @author bobochang
 * @description RPC 框架中实现 User 方法
 */
@DubboService
public class RPCUserImpl implements RPCUser {

    @Resource
    private UserService userService;

    /**
     * 根据用户 accessKey 查询是否分配密钥
     *
     * @param accessKey 用户的密钥
     * @return 查询到的用户
     */
    @Override
    public User getInvokeUser(String accessKey) {
        ThrowUtils.throwIf(StringUtils.isBlank(accessKey), ErrorCode.PARAMS_ERROR);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccessKey, accessKey);
        return userService.getOne(queryWrapper);
    }
}
