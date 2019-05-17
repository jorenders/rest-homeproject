package be.renders.homeproject;

import be.renders.homeproject.repository.domain.Meting;

import java.sql.Timestamp;

/**
 * Created by joren on 17/05/2019.
 */
public class TestHelperClass {

    public static Meting createMeting(long id, Timestamp datum, Long sensorSource, Double waarde) {
        Meting result = new Meting();
        result.setId(id);
        result.setDatum(datum);
        result.setSensorsource(sensorSource);
        result.setWaarde(waarde);
        return result;
    }
}
