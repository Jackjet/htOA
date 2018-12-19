package com.kwchina.oa.purchase.zhaotou.VO;

import com.kwchina.oa.purchase.sanfang.VO.SanfangListVO;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/8/1 17:09
 *
 * @desc
 */
@Data
public class BidListVO {
    private Integer bidInfoId;
    private String projectName;
    private String applierName;
    private String departmentName;
    private Date startTime;
    private String zbcode;
    private String zhaotouStatus;
}
