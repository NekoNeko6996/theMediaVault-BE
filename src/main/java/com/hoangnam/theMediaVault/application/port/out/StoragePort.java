package com.hoangnam.theMediaVault.application.port.out;

import java.io.InputStream;

/**
 *  
 * Định nghĩa các tương tác với storage vật lý
 */
public interface StoragePort {
    String upload(String path, InputStream inputStream, long size, String contentType);
    void delete(String path);
    boolean exists(String path);
    String getDownloadUrl(String path);
    void rename(String newPath, String oldPath);
}
