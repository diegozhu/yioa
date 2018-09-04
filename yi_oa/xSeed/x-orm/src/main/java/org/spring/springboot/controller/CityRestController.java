package org.spring.springboot.controller;

import org.spring.springboot.domain.City;
import org.spring.springboot.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class CityRestController {

    @Autowired
    private CityService cityService;

    @Value("${xseed.hi}")
    private String histr;

    @RequestMapping(value = "/api/city/{cityName}", method = RequestMethod.GET)
    public City findOneCity(@PathVariable String cityName) {
        return cityService.findCityByName(cityName);
    }


    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String hi() {
        return this.histr;
    }

}
