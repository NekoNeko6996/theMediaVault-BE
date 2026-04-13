/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  tranp
 * Created: Apr 12, 2026
 */

CREATE TABLE `users` (
    -- Định danh chính (UUID)
    `id` VARCHAR(36) NOT NULL,
    
    -- Thông tin đăng nhập & định danh
    `username` VARCHAR(50) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password_hash` VARCHAR(255) NOT NULL,
    
    -- Trạng thái & Phân quyền
    `is_active` TINYINT(1) NOT NULL DEFAULT 0, -- MySQL lưu Boolean là TINYINT(1)
    `role` VARCHAR(20) NOT NULL, -- Lưu Enum dưới dạng String (ADMIN, USER...)
    
    -- Xác thực
    `email_verified_at` DATETIME DEFAULT NULL,
    
    -- Quản lý dung lượng (Lưu dạng bytes nên dùng BIGINT)
    `storage_limit` BIGINT NOT NULL DEFAULT 5368709120, -- Mặc định 5GB
    `used_storage` BIGINT NOT NULL DEFAULT 0,
    
    -- Metadata thời gian
    `create_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `last_login_at` DATETIME DEFAULT NULL,
    `deleted_at` DATETIME DEFAULT NULL, -- Dùng cho Soft Delete

    -- Ràng buộc
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    INDEX `idx_role` (`role`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;