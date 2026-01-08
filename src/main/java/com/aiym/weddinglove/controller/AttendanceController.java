package com.aiym.weddinglove.controller;

import com.aiym.weddinglove.extension.AttendanceExtension;
import com.aiym.weddinglove.service.AttendanceService;
import com.aiym.weddinglove.vo.AttendanceQuery;
import com.aiym.weddinglove.vo.AttendanceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import run.halo.app.plugin.ApiVersion;

/**
 * @author Tanzs
 * @date 2025/12/9 16:35
 * @description
 */
@ApiVersion("api.wedding.aiym.fun/v1alpha1")
@RequestMapping("/attendance")
@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/submit")
    public Mono<AttendanceExtension> submit(@RequestBody AttendanceVO attendanceRequest) {
        return attendanceService.submitOrUpdate(attendanceRequest);
    }

    @GetMapping("/list")
    public Mono<ListResult<AttendanceExtension>> list(ServerWebExchange exchange) {
        AttendanceQuery query = new AttendanceQuery(exchange);
        return attendanceService.list(query);
    }


}
