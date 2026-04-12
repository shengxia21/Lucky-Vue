package com.lucky.system.service;

import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.system.domain.SysConfig;
import com.lucky.system.domain.query.config.SysConfigQuery;
import com.lucky.system.domain.query.config.SysConfigSaveQuery;
import com.lucky.system.domain.vo.config.SysConfigVO;

import java.util.List;

/**
 * 参数配置 服务层
 *
 * @author ruoyi
 */
public interface ISysConfigService {

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    SysConfigVO selectConfigById(Long configId);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String selectConfigByKey(String configKey);

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    boolean selectCaptchaEnabled();

    /**
     * 获取注册开关
     *
     * @return true开启，false关闭
     */
    boolean selectRegisterEnabled();

    /**
     * 查询参数配置列表
     *
     * @param pageQuery 分页查询对象
     * @param query 参数配置查询对象
     * @return 参数配置集合
     */
    TableDataInfo<SysConfigVO> selectConfigList(PageQuery pageQuery, SysConfigQuery query);

    /**
     * 查询参数配置列表
     *
     * @param query 参数配置查询对象
     * @return 参数配置集合
     */
    List<SysConfig> selectConfigList(SysConfigQuery query);

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    int insertConfig(SysConfigSaveQuery config);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    int updateConfig(SysConfigSaveQuery config);

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    void deleteConfigByIds(Long[] configIds);

    /**
     * 加载参数缓存数据
     */
    void loadingConfigCache();

    /**
     * 清空参数缓存数据
     */
    void clearConfigCache();

    /**
     * 重置参数缓存数据
     */
    void resetConfigCache();

    /**
     * 校验参数键名是否唯一
     *
     * @param configId 参数配置ID
     * @param configKey 参数键名
     * @return 结果
     */
    boolean checkConfigKeyUnique(Long configId, String configKey);

}