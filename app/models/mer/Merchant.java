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
public class Merchant extends Model{

    /**
     * 商户名称
     */
    @Column(name = "name")
    public String name;

    /**
     * 商户编号
     */
    @Column(name = "code")
    public String code;


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
