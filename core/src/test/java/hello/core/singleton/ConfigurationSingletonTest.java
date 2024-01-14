package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;

public class ConfigurationSingletonTest {
	
	@Test 
	void configurationDeep() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		AppConfig bean=ac.getBean(AppConfig.class);
		
		System.out.println("bean = " + bean.getClass());
	}

}
