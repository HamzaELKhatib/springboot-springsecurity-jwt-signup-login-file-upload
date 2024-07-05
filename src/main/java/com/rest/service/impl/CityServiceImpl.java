package com.rest.service.impl;

import com.rest.io.entity.CityEntity;
import com.rest.io.repositories.CityRepository;
import com.rest.service.CityService;
import com.rest.shared.Utils;
import com.rest.shared.dto.CityDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Service
public class CityServiceImpl implements CityService {
    private static final Logger LOGGER = Logger.getLogger(CityServiceImpl.class.getName());
    private static final String FILE_NAME = "src/main/resources/assets/csv/cities.csv";
    private final List<CityDto> cities = new ArrayList<>();

    @Autowired
    Utils utils;

    @Autowired
    CityRepository cityRepository;

    @PostConstruct
    public void init() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = br.readLine()) != null) {
                String[] cityCsv = line.split(",");
                CityDto cityObj = new CityDto();
                cityObj.setLatD(cityCsv[0]);
                cityObj.setNs(cityCsv[1]);
                cityObj.setLongD(cityCsv[2]);
                cityObj.setEw(cityCsv[3]);
                cityObj.setCity(cityCsv[4]);
                cityObj.setState(cityCsv[5]);
                cities.add(cityObj);
            }
            LOGGER.info("Cities loaded: " + cities.size());
        } catch (IOException e) {
            LOGGER.info("Error while reading file: " + e.getMessage());
        }
    }

    @Override
    public List<CityDto> getCities() {
        return cities;
    }

    @Override
    public CityDto addCity(CityDto cityDto) {

        CityEntity cityEntity = new CityEntity();

        BeanUtils.copyProperties(cityDto, cityEntity);

        CityEntity storedCityDetails = cityRepository.save(cityEntity);

        CityDto returnValue = new CityDto();

        BeanUtils.copyProperties(storedCityDetails, returnValue);

        return returnValue;
    }

    @Override
    public CityDto addRandomCity() {

        CityEntity cityEntity = new CityEntity();

        CityDto randomCityDto = CityDto.builder()
                .latD(utils.generateRandomInt())
                .ns(utils.generateRandomValues(2))
                .longD(utils.generateRandomInt())
                .ew(utils.generateRandomValues(2))
                .city(utils.generateRandomValues(10))
                .state(utils.generateRandomValues(20))
                .build();

        BeanUtils.copyProperties(randomCityDto, cityEntity);

        CityEntity storedCityDetails = cityRepository.save(cityEntity);

        CityDto returnValue = new CityDto();

        BeanUtils.copyProperties(storedCityDetails, returnValue);

        return returnValue;
    }

}
