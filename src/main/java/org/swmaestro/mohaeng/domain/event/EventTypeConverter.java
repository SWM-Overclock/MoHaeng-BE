package org.swmaestro.mohaeng.domain.event;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EventTypeConverter implements AttributeConverter<EventType, String> {

    @Override
    public String convertToDatabaseColumn(EventType eventType) {
        if (eventType == null) {
            return null;
        }
        return eventType.getValue();
    }

    @Override
    public EventType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        for (EventType eventType : EventType.values()) {
            if (eventType.getValue().equals(dbData)) {
                return eventType;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + dbData);
    }
}

