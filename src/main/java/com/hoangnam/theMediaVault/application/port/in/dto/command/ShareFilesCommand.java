package com.hoangnam.theMediaVault.application.port.in.dto.command;

import com.hoangnam.theMediaVault.domain.model.FilePermission;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;


@Value
@AllArgsConstructor
public class ShareFilesCommand {
    String ownerId;
    List<String> fileToShareIds;
    List<String> userToShareIds;
    FilePermission permission;
    DateTime expiration;
}
