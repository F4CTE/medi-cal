package com.b3al.med.medi_cal.util;

import static com.b3al.med.medi_cal.util.MongoOffsetDateTimeWriter.DATE_FIELD;
import static com.b3al.med.medi_cal.util.MongoOffsetDateTimeWriter.OFFSET_FIELD;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;


public class MongoOffsetDateTimeReader implements Converter<Document, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(final Document document) {
        final Date dateTime = document.getDate(DATE_FIELD);
        final ZoneOffset offset = ZoneOffset.of(document.getString(OFFSET_FIELD));
        return OffsetDateTime.ofInstant(dateTime.toInstant(), offset);
    }

}
