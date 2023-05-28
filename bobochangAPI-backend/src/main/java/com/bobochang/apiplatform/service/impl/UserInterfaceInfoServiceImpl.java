package com.bobochang.apiplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobochang.apiplatform.common.ErrorCode;
import com.bobochang.apiplatform.exception.BusinessException;
import com.bobochang.apiplatform.exception.ThrowUtils;
import com.bobochang.apiplatform.model.entity.UserInterfaceInfo;
import com.bobochang.apiplatform.service.UserInterfaceInfoService;
import com.bobochang.apiplatform.mapper.UserInterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author bobochang
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2023-05-26 13:48:25
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，所有参数必须非空
        if (add) {
            ThrowUtils.throwIf(userInterfaceInfo.getUserId() <= 0 || userInterfaceInfo.getInterfaceInfoId() <= 0, ErrorCode.PARAMS_ERROR, "接口或用户不存在");
        }
        ThrowUtils.throwIf(userInterfaceInfo.getLeftNum() < 0, ErrorCode.PARAMS_ERROR, "调用剩余次数不能小于0");
    }

    @Override
    public boolean invokeCount(long interfaceId, long userId) {
        // 校验
        ThrowUtils.throwIf(interfaceId <= 0 || userId <= 0, ErrorCode.PARAMS_ERROR);
        // 指定更新
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId", userId);
        updateWrapper.eq("interfaceInfoId", interfaceId);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return update(updateWrapper);
    }
}




