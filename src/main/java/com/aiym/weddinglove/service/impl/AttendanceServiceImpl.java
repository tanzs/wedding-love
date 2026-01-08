package com.aiym.weddinglove.service.impl;

import com.aiym.weddinglove.extension.AttendanceExtension;
import com.aiym.weddinglove.service.AttendanceService;
import com.aiym.weddinglove.vo.AttendanceQuery;
import com.aiym.weddinglove.vo.AttendanceVO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * @author Tanzs
 * @date 2025/12/9 14:15
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final ReactiveExtensionClient client;

    /**
     * 创建或更新
     */
    @Override
    public Mono<AttendanceExtension> submitOrUpdate(AttendanceVO request) {

        return client.fetch(AttendanceExtension.class, request.getName())
            .flatMap(result -> {
                if (ObjectUtils.isNotEmpty(result)) {
                    // ---- 更新逻辑 ----
                    AttendanceExtension existing = result;
                    updateSpec(existing.getSpec(), request);

                    existing.getSpec().setSubmitTime(LocalDateTime.now());
                    existing.getSpec().setApproved(false);

                    return client.update(existing);
                }
                // ---- 创建逻辑 ----
                return createNew(request);
            });
    }

    /**
     * 创建
     */
    private Mono<AttendanceExtension> createNew(AttendanceVO request) {
        AttendanceExtension extension = new AttendanceExtension();

        // metadata
        Metadata metadata = new Metadata();
        metadata.setName("attendance-" + UUID.randomUUID());
        extension.setMetadata(metadata);

        // spec
        AttendanceExtension.AttendanceRegistration spec =
            new AttendanceExtension.AttendanceRegistration();
        updateSpec(spec, request);

        spec.setSubmitTime(LocalDateTime.now());
        spec.setApproved(false);
        extension.setSpec(spec);

        // GVK（如果未自动处理）
        extension.setApiVersion("wedding.aiym.fun/v1alpha1");
        extension.setKind("AttendanceRegistration");

        return client.create(extension);
    }

    /**
     * 列表 分页 + 多条件查询
     */
    @Override
    public Mono<ListResult<AttendanceExtension>> list(AttendanceQuery query) {
        return client.listBy(AttendanceExtension.class, query.toListOptions(), query.toPageRequest());
    }

    /**
     * 审核记录
     */
    @Override
    public Mono<AttendanceExtension> approve(String metadataName, String adminRemark) {
        return client.fetch(AttendanceExtension.class, metadataName)
            .flatMap(extension -> {
                extension.getSpec().setApproved(true);

                if (StringUtils.isNotBlank(adminRemark)) {
                    extension.getSpec().setAdminRemark(adminRemark);
                }
                return client.update(extension);
            })
            .doOnSuccess(e -> log.info("✔ 已审核签到记录：{}", metadataName))
            .doOnError(e -> log.error("审核签到记录失败：{}", metadataName, e));
    }

    @Override
    public Mono<List<AttendanceExtension>> approveBatch(List<String> metadataNames, String adminRemark) {
        if (metadataNames == null || metadataNames.isEmpty()) {
            return Mono.just(List.of());
        }
        return Flux.fromIterable(metadataNames)
            .flatMap(name -> approve(name, adminRemark))
            .collectList();
    }

    /**
     * 删除
     */
    @Override
    public Mono<Void> delete(String metadataName) {
        return client.fetch(AttendanceExtension.class, metadataName)
            .flatMap(client::delete)
            .doOnSuccess(v -> log.info("✔ 已删除签到记录：{}", metadataName))
            .doOnError(e -> log.error("删除签到记录失败：{}", metadataName, e))
            .then();
    }

    @Override
    public Mono<Void> deleteBatch(List<String> metadataNames) {
        if (metadataNames == null || metadataNames.isEmpty()) {
            return Mono.empty();
        }
        return Flux.fromIterable(metadataNames)
            .flatMap(name -> delete(name))
            .then();
    }

    /**
     * 公共字段赋值
     */
    private void updateSpec(AttendanceExtension.AttendanceRegistration spec,
        AttendanceVO request) {

        spec.setName(request.getName());
        spec.setPhone(request.getPhone());
        spec.setEmail(request.getEmail());
        spec.setGuestCount(request.getGuestCount());
        spec.setRemark(request.getRemark());
    }
}
