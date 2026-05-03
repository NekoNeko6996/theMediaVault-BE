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
    /**
     * 
     * @param path
     * @param type = preview to set link to can open and review, download to set link only download file
     * @return 
     */
    String getDownloadUrl(String path, String type);
    void rename(String newPath, String oldPath);
}
