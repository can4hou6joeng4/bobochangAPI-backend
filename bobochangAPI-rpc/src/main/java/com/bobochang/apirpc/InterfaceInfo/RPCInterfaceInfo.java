package com.bobochang.apirpc.InterfaceInfo;

import com.bobochang.apicommon.model.entity.InterfaceInfo;

public interface RPCInterfaceInfo {
    /**
     * 查询数据库中调用的接口是否存在
     *
     * @param path   接口路径
     * @param method 接口调用方式
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}