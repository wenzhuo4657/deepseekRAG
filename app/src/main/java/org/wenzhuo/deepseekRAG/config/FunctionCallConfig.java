package org.wenzhuo.deepseekRAG.config;

import com.alibaba.cloud.ai.functioncalling.weather.WeatherProperties;
import com.alibaba.cloud.ai.functioncalling.weather.WeatherService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.wenzhuo.deepseekRAG.domain.MockWeather.MockWeatherService;

import java.util.function.Function;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/22
 * @description:
 */
@Configuration
public class FunctionCallConfig {

    @Bean
    @Description("根据地址和城市获取天气")
    public Function<MockWeatherService.Request,MockWeatherService.Response>  weatherFunction(){
        return  new MockWeatherService();
    }



//    以下为官方插件
@Bean
@ConditionalOnMissingBean
@Description("Use api.weather to get weather information.")
public WeatherService getWeatherServiceFunction() {
    WeatherProperties properties=new WeatherProperties();
    properties.setApiKey("sk-84121097193a4d6881d0ce6998fdd988");
    return new WeatherService(properties);
}

}
