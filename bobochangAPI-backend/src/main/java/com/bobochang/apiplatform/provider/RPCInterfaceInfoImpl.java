package com.bobochang.apiplatform.provider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bobochang.apicommon.common.ErrorCode;
import com.bobochang.apicommon.model.entity.InterfaceInfo;
import com.bobochang.apiplatform.exception.ThrowUtils;
import com.bobochang.apiplatform.service.InterfaceInfoService;
import com.bobochang.apirpc.InterfaceInfo.RPCInterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 2023/5/31 - 10:47
 *
 * @author bobochang
 * @description RPC 框架中实现 InterfaceInfo 方法
 */
@DubboService
public class RPCInterfaceInfoImpl implements RPCInterfaceInfo {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 查询数据库中调用的接口是否存在
     *
     * @param path   接口路径
     * @param method 接口调用方式
     * @return 接口信息
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        ThrowUtils.throwIf(StringUtils.isBlank(path) || StringUtils.isBlank(method), ErrorCode.PARAMS_ERROR, "接口不存在");
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfo::getUrl, path).eq(InterfaceInfo::getMethod, method);
        return interfaceInfoService.getOne(queryWrapper);
    }
}
