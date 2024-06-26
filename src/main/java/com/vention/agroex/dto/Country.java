package com.vention.agroex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class Country {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Country name can`t be blank")
    @Size(min = 1, max = 64, message = "Country name must contain between 1 and 64 characters")
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> regions;
}
