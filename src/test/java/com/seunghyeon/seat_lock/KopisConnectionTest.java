package com.seunghyeon.seat_lock;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.seunghyeon.seat_lock.dto.KopisPerformanceListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class KopisConnectionTest {

    @Test
    void kopis_응답을_객체로_변환한다() throws Exception {
        String serviceKey = "0e4deb99e3234046a7740d9cdf087f2e";
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.kopis.or.kr/openApi/restful/pblprfr"
                + "?service=" + serviceKey
                + "&stdate=20260101"
                + "&eddate=20261231"
                + "&cpage=1"
                + "&rows=10";

        byte[] responseBytes = restTemplate.getForObject(url, byte[].class);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KopisPerformanceListResponse result = xmlMapper.readValue(responseBytes, KopisPerformanceListResponse.class);

        System.out.println(result.performances().get(0).name());
    }
}
