package com.bobochang.apiplatform.service;

import com.bobochang.apiplatform.model.entity.InterfaceInfo;
import com.bobochang.apiplatform.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author bobochang
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-05-26 13:48:25
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 修改调用次数
     * @param interfaceId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceId,long userId);
}
