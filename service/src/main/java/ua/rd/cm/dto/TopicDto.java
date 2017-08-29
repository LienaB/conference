package ua.rd.cm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TopicDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
}
