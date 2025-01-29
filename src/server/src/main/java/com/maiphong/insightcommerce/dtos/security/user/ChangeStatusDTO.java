package com.maiphong.insightcommerce.dtos.security.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChangeStatusDTO {
    @NotNull(message = "Status is required")
    private Boolean status;
}
