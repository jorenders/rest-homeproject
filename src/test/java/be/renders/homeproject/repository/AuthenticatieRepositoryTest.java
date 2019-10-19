package be.renders.homeproject.repository;

import be.renders.homeproject.ResponseCode;
import be.renders.homeproject.repository.domain.Configuratie.Module;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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

    @Test
     public void checkModuleGeeftGekendTerugIndienModuleGeregistreerdIsAlsGekendInDB() {
        Module module = new Module();
        module.setId(1L);
        module.setMacAdres("macadres_gekend");
        Query query = mock(Query.class);
        doReturn(query).when(em).createQuery(anyString());
        doReturn(Arrays.asList(module)).when(query).getResultList();
        doReturn(module).when(query).getSingleResult();

        ResponseCode result = authenticatieRepository.checkModule("macadres_gekend");

        assertEquals(ResponseCode.MODULE_GEKEND, result);
    }

    @Test
    public void checkModuleGeeftNietGekendTerugIndienModuleNietGeregistreerdIsAlsGekendInDB() {
        Module module = new Module();
        module.setId(1L);
        module.setMacAdres("macadres_gekend");
        Query query = mock(Query.class);
        doReturn(query).when(em).createQuery(anyString());
        doReturn(Arrays.asList()).when(query).getResultList();
        doReturn(null).when(query).getSingleResult();

        ResponseCode result = authenticatieRepository.checkModule("macadres_niet_gekend");

        assertEquals(ResponseCode.MODULE_NIET_GEKEND, result);
    }

    @Test
    public void checkModuleGeeftDubbelGeregistreerdTerugIndienModule2XGeregistreerdIsAlsGekendInDB() {
        Module module = new Module();
        module.setId(1L);
        module.setMacAdres("macadres_gekend");
        Query query = mock(Query.class);
        doReturn(query).when(em).createQuery(anyString());
        doReturn(Arrays.asList(module, module)).when(query).getResultList();

        ResponseCode result = authenticatieRepository.checkModule("macadres_gekend");

        assertEquals(ResponseCode.MODULE_DUBBEL_GEREGISTREERD, result);
    }
}
