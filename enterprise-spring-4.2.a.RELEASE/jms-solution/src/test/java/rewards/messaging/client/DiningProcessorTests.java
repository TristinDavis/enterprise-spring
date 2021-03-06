package rewards.messaging.client;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.messaging.JmsInfrastructureConfig;

/**
 * Tests the Dining batch processor
 */
@SpringApplicationConfiguration(locations = {
		"/rewards/messaging/client/client-config.xml",
		"/system-test-config.xml"},
		classes=JmsInfrastructureConfig.class
)
@RunWith(SpringJUnit4ClassRunner.class)
public class DiningProcessorTests {

	@Autowired
	private DiningProcessor diningProcessor;

	@Autowired
	private RewardConfirmationLogger confirmationLogger;

	@Test
	public void testBatch() throws Exception {
		
		List<Dining> dinings = getTestDinings();

		for(Dining dining : dinings) {
			diningProcessor.process(dining);
		}

		waitForBatch(dinings.size(), 1000);

		assertThat(confirmationLogger.getConfirmations().size(), is(dinings.size()));
	}

	private void waitForBatch(int batchSize, int timeout) throws InterruptedException {
		int sleepTime = 100;
		while (confirmationLogger.getConfirmations().size() < batchSize && timeout > 0) {
			Thread.sleep(sleepTime);
			timeout -= sleepTime;
		}
	}
	
	private List<Dining> getTestDinings() {
		return Arrays.asList(
						Dining.createDining("80.93", "1234123412341234", "1234567890"),
						Dining.createDining("56.12", "1234123412341234", "1234567890"),
						Dining.createDining("32.64", "1234123412341234", "1234567890"),
						Dining.createDining("77.05", "1234123412341234", "1234567890"),
						Dining.createDining("94.50", "1234123412341234", "1234567890"));
	}
}
