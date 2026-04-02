package com.lucky.common.core.page;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.constant.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author lucky
 */
@Data
@NoArgsConstructor
public class TableDataInfo<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, long total) {
        this.rows = list;
        this.total = total;
        this.code = HttpStatus.SUCCESS;
        this.msg = "查询成功";
    }

    /**
     * 构建表格分页数据对象
     */
    public static <T> TableDataInfo<T> build(List<T> list, long total) {
        return new TableDataInfo<>(list, total);
    }

    /**
     * 根据IPage分页对象构建表格分页数据对象
     */
    public static <T> TableDataInfo<T> build(IPage<T> page) {
        TableDataInfo<T> data = new TableDataInfo<>();
        data.setCode(HttpStatus.SUCCESS);
        data.setMsg("查询成功");
        data.setRows(page.getRecords());
        data.setTotal(page.getTotal());
        return data;
    }

    /**
     * 根据IPage分页对象构建表格分页数据对象
     */
    public static <T, V> TableDataInfo<V> build(IPage<T> page, Class<V> clazz) {
        TableDataInfo<V> data = new TableDataInfo<>();
        data.setCode(HttpStatus.SUCCESS);
        data.setMsg("查询成功");
        data.setRows(BeanUtil.copyToList(page.getRecords(), clazz));
        data.setTotal(page.getTotal());
        return data;
    }

}
