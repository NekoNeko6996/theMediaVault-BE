package com.hoangnam.theMediaVault.domain.model;

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

    public boolean isFolder() {
        return this.itemType == FileItemType.FOLDER;
    }

    public boolean isFile() {
        return this.itemType == FileItemType.FILE;
    }

    public String getRootDir() {
        return "user_" + this.owner.getId() + "/";
    }

    public File duplicate(String newId, String newUniqueName, File targetParent) {
        return this.toBuilder()
                .id(newId)
                .name(newUniqueName)
                .parent(targetParent)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
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

    public static File createFile(
            String id,
            User owner,
            File parent,
            String name,
            String mimeType,
            String extension,
            Long sizeBytes,
            String storagePath,
            String fileHash) {
        return File.builder()
                .id(id)
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
