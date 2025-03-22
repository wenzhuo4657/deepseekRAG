package org.wenzhuo.deepseekRAG.domain.MockWeather;




import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.function.Function;

/**
 * @author: wenzhuo4657
 * @date: 2025/3/22
 * @description:
 */
public class MockWeatherService  implements Function<MockWeatherService.Request, MockWeatherService.Response> {
    public enum Unit { C, F }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Request(
            @JsonPropertyDescription("地址，例如湖滨区") String location,
            @JsonPropertyDescription("城市，例如C、F") Unit unit) {}
    public record Response(double temp, Unit unit) {}
    @Override
    public Response apply(Request request) {
        return new Response(30.0,Unit.C);
    }
}
