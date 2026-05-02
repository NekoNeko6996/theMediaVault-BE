package com.hoangnam.theMediaVault.application.port.in;

import com.hoangnam.theMediaVault.application.port.in.dto.command.GetFileInfoQuery;
import com.hoangnam.theMediaVault.domain.model.File;


public interface GetFileInfoUseCase {
    File execute(GetFileInfoQuery query);
}
