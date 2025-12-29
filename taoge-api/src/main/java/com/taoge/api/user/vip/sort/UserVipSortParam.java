package com.taoge.api.user.vip.sort;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

import java.util.List;

@Data
public class UserVipSortParam extends BaseParam {
    @NotNull
    private List<Long> ids;
}
