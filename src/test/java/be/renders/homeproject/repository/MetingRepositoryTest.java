package be.renders.homeproject.repository;

import be.renders.homeproject.ResponseCode;
import be.renders.homeproject.TestHelperClass;
import be.renders.homeproject.repository.domain.Meting;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by joren on 17/05/2019.
 */

public class MetingRepositoryTest {

    @Spy
    JdbcTemplate jdbcTemplate;

    @Mock
    private EntityManager em;

    @InjectMocks
    private MetingRepository metingRepository;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registreerMetingSchrijftMetingWegNaarDB() {
        ResponseCode result = metingRepository.registreerMeting(1, 12.00);
        assertEquals(result, ResponseCode.OK);
    }

    @Test
    public void getMetingHaaltAlleMetingenOpVanDeAfgelopen60seconden() {
        Timestamp now = new Timestamp(Instant.now().toEpochMilli());
        long nowLong = now.getTime();
        Meting meting1 = TestHelperClass.createMeting(0L, now, 123456L, 12.15);
        Meting meting2 = TestHelperClass.createMeting(1L, now, 123416L, 18.15);

        List<Meting> metingen = new ArrayList<Meting>();
        metingen.addAll(Arrays.asList(meting1, meting2));
        Query query = mock(Query.class);
        doReturn(query).when(em).createQuery(anyString());
        doReturn(metingen).when(query).getResultList();

        List<Meting> result = metingRepository.getMeting(Instant.now().toEpochMilli());
        assertEquals(result.size(), metingen.size());
    }
}
