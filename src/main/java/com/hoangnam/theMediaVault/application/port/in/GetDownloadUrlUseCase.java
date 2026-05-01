package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.GetDowloadUrlQuery;
import com.hoangnam.theMediaVault.application.port.in.dto.result.GetDownLoadUrlResult;

/**
 *
 * 
 */
public interface GetDownloadUrlUseCase {
    GetDownLoadUrlResult execute(GetDowloadUrlQuery query);
}
