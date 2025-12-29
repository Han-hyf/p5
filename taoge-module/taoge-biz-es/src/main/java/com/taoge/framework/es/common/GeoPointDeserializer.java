package com.taoge.framework.es.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class GeoPointDeserializer extends StdDeserializer<GeoPoint> {
    public GeoPointDeserializer() {
        super(GeoPoint.class);
    }

    protected GeoPointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public GeoPoint deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        JsonNode latNode = node.get("lat");
        double lat = latNode.asDouble();

        JsonNode lonNode = node.get("lon");
        double lon = lonNode.asDouble();

        return new GeoPoint(lat, lon);
    }
}
