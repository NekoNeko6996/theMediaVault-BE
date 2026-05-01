package com.hoangnam.theMediaVault.application.port.out;

import com.hoangnam.theMediaVault.domain.model.File;
import java.util.List;

/**
 *
 * 
 */
public interface FileAndUserTransactionPort {
    void saveFilesAndUpdateStorage(String ownerId, List<File> files, long sizeDelta);
    void deleteFilesAndRefundQuotaStorage(String ownerId, List<File> files, long refundQuota);
}
