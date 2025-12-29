package com.taoge.framework.es.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class GeoPointSerializer extends StdSerializer<GeoPoint> {
    public GeoPointSerializer() {
        super(GeoPoint.class);
    }

    protected GeoPointSerializer(Class<GeoPoint> t) {
        super(t);
    }

    @Override
    public void serialize(GeoPoint geoPoint, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("lat", geoPoint.getLat());
        jsonGenerator.writeNumberField("lon", geoPoint.getLon());
        jsonGenerator.writeEndObject();
    }
}
