-- Bảng `users`: Quản lý, phân quyền người dùng
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

-- Bảng `files`: Quản lý cả File và Folder (Cấu trúc cây)
CREATE TABLE `files` (
    `id` VARCHAR(36) NOT NULL,
    `owner_id` VARCHAR(36) NOT NULL,
    `parent_id` VARCHAR(36) DEFAULT NULL, -- NULL nghĩa là nằm ở thư mục gốc (root)
    
    -- Thông tin cơ bản
    `name` VARCHAR(255) NOT NULL,
    `item_type` ENUM('FILE', 'FOLDER') NOT NULL,
    `mime_type` VARCHAR(100) DEFAULT NULL, -- Ví dụ: 'application/pdf', 'image/png' (NULL nếu là FOLDER)
    `extension` VARCHAR(20) DEFAULT NULL, -- Ví dụ: 'pdf', 'png', 'docx'
    `size_bytes` BIGINT NOT NULL DEFAULT 0,
    
    -- Vị trí lưu trữ thực tế (S3, MinIO, Local Disk...)
    `storage_path` VARCHAR(512) DEFAULT NULL, -- Cần thiết cho FILE, NULL cho FOLDER
    `file_hash` VARCHAR(64) DEFAULT NULL, -- MD5 hoặc SHA256 để chống trùng lặp file (Deduplication)
    
    -- Trạng thái & Phân loại
    `is_starred` TINYINT(1) NOT NULL DEFAULT 0,
    `is_trashed` TINYINT(1) NOT NULL DEFAULT 0, -- Quản lý thùng rác
    
    -- Metadata thời gian
    `create_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `trashed_at` DATETIME DEFAULT NULL, -- Thời điểm đưa vào thùng rác để dọn dẹp tự động (VD: sau 30 ngày)

    -- Ràng buộc & Index
    PRIMARY KEY (`id`),
    INDEX `idx_owner` (`owner_id`),
    INDEX `idx_parent` (`parent_id`),
    INDEX `idx_is_trashed` (`is_trashed`),
    CONSTRAINT `fk_files_owner` FOREIGN KEY (`owner_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_files_parent` FOREIGN KEY (`parent_id`) REFERENCES `files`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Bảng `file_shares`: Quản lý quyền chia sẻ (Sharing & Permissions)
CREATE TABLE `file_shares` (
    `id` VARCHAR(36) NOT NULL,
    `file_id` VARCHAR(36) NOT NULL,
    `shared_by` VARCHAR(36) NOT NULL,
    `shared_with` VARCHAR(36) DEFAULT NULL, -- ID người được chia sẻ (NULL nếu chia sẻ public bằng link)
    
    -- Quyền hạn & Token
    `permission` ENUM('VIEW', 'COMMENT', 'EDIT') NOT NULL DEFAULT 'VIEW',
    `public_token` VARCHAR(64) DEFAULT NULL, -- Chuỗi random sinh ra cho public link (VD: drive.com/s/xyz123)
    `expires_at` DATETIME DEFAULT NULL, -- Hạn chót của link/quyền chia sẻ
    
    `create_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Ràng buộc & Index
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_public_token` (`public_token`),
    INDEX `idx_file_id` (`file_id`),
    INDEX `idx_shared_with` (`shared_with`),
    CONSTRAINT `fk_share_file` FOREIGN KEY (`file_id`) REFERENCES `files`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_share_by` FOREIGN KEY (`shared_by`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_share_with` FOREIGN KEY (`shared_with`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;