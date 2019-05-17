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

/**
 * Created by joren on 17/05/2019.
 */

public class AuthenticatieRepositoryTest {

    @Spy
    JdbcTemplate jdbcTemplate;

    @Mock
    private EntityManager em;

    @InjectMocks
    private AuthenticatieRepository authenticatieRepository;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registreerConfiguratieSchrijftMetingWegNaarDB() {
        ResponseCode result = authenticatieRepository.registreerNieuweModule("macadress module");
        assertEquals(result, ResponseCode.OK);
    }
}
