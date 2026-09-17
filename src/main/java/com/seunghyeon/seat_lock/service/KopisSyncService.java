package com.seunghyeon.seat_lock.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.seunghyeon.seat_lock.dto.KopisPerformanceItem;
import com.seunghyeon.seat_lock.dto.KopisPerformanceListResponse;
import com.seunghyeon.seat_lock.entity.Event;
import com.seunghyeon.seat_lock.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KopisSyncService {
    private final EventRepository repository;
    @Value("${kopis.service-key}")
    private String serviceKey;

    public List<Event> fetchAndConvert()  throws IOException {


        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.kopis.or.kr/openApi/restful/pblprfr"
                + "?service=" + serviceKey
                + "&stdate=20260101"
                + "&eddate=20261231"
                + "&cpage=1"
                + "&rows=50";

        byte[] responseBytes = restTemplate.getForObject(url, byte[].class);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KopisPerformanceListResponse result = xmlMapper.readValue(responseBytes, KopisPerformanceListResponse.class);

        List<Event> events= new ArrayList<>();
        for(KopisPerformanceItem item : result.performances()) {
            LocalDate date = LocalDate.parse(item.startDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            Instant startedAt = date.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();


            Event event = Event.builder().name(item.name())
                    .kopisId(item.kopisId())
                    .startedAt(startedAt)
                    .build();
        events.add(event);
        }
        return events;
    }
}

