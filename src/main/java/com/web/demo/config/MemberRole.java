package com.web.demo.config;

import lombok.Getter;

// 사용자 권한
@Getter
public enum MemberRole {
	ADMIN("ROLE_ADMIN"),
	SUBADMIN("ROLE_SUBADMIN"),
    BASIC("ROLE_BASIC");

    private final String role;

    MemberRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return this.role;
    }
    public String getRole() {
        return role;
    }
}
