package com.bobochang.apirpc.UserInterfaceInfo;

public interface RPCUserInterfaceInfo {
    /**
     * 调用接口统计次数
     * @param interfaceInfoId 接口 id
     * @param userId 用户 id
     * @return 是否操作成功
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}