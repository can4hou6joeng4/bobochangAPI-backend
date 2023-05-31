package com.bobochang.apiplatform.provider;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bobochang.apicommon.common.ErrorCode;
import com.bobochang.apicommon.model.entity.UserInterfaceInfo;
import com.bobochang.apiplatform.exception.ThrowUtils;
import com.bobochang.apiplatform.service.UserInterfaceInfoService;
import com.bobochang.apirpc.UserInterfaceInfo.RPCUserInterfaceInfo;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 2023/5/31 - 10:50
 *
 * @author bobochang
 * @description
 */
@DubboService
public class RPCUserInterfaceInfoImpl implements RPCUserInterfaceInfo {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 调用接口统计次数
     *
     * @param interfaceInfoId 接口 id
     * @param userId          用户 id
     * @return 是否操作成功
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        ThrowUtils.throwIf(interfaceInfoId < 1 || userId < 1, ErrorCode.PARAMS_ERROR);
        LambdaUpdateWrapper<UserInterfaceInfo> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getId, interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId, userId)
                .setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return userInterfaceInfoService.update(queryWrapper);
    }
}
