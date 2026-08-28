package com.equipsphere.dto.booking;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatusUpdateDTO {

    @NotBlank(message = "Status is required (APPROVED, REJECTED, CANCELLED, COMPLETED)")
    private String status;

    private String adminRemark;
}
