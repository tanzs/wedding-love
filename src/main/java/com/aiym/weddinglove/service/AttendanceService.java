package com.aiym.weddinglove.service;

import com.aiym.weddinglove.extension.AttendanceExtension;
import com.aiym.weddinglove.vo.AttendanceQuery;
import com.aiym.weddinglove.vo.AttendanceVO;
import java.util.List;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;

/**
 * @author Tanzs
 * @date 2025/12/9 14:11
 * @description 搂席登记服务
 */
public interface AttendanceService {

    /**
     * 提交或更新登记信息（C端接口）
     * 逻辑：根据姓名查询，存在则更新，不存在则创建
     */
    Mono<AttendanceExtension> submitOrUpdate(AttendanceVO request);

    /**
     * 分页查询（管理端接口）
     */
    Mono<ListResult<AttendanceExtension>> list(AttendanceQuery query);

    /**
     * 审核/确认（管理端接口）
     * @param metadataName CRD的唯一标识 (metadata.name)
     * @param adminRemark 管理员备注
     */
    Mono<AttendanceExtension> approve(String metadataName, String adminRemark);

    /**
     * 批量审核/确认（管理端接口）
     */
    Mono<List<AttendanceExtension>> approveBatch(List<String> metadataNames, String adminRemark);

    /**
     * 删除（管理端接口）
     */
    Mono<Void> delete(String metadataName);

    /**
     * 批量删除（管理端接口）
     */
    Mono<Void> deleteBatch(List<String> metadataNames);
}
