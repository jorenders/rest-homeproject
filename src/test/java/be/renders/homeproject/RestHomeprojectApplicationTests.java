package be.renders.homeproject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestHomeprojectApplicationTests {

	private RestHomeprojectApplication restHomeprojectApplication;


	@Test
	public void contextLoads() {
		restHomeprojectApplication = new RestHomeprojectApplication();
		ViewResolverRegistry viewResolverRegistry = new ViewResolverRegistry();
		restHomeprojectApplication.configureViewResolvers(viewResolverRegistry);
	}

}
