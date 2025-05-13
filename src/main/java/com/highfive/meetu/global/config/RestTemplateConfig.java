package com.highfive.meetu.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 전역 설정 클래스: RestTemplate 빈 등록
 * 외부 API 호출 시 RestTemplate을 주입받아 사용합니다.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate Bean 등록
     * - 기본 JSON 처리 외에도 text/xml, application/xml도 JSON으로 파싱할 수 있도록 설정합니다.
     *
     * @return 커스터마이징된 RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // JSON 컨버터가 text/xml, application/xml 도 처리할 수 있도록 MediaType 확장
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mc = (MappingJackson2HttpMessageConverter) converter;
                List<MediaType> types = new ArrayList<>(mc.getSupportedMediaTypes());
                types.add(MediaType.TEXT_XML);
                types.add(MediaType.APPLICATION_XML);
                mc.setSupportedMediaTypes(types);
                break;
            }
        }

        return restTemplate;
    }
}
