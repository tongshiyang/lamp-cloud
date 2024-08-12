package top.tangyh.lamp.common.satoken;

import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.tangyh.basic.base.R;

import java.util.Collections;
import java.util.List;

/**
 * sa-token 权限实现
 * @author tangyh
 * @since 2024/8/6 21:46
 */
@Slf4j
@RequiredArgsConstructor
public class StpInterfaceFeignImpl implements StpInterface {
    private final StpApi stpApi;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        R<List<String>> permissionList = stpApi.getPermissionList();
        if (permissionList.getIsSuccess()) {
            return permissionList.getData();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        R<List<String>> roleList = stpApi.getRoleList();
        if (roleList.getIsSuccess()) {
            return roleList.getData();
        }
        return Collections.emptyList();
    }
}
