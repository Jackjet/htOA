package com.kwchina.oa.purchase.yiban.entity;


import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Create by yuanjl on 2018/5/29
 */

@Entity
@Table(name = "Purchase_PriceBank", schema = "dbo")
public class PriceBank {

    private Integer priceBankId;
    private String purchaseGoods;                       //采购物品
    private String purchaseFinalMoney;                       //最终价格
    private Timestamp time;
    private String guikou;
    private String type;
    private int purchaseId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getPriceBankId() {
        return priceBankId;
    }
    public void setPriceBankId(Integer priceBankId) {
        this.priceBankId = priceBankId;
    }

    public String getPurchaseGoods() {
        return purchaseGoods;
    }
    public void setPurchaseGoods(String purchaseGoods) {
        this.purchaseGoods = purchaseGoods;
    }

    public String getPurchaseFinalMoney() {
        return purchaseFinalMoney;
    }
    public void setPurchaseFinalMoney(String purchaseFinalMoney) {
        this.purchaseFinalMoney = purchaseFinalMoney;
    }

    public int getPurchaseId() {
        return purchaseId;
    }
    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getGuikou() {
        return guikou;
    }
    public void setGuikou(String guikou) {
        this.guikou = guikou;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
