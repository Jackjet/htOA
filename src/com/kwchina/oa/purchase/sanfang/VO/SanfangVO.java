package com.kwchina.oa.purchase.sanfang.VO;

import java.util.List;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/8/1 17:09
 *
 * @desc
 */
public class SanfangVO {
    private int page;
    private int total;
    private int records;
    private List<SanfangListVO> rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public List<SanfangListVO> getRows() {
        return rows;
    }

    public void setRows(List<SanfangListVO> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "SanfangVO{" +
                "page=" + page +
                ", total=" + total +
                ", records=" + records +
                ", rows=" + rows +
                '}';
    }
}
