package developer.citypalestine8936ps.new_city_feature

import developer.citypalestine8936ps.new_city_feature.model.NewCityData

interface CityListener {
    fun onClickCity(city: NewCityData)
}