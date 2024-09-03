package top.tangyh.lamp.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.cache.redis2.CacheResult;
import top.tangyh.basic.cache.repository.CacheOps;
import top.tangyh.basic.model.cache.CacheKey;
import top.tangyh.lamp.common.cache.tenant.application.AllResourceApiCacheKeyBuilder;
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

    /**
     * 查询系统拥有的所有URI和权限关系
     * @return 系统中的所有资源
     */
    public Map<String, Set<String>> findAllApi() {
        CacheKey cacheKey = AllResourceApiCacheKeyBuilder.builder();
        CacheResult<Map<String, Set<String>>> result = cacheOps.get(cacheKey, (k) -> resourceBiz.findAllApi());
        return result.getValue();
    }

}
