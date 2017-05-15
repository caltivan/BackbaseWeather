
package com.backbase.weather_challenge.model;

import com.backbase.weather_challenge.model.forecast.Clouds;
import com.backbase.weather_challenge.model.forecast.Coord;
import com.backbase.weather_challenge.model.forecast.Main;
import com.backbase.weather_challenge.model.forecast.Sys;
import com.backbase.weather_challenge.model.forecast.Weather;
import com.backbase.weather_challenge.model.forecast.Wind;

import java.util.List;

/**
 * Created by JGomez on 5/13/17.
 */
public class Forecast {

    public Coord coord;
    public List<Weather> weather = null;
    public String base;
    public Main main;
    public Wind wind;
    public Clouds clouds;
    public Integer dt;
    public Sys sys;
    public int id;
    public String name;
    public int cod;

}
