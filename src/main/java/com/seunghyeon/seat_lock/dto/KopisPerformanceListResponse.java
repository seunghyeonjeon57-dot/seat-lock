package com.seunghyeon.seat_lock.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "dbs")
public record KopisPerformanceListResponse(
        @JacksonXmlProperty(localName = "db")
        @JacksonXmlElementWrapper(useWrapping = false)
        List<KopisPerformanceItem> performances
) {
}
