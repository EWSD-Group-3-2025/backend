package org.teamSmurfs.backend.config.deprecated;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;

import java.time.Instant;

@Component
public class DeprecatedRouteInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        if (handler instanceof HandlerMethod method) {
            DeprecatedRoute deprecatedRoute = method.getMethodAnnotation(DeprecatedRoute.class);
            if (deprecatedRoute != null) {
                ApiResponse apiResponse = ApiResponse.builder()
                        .success(0)
                        .code(HttpServletResponse.SC_GONE)
                        .message(deprecatedRoute.message())
                        .data(false)
                        .build();

                ApiResponse finalResponse = ResponseUtil.buildResponse(request, apiResponse, requestStartTime).getBody();

                response.setStatus(HttpServletResponse.SC_GONE);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(finalResponse));

                return false;
            }
        }
        return true;
    }
}
