package com.kwchina.oa.purchase.zhaotou.entity;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import lombok.Data;

import javax.persistence.*;

/**
 * 各个打分人的总分
 * @author JJ-Lee
 * @date : 2018-12-21 13:57
 **/
@Entity
@Table(name = "zhaotou_eachTotal", schema = "dbo")
@ObjectId(id="totalId")
@Data
public class EachTotal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer totalId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scorerId")
    private SystemUserInfor scorer;
    private Integer totalTech;
    private Integer totalBiz;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scoreTotalId",nullable = false)
    private ZhaotouScoreTotal scoreTotal;
}
