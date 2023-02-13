package com.example.healthExpert.parse

class WeatherParse {
    var coord: CoordDTO? = null
    var weather: List<WeatherDTO>? = null
    var base: String? = null
    var main: MainDTO? = null
    var visibility: Int? = null
    var wind: WindDTO? = null
    var clouds: CloudsDTO? = null
    var dt: Int? = null
    var sys: SysDTO? = null
    var timezone: Int? = null
    var id: Int? = null
    var name: String? = null
    var cod: Int? = null

    class CoordDTO {
        var lon: Double? = null
        var lat: Double? = null
    }

    class MainDTO {
        var temp: Double? = null
        var feels_like: Double? = null
        var temp_min: Double? = null
        var temp_max: Double? = null
        var pressure: Int? = null
        var humidity: Int? = null
        var sea_level: Int? = null
        var grnd_level: Int? = null
    }

    class WindDTO {
        var speed: Double? = null
        var deg: Int? = null
        var gust: Double? = null
    }

    class CloudsDTO {
        var all: Int? = null
    }

    class SysDTO {
        var type: Int? = null
        var id: Int? = null
        var country: String? = null
        var sunrise: Int? = null
        var sunset: Int? = null
    }

    class WeatherDTO {
        var id: Int? = null
        var main: String? = null
        var description: String? = null
        var icon: String? = null
    }
}