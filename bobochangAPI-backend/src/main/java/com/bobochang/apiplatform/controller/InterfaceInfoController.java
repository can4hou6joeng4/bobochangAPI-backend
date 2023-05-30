package com.bobochang.apiplatform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bobochang.apicommon.common.*;
import com.bobochang.apicommon.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.bobochang.apicommon.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.bobochang.apicommon.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.bobochang.apicommon.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.bobochang.apicommon.model.entity.InterfaceInfo;
import com.bobochang.apicommon.model.entity.User;
import com.bobochang.apicommon.model.enums.InterfaceInfoStatusEnum;
import com.bobochang.apiplatform.annotation.AuthCheck;
import com.bobochang.apiplatform.constant.CommonConstant;
import com.bobochang.apiplatform.exception.BusinessException;
import com.bobochang.apiplatform.exception.ThrowUtils;
import com.bobochang.apiplatform.service.InterfaceInfoService;
import com.bobochang.apiplatform.service.UserService;
import com.bobochang.sdk.client.BobochangApiClient;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 2023/5/26 - 13:53
 *
 * @author bobochang
 * @description
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    // endregion

    // region 发布下线

    /**
     * 发布接口
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/publish")
    public BaseResponse<Boolean> publishInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
        ThrowUtils.throwIf(ObjectUtils.isEmpty(idRequest.getId()), ErrorCode.PARAMS_ERROR);
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(ObjectUtils.isEmpty(oldInterfaceInfo), ErrorCode.NOT_FOUND_ERROR);
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(idRequest.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线接口
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                      HttpServletRequest request) {
        ThrowUtils.throwIf(ObjectUtils.isEmpty(idRequest.getId()), ErrorCode.PARAMS_ERROR);
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(ObjectUtils.isEmpty(oldInterfaceInfo), ErrorCode.NOT_FOUND_ERROR);
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(idRequest.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }
    // endregion

    //region 测试调用
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterface(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                HttpServletRequest request){
        ThrowUtils.throwIf(ObjectUtils.isEmpty(interfaceInfoInvokeRequest.getId()), ErrorCode.PARAMS_ERROR);
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(interfaceInfoInvokeRequest.getId());
        ThrowUtils.throwIf(ObjectUtils.isEmpty(oldInterfaceInfo), ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(oldInterfaceInfo.getStatus()==InterfaceInfoStatusEnum.OFFLINE.getValue(), ErrorCode.PARAMS_ERROR,"接口已关闭");
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        BobochangApiClient apiClient = new BobochangApiClient(accessKey, secretKey);
        Gson gson = new Gson();
        com.bobochang.sdk.model.User user = gson.fromJson(interfaceInfoInvokeRequest.getRequestParams(), com.bobochang.sdk.model.User.class);
        return ResultUtils.success(apiClient.getUsernameByPost(user));
    }
    //endregion
}
