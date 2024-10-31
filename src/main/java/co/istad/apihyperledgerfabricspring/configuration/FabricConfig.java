package co.istad.apihyperledgerfabricspring.configuration;

import co.istad.apihyperledgerfabricspring.utils.FabricUtil;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Configuration
public class FabricConfig {
    @Value("${fabric.chaincode-name}")
    private String chaincodeName;

    @Value("${fabric.channel-name}")
    private String channelName;

    @Value("${fabric.msp-id}")
    private String mspId;

    @Value("${fabric.peer-endpoint}")
    private String peerEndpoint;

    @Value("${fabric.override-auth}")
    private String overrideAuth;

    @Bean
    public FabricUtil fabricUtil() {
        if (!StringUtils.hasText(mspId) || !StringUtils.hasText(peerEndpoint) || !StringUtils.hasText(overrideAuth)) {
            throw new IllegalStateException("Required Fabric configuration properties are missing. Please check your application.properties/yaml file.");
        }
        return new FabricUtil(mspId, peerEndpoint, overrideAuth);
    }

    @Bean
    public Gateway gateway(FabricUtil fabricUtil)
            {

        if (!StringUtils.hasText(chaincodeName) || !StringUtils.hasText(channelName)) {
            throw new IllegalStateException("Chaincode name or channel name is missing in configuration.");
        }
                /*
                when we use directory from static it can cause the reason null string so we can define var string
                to store that value like below:
                var channel = fabricUtil.newGrpcConnection();
                var identity = fabricUtil.newIdentity();
                var signer = fabricUtil.newSigner();

                 */
        try {
            var channel = fabricUtil.newGrpcConnection();
            var identity = fabricUtil.newIdentity();
            var signer = fabricUtil.newSigner();

            return Gateway.newInstance()
                    .identity(identity)
                    .signer(signer)
                    .hash(Hash.SHA256)
                    .connection(channel)
                    .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                    .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                    .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                    .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES))
                    .connect();
        } catch (Exception e) {

            throw new RuntimeException("Failed to create Gateway instance: " + e.getMessage(), e);
        }
    }

    // it can be lazy so can use annotation @Lazy
    @Bean
    public Contract contract(Gateway gateway) {
        try {

            return gateway.getNetwork(channelName).getContract(chaincodeName);

        } catch (Exception e) {

            throw new RuntimeException("Failed to get contract: " + e.getMessage(), e);

        }
    }
}
