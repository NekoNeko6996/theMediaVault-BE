package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.dto.response;

import com.hoangnam.theMediaVault.application.port.in.dto.list_object.FileHashAndReason;
import java.util.List;


public class FailedUploadFilesResponse {
    List<FileHashAndReason> errors;
}
