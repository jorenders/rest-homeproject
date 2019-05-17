package be.renders.homeproject.repository;

import be.renders.homeproject.ResponseCode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;

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
}
