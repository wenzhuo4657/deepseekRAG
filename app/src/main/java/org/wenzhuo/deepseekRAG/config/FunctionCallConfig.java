package org.wenzhuo.deepseekRAG.config;

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

}
