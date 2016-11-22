package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringApplicationConfiguration(locations={"/rewards/messaging/spring-integration-idempotent-receiver-config.xml",
        "/rewards/messaging/spring-integration-infrastructure-config.xml",
        "/rewards/messaging/spring-integration-marshalling-config.xml",
        "/rewards/messaging/spring-integration-xml-splitting-config.xml",
        "classpath:system-test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RewardMessagingIntegrationTests {

	@Autowired MessagingTemplate template;

	// xTODO 05: Remove @Ignore below.
	//			Examine the test logic.  
	//			It ensures a dining XML file sent in results in an XML confirmation out.
	//			Run this test, it should pass.
	@Test
	//@Ignore
	public void sendSingleXmlDiningTwice() throws Exception {
		File xmlFile = new ClassPathResource("dining-sample.xml", getClass()).getFile();
		template.convertAndSend("xmlDinings", xmlFile);
		
		String payload = template.receiveAndConvert("xmlConfirmations", String.class);		
		assertTrue(payload.contains("<reward-confirmation"));
		
		template.convertAndSend("xmlDinings", xmlFile);
		assertEquals(payload, template.receiveAndConvert("xmlConfirmations", String.class));
	}
	
	// xTODO 10: Remove @Ignore below
	//			Examine the test logic, this test case involves a single 
	//			XML document with multiple dining sub-elements.
	//			Run this test, it should pass.
	@Test
	//@Ignore
	public void sendMultipleDinings() throws Exception {
		File xmlFile = new ClassPathResource("dinings-sample.xml", getClass()).getFile();
		template.convertAndSend("mixedXmlDinings", xmlFile);
		
		assertNotNull(template.receiveAndConvert("xmlConfirmations", Object.class));
		assertNotNull(template.receiveAndConvert("xmlConfirmations", Object.class));
	}


	@Test
	public void configOk() throws Exception {
		// testing the fixture
	}
}
