package top.tangyh.lamp.common.satoken;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.tangyh.basic.base.R;
import top.tangyh.basic.constant.Constants;

import java.util.List;

/**
 * 参数API
 *
 * @author zuihou
 * @date 2020年04月02日22:53:56
 */
@FeignClient(name = "${" + Constants.PROJECT_PREFIX + ".feign.oauth-server:lamp-oauth-server}", path = "/anyone")
public interface StpApi {

    /**
     * 查询用户的资源权限列表
     * @return
     */
    @GetMapping("/getPermissionList")
    R<List<String>> getPermissionList();

    /**
     * 查询用户的角色列表
     * @return
     */
    @GetMapping("/getRoleList")
    R<List<String>> getRoleList();
}
