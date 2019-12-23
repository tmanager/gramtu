package com.frank.gramtu.web.feature;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 特色服务管理Repository.
 *
 * @author 张孝党 2019/12/23.
 * @version V1.00.
 * <p>
 * 更新履历： V1.00 2019/12/23 张孝党 创建.
 */
@Mapper
@Repository
public interface FeatureRepository {

    /**
     * 查询总条数.
     */
    int getCnt(Map<String, Object> param);

    /**
     * 查询明细.
     */
    List<Map<String, String>> getServiceList(Map<String, Object> param);
}
