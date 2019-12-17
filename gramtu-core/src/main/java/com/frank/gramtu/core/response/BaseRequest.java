package com.frank.gramtu.core.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseRequest {

    // 当前页码，如果等于空，表示不分页
    private int currentpage;

    // 开始检索index
    private int startindex;

    // 每页显示条数，如果等于空，表示不分页
    private int pagesize;

    // 请求次数
    private int draw;
}
