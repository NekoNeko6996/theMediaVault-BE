package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response;

import com.hoangnam.theMediaVault.application.port.in.dto.objects.FileIdAndReason;
import java.util.List;


public class FailedMoveFIlesResponse {
    List<FileIdAndReason> errors;
}
