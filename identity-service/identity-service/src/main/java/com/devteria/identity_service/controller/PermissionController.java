package com.devteria.identity_service.controller;


import com.devteria.identity_service.dto.request.PermissionRequest;
import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.dto.response.PermissionResponse;
import com.devteria.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;
    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();

    }
    @DeleteMapping("/{permission}")
    ApiResponse<Void> deletePermission(@PathVariable String permission){
        permissionService.delete(permission);
        return ApiResponse.<Void>builder().build();
    }

}
