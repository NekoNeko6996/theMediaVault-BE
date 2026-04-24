package com.hoangnam.theMediaVault.domain.model;

import com.hoangnam.theMediaVault.domain.exception.DomainException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class File {

    String id;
    User owner;
    File parent;
    String name;
    FileItemType itemType;
    String mimeType;
    String extension;
    Long sizeBytes;
    String storagePath;
    String fileHash;
    boolean isStarred;
    boolean isTrashed;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    LocalDateTime trashedAt;

    public boolean isTrashed() {
        return this.isTrashed && trashedAt != null;
    }

    public File toggleStarred() {
        return this.toBuilder().isStarred(!this.isStarred).build();
    }

    public File throwToTrash() {
        if (this.isTrashed) {
            throw new DomainException(String.format("The file %s is already in trash.", this.name));
        }

        // quăng nó vào thùng rác
        return this.toBuilder()
                .isTrashed(true)
                .trashedAt(LocalDateTime.now())
                .build();
    }

    public File takenFormTrash() {
        if (!this.isTrashed) {
            throw new DomainException(String.format("The file %s isn't in the trash can.", this.name));
        }

        // lấy nó lại từ thùng rác
        return this.toBuilder()
                .isTrashed(false)
                .build();
    }

    public File rename(String newName) {
        return this.toBuilder()
                .name(newName)
                .updateAt(LocalDateTime.now())
                .build();
    }

    public File moveTo(File newParent) {
        return this.toBuilder()
                .parent(newParent)
                .updateAt(LocalDateTime.now())
                .build();
    }

    public boolean isFolder() {
        return this.itemType == FileItemType.FOLDER;
    }

    public boolean isFile() {
        return this.itemType == FileItemType.FILE;
    }
    
    public String getRootDir() {
        return "user_" + this.owner.getId() + "/";
    }

    public static File createFile(
            User owner,
            File parent,
            String name,
            String mimeType,
            String extension,
            Long sizeBytes,
            String storagePath,
            String fileHash) {
        return File.builder()
                .id(UUID.randomUUID().toString())
                .owner(owner)
                .parent(parent)
                .name(name)
                .itemType(FileItemType.FILE)
                .mimeType(mimeType)
                .extension(extension)
                .sizeBytes(sizeBytes)
                .storagePath(storagePath)
                .fileHash(fileHash)
                .isStarred(false)
                .isTrashed(false)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    public static File createFolder(
            User owner,
            File parent,
            String name,
            String storagePath) {
        return File.builder()
                .id(UUID.randomUUID().toString())
                .owner(owner)
                .parent(parent)
                .name(name)
                .itemType(FileItemType.FOLDER)
                .mimeType(null)
                .extension(null)
                .sizeBytes(0L)
                .storagePath(storagePath)
                .fileHash(null)
                .isStarred(false)
                .isTrashed(false)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }
}
