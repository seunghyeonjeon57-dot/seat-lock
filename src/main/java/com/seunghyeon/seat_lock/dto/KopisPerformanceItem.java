package com.seunghyeon.seat_lock.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record KopisPerformanceItem(
        @JacksonXmlProperty(localName = "mt20id") String kopisId,
        @JacksonXmlProperty(localName = "prfnm") String name,
        @JacksonXmlProperty(localName = "prfpdfrom")  String startDate
) {
}
