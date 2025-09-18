package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dto.request.PermissionRequest;
import com.devteria.identity_service.dto.response.PermissionResponse;
import com.devteria.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")

public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);


}
