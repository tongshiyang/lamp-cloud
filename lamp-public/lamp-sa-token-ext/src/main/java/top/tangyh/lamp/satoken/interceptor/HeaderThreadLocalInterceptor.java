package top.tangyh.lamp.satoken.interceptor;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import top.tangyh.basic.boot.utils.WebUtils;
import top.tangyh.basic.context.ContextConstants;
import top.tangyh.basic.context.ContextUtil;
import top.tangyh.lamp.common.properties.IgnoreProperties;

import java.util.Map;

import static top.tangyh.basic.context.ContextConstants.JWT_KEY_COMPANY_ID;
import static top.tangyh.basic.context.ContextConstants.JWT_KEY_DEPT_ID;
import static top.tangyh.basic.context.ContextConstants.JWT_KEY_EMPLOYEE_ID;
import static top.tangyh.basic.context.ContextConstants.JWT_KEY_TOP_COMPANY_ID;

/**
 * 拦截器：
 * 将请求头数据，封装到BaseContextHandler(ThreadLocal)
 * <p>
 * 该拦截器要优先于系统中其他的业务拦截器
 * <p>
 * lamp-cloud 项目启用该拦截器， 通过 lamp.webmvc.header = true
 * lamp-boot 项目禁用该拦截器， 通过 lamp.webmvc.header = false
 * <p>
 *
 * @author zuihou
 * @date 2020/10/31 9:49 下午
 */
@Slf4j
@RequiredArgsConstructor
public class HeaderThreadLocalInterceptor implements AsyncHandlerInterceptor {
    private final IgnoreProperties ignoreProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        log.info("HeaderThreadLocalInterceptor url={}, method={}", request.getRequestURI(), request.getMethod());
        ContextUtil.setPath(WebUtils.getHeader(request, ContextConstants.PATH_HEADER));

        ContextUtil.setApplicationId(WebUtils.getHeader(request, ContextConstants.APPLICATION_ID_HEADER));
        ContextUtil.setClientId(WebUtils.getHeader(request, ContextConstants.CLIENT_ID_HEADER));
        ContextUtil.setLogTraceId(WebUtils.getHeader(request, ContextConstants.TRACE_ID_HEADER));
        Map<String, String> localMap = ContextUtil.getLocalMap();
        localMap.forEach(MDC::put);

        ContextUtil.setGrayVersion(WebUtils.getHeader(request, ContextConstants.GRAY_VERSION));

        // 不需要登录也能访问
        if (ignoreProperties.isIgnoreUser(request.getMethod(), request.getRequestURI())) {
            log.debug("access filter not execute");
            return true;
        }
        SaSession tokenSession = StpUtil.getTokenSession();
//        SaSession tokenSession = StpUtil.getSession();

        if (tokenSession != null) {
            Long userId = (Long) tokenSession.getLoginId();
            long topCompanyId = tokenSession.getLong(JWT_KEY_TOP_COMPANY_ID);
            long companyId = tokenSession.getLong(JWT_KEY_COMPANY_ID);
            long deptId = tokenSession.getLong(JWT_KEY_DEPT_ID);
            long employeeId = tokenSession.getLong(JWT_KEY_EMPLOYEE_ID);

            //6, 转换，将 token 解析出来的用户身份 和 解码后的tenant、Authorization 重新封装到请求头
            ContextUtil.setUserId(userId);
            ContextUtil.setEmployeeId(employeeId);
            ContextUtil.setCurrentCompanyId(companyId);
            ContextUtil.setCurrentTopCompanyId(topCompanyId);
            ContextUtil.setCurrentDeptId(deptId);
            MDC.put(ContextConstants.USER_ID_HEADER, String.valueOf(userId));
            MDC.put(ContextConstants.EMPLOYEE_ID_HEADER, String.valueOf(employeeId));
        } else {
            ContextUtil.setUserId(WebUtils.getHeader(request, ContextConstants.USER_ID_HEADER));
            ContextUtil.setEmployeeId(WebUtils.getHeader(request, ContextConstants.EMPLOYEE_ID_HEADER));
            ContextUtil.setCurrentCompanyId(WebUtils.getHeader(request, ContextConstants.CURRENT_COMPANY_ID_HEADER));
            ContextUtil.setCurrentTopCompanyId(WebUtils.getHeader(request, ContextConstants.CURRENT_TOP_COMPANY_ID_HEADER));
            ContextUtil.setCurrentDeptId(WebUtils.getHeader(request, ContextConstants.CURRENT_DEPT_ID_HEADER));
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ContextUtil.remove();
    }
}
