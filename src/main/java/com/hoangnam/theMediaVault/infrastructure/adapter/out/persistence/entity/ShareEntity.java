package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence.entity;

import com.hoangnam.theMediaVault.domain.model.FilePermission;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Builder
@Table(name = "shares")
public class ShareEntity  {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_by", nullable = false)
    private UserEntity sharedBy;
    
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FilePermission permission = FilePermission.VIEW; 
    
    @Column(name = "public_token", length = 64, unique = true)
    private String publicToken;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    
    /**
     * Tạo bảng tạm để tham chiếu đến bảng share_items trong db(do chỉ có 2 trường id[thực chất là bảng tạm để kết nối many to many giữa file và share])
     * Việc tạo bảng tạm thế này giúp không cần tạo thêm Entity phụ bằng java
     */
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "share_items",
            joinColumns = @JoinColumn(name = "share_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private Set<FileEntity> files = new HashSet();
    
    
    /**
     * Tạo bảng tạm để tham chiếu đến bảng share_recipients trong db(do chỉ có 2 trường id[thực chất là bảng tạm để kết nối many to many giữa user và share])
     * Việc tạo bảng tạm thế này giúp không cần tạo thêm Entity phụ bằng java
     */
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "share_recipients",
        joinColumns = @JoinColumn(name = "share_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> recipients = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;    
}
