package com.dewey.core.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * <p>
 * 数据配置项
 * </p>
 *
 * @author dewey
 * @since 2019-07-24
 */
@Data
@TableName("DATA_CONFIG")
public class DataConfig {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联项目id
     */
    @TableField("PROJECT_ID")
    private Integer projectId;

    /**
     * 表名
     */
    @TableField("PROJECT_TABLE_NAME")
    private String projectTableName;

    /**
     * 列名
     */
    @TableField("CLO_NAME")
    private String cloName;

    /**
     * 列配置说明，说明项以逗号分开
     */
    @TableField("CLO_CONFIG")
    private String cloConfig;

    /**
     * 关联表名
     */
    @TableField("RELATE_TABLE_NAME")
    private String relateTableName;

    /**
     * 关联列名
     */
    @TableField("RELATE_CLO_NAME")
    private String relateCloName;


}
