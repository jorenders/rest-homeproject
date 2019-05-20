package be.renders.homeproject.repository;

import be.renders.homeproject.ResponseCode;
import be.renders.homeproject.TestHelperClass;
import be.renders.homeproject.repository.domain.Configuratie;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by joren on 17/05/2019.
 */

public class ConfiguratieRepositoryTest {

    @Spy
    JdbcTemplate jdbcTemplate;

    @Mock
    private EntityManager em;

    @InjectMocks
    private ConfiguratieRepository configuratieRepository;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registreerConfiguratieSchrijftMetingWegNaarDB() {
        ResponseCode result = configuratieRepository.registreerConfiguratie("temp living", "temp liv");
        assertEquals(result, ResponseCode.OK);
    }

    @Test
    public void haalOverzichtConfiguratieOpGeeftAlleConfiguratiesTerug() {
        List<Configuratie> configuraties = new ArrayList<Configuratie>();
        Configuratie configuratie1 = TestHelperClass.createConfiguratie(1L, "value1", "naam1");
        Configuratie configuratie2 = TestHelperClass.createConfiguratie(2L, "value2", "naam2");

        configuraties.addAll(Arrays.asList(configuratie1, configuratie2));

        Query query = mock(Query.class);
        doReturn(query).when(em).createQuery(anyString());
        doReturn(configuraties).when(query).getResultList();

        List<Configuratie> result = configuratieRepository.haalOverzichtConfiguratieOp();
        assertEquals(configuraties, result);
    }
}
