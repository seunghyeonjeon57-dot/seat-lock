package com.seunghyeon.seat_lock;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class KopisConnectionTest {

    @Test
    void kopis_서버에_요청하면_응답이_온다() {
        String serviceKey = "0e4deb99e3234046a7740d9cdf087f2e";
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.kopis.or.kr/openApi/restful/pblprfr"
                + "?service=" + serviceKey
                + "&stdate=20260101"
                + "&eddate=20261231"
                + "&cpage=1"
                + "&rows=10";

        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
    }
}
