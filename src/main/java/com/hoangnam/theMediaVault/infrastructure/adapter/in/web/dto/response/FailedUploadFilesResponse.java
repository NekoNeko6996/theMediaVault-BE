package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response;

import com.hoangnam.theMediaVault.application.port.in.dto.objects.FileHashAndReason;
import java.util.List;


public class FailedUploadFilesResponse {
    List<FileHashAndReason> errors;
}
