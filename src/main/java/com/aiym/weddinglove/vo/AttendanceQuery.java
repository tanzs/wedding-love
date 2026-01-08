package com.aiym.weddinglove.vo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.PageRequest;
import run.halo.app.extension.index.query.QueryFactory;
import run.halo.app.extension.router.SortableRequest;

/**
 * @author Tanzs
 * @date 2025/12/9 15:23
 * @description
 */
public class AttendanceQuery extends SortableRequest {

    private String name;

    private String phone;

    private String email;

    private Integer guestCount;

    private String remark;

    private Boolean approved;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public AttendanceQuery(ServerWebExchange exchange) {
        super(exchange);
        var params = exchange.getRequest().getQueryParams();
        this.name = params.getFirst("name");
        this.phone = params.getFirst("phone");
        this.email = params.getFirst("email");
        this.remark = params.getFirst("remark");
        var guestCountStr = params.getFirst("guestCount");
        if (guestCountStr != null) {
            try {
                this.guestCount = Integer.parseInt(guestCountStr);
            } catch (NumberFormatException ignored) {
                this.guestCount = null;
            }
        }
        var approvedStr = params.getFirst("approved");
        if (approvedStr != null) {
            this.approved = Boolean.valueOf(approvedStr);
        }
    }

    @Override
    public ListOptions toListOptions() {
        var builder = ListOptions.builder(super.toListOptions());

        if (StringUtils.isNotBlank(getName())) {
            builder.andQuery(QueryFactory.contains("spec.name", getName()));
        }
        if (StringUtils.isNotBlank(getEmail())) {
            builder.andQuery(QueryFactory.equal("spec.email", getEmail()));
        }
        if (StringUtils.isNotBlank(getPhone())) {
            builder.andQuery(QueryFactory.equal("spec.phone", getPhone()));
        }
        if (getApproved() != null) {
            builder.andQuery(QueryFactory.equal("spec.approved", getApproved().toString()));
        }
        return builder.build();
    }

    @Override
    public PageRequest toPageRequest() {
        return super.toPageRequest();
    }
}
