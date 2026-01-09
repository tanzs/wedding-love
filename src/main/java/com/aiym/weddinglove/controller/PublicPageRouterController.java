package com.aiym.weddinglove.controller;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.theme.TemplateNameResolver;

/**
 * @author Tanzs
 * @date 2025/12/8 11:20
 * @description 自定义公共页面访问路由
 */
@Configuration
@RequiredArgsConstructor
public class PublicPageRouterController {

    private final TemplateNameResolver templateNameResolver;
    private final static String PAGE_BASE_PATH = "/wedding-love";

    @Bean
    RouterFunction<ServerResponse> multiPageRoutes() {
        // 将多个路由规则组合在一起
        return route()
            .GET(PAGE_BASE_PATH + "/index", this::renderIndex)
            .build();
    }

    Mono<ServerResponse> renderIndex(ServerRequest request) {
        // 可以在这里准备需要传递给模板的数据
        Map<String, Object> model = new HashMap<>();
        model.put("message", "wedding");

        // 解析模板，如果主题没有提供，则使用插件自带的默认模板
        return templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "wedding")
            .flatMap(templateName -> ServerResponse.ok().render(templateName, model));
    }



}
