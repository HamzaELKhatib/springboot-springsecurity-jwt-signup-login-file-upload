package com.rest.shared.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String latD;

    private String ns;

    private String longD;

    private String ew;

    private String city;

    private String state;
}
