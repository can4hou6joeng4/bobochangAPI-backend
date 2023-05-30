package com.bobochang.apicommon.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 2023/5/29 - 09:25
 *
 * @author bobochang
 * @description
 */
@Data
public class UserInterfaceInfoVO implements Serializable {

    /**
     * 剩余调用次数
     */
    private int leftNum;

    /**
     * 总调用次数
     */
    private int totalNum;

    private static final long serialVersionUID = 1L;
}
