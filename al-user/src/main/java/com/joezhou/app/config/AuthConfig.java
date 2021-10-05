package com.joezhou.app.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JoeZhou
 */
@Slf4j
@Component
public class AuthConfig implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String app = httpServletRequest.getParameter("app");
        if (app == null) {
            app = httpServletRequest.getHeader("app");
        }
        log.info("从请求参数/请求头中获取app标识：{}", app);
        return app;
    }
}
