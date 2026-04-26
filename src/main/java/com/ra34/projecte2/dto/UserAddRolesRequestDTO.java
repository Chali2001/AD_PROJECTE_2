package com.ra34.projecte2.dto;

import java.util.List;

public class UserAddRolesRequestDTO {

    private List<Long> roleIds;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
