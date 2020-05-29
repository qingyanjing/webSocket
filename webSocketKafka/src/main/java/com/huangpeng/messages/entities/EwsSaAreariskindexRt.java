package com.huangpeng.messages.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EwsSaAreariskindexRt {
    /**
     * 区域Id
     */
    private String areaId;

    /**
     * 统计时间
     */
    private Date statTime;

    /**
     * 风险值
     */
    private BigDecimal riskValue;

    /**
     * 环比风险值
     */
    private BigDecimal preHourRiskValue;

    /**
     * 同比风险值
     */
    private BigDecimal yesterdaySameHourRiskValue;

    /**
     * 预警等级Id
     */
    private String earlyWarningLevelId;

    /**
     * 持续时长
     */
    private Integer continueTimeLength;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}