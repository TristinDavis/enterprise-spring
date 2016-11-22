package rewards;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;
import rewards.internal.reward.RewardRepository;

@Configuration
@Import({SpringIntegrationIdempotentReceiverConfig.class, SpringIntegrationInfraConfig.class})
public class TestContextConfig {

	@Bean
	public RewardNetwork rewardNetwork() {
		return Mockito.mock(RewardNetwork.class);
	}

	@Bean
	public RewardRepository rewardRepository() {
		return Mockito.mock(RewardRepository.class);
	}

	@Bean
	public MessageChannel errorTestChannel() {
		return MessageChannels.queue().get();
	}

	@Bean
	public IntegrationFlow bridgeFlow() {  //Bridge between errorChannel and errorTestChannel
		return IntegrationFlows
				.from("errorChannel")
				.channel("errorTestChannel")
				.get();
	}

}
