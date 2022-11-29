package com.rest.io.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "city")
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "lat_d")
    private String latD;

    @Column(name = "ns")
    private String ns;

    @Column(name = "long_d")
    private String longD;

    @Column(name = "ew")
    private String ew;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

}
