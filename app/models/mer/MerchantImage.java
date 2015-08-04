package models.mer;

import models.constants.DeletedStatus;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by upshan on 15/8/3.
 */
@Entity
@Table(name = "merchants")
public class MerchantImage extends Model{

    /**
     * 所属商户
     */
    @JoinColumn(name = "merchant_id")
    @ManyToOne
    public Merchant merchant;

    /**
     * 图片宽度
     */
    public Integer width;

    /**
     * 图片高度
     */
    public Integer height;

    /**
     * 图片路径
     */
    public String url;

    /**
     * 图片上传订单号
     */
    public String uFId;


    /**
     * 创建时间
     */
    @Column(name = "created_at")
    public Date createdAt;

    /**
     * 逻辑删除,0:未删除，1:已删除
     */
    @Enumerated(EnumType.ORDINAL)
    public DeletedStatus deleted;
}
