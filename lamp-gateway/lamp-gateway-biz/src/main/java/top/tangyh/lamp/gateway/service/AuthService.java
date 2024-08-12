package top.tangyh.lamp.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.cache.repository.CacheOps;
import top.tangyh.lamp.oauth.biz.ResourceBiz;

import java.util.Map;
import java.util.Set;

/**
 * 网关权限认证服务
 *
 * @author tangyh
 * @since 2024/8/8 11:06
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final ResourceBiz resourceBiz;
    private final CacheOps cacheOps;

    public Map<String, Set<String>> findAllApi() {
//        cacheOps.get()
//         查询系统中配置的URI和权限关系
        return resourceBiz.findAllApi();
    }

}
