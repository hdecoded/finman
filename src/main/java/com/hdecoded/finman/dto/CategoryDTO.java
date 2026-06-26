package com.hdecoded.finman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;
    private String categoryName;
    private String description;
    private String icon;
    private String type;
    private Long profileId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
