package com.rest.service;

import com.rest.shared.dto.CityDto;

import java.util.List;

public interface CityService {

    List<CityDto> getCities();

    CityDto addCity(CityDto cityDto);

    CityDto addRandomCity();
}
