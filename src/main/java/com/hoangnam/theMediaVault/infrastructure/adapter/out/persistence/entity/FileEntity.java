package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity;

import com.hoangnam.theMediaVault.domain.model.FileItemType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private FileEntity parent;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private FileItemType itemType;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(length = 20)
    private String extension;

    @Builder.Default
    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes = 0L;

    @Column(name = "storage_path", length = 512)
    private String storagePath;

    @Column(name = "file_hash", length = 64)
    private String fileHash;

    @Builder.Default
    @Column(name = "is_starred", nullable = false)
    private boolean isStarred = false;

    @Builder.Default
    @Column(name = "is_trashed", nullable = false)
    private boolean isTrashed = false;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "trashed_at")
    private LocalDateTime trashedAt;
}