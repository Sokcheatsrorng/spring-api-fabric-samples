package co.istad.apihyperledgerfabricspring.utils;

import co.istad.apihyperledgerfabricspring.configuration.FabricConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.identity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;


@RequiredArgsConstructor
public  class FabricUtil {

    @Value("${fabric.msp_id}")
    private static  String MSP_ID;

    @Value("${fabric.peer-endpoint}")
    private static String PEER_ENDPOINT;

    @Value("${fabric.override-auth}")
    private static String OVERRIDE_AUTH;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FabricUtil(String mspId, String peerEndpoint, String overrideAuth) {
        MSP_ID = mspId;
        PEER_ENDPOINT = peerEndpoint;
        OVERRIDE_AUTH = overrideAuth;
    }

    @Bean
    public FabricUtil fabricUtil() {
        return new FabricUtil(MSP_ID, PEER_ENDPOINT, OVERRIDE_AUTH);
    }


    // Path to crypto materials.
    private static final Path CRYPTO_PATH = Paths
            .get("./fabric-samples/test-network/organizations/peerOrganizations/org1.example.com");
    // Path to user certificate.
    private static final Path CERT_DIR_PATH = CRYPTO_PATH
            .resolve(Paths.get("users/User1@org1.example.com/msp/signcerts"));
    // Path to user private key directory.
    private static final Path KEY_DIR_PATH = CRYPTO_PATH
            .resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
    // Path to peer tls certificate.
    private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));


    public static Identity newIdentity() throws IOException, CertificateException {
        try (var certReader = Files.newBufferedReader(getFirstFilePath(CERT_DIR_PATH))) {
            var certificate = Identities.readX509Certificate(certReader);
            return new X509Identity(MSP_ID, certificate);
        }
    }


    public static Signer newSigner()  {
        try (var keyReader = Files.newBufferedReader(getFirstFilePath(KEY_DIR_PATH))) {
            var privateKey = Identities.readPrivateKey(keyReader);
            return Signers.newPrivateKeySigner(privateKey);
        } catch (IOException | InvalidKeyException e) {
            throw new RuntimeException("Failed to create identity", e);
        }


    }

    public static Path getFirstFilePath(Path dirPath) throws IOException {
        try (var keyFiles = Files.list(dirPath)) {
            return keyFiles.findFirst().orElseThrow();
        }
    }

    public static ManagedChannel newGrpcConnection() throws Exception {
        var credentials = TlsChannelCredentials.newBuilder()
                .trustManager(TLS_CERT_PATH.toFile())
                .build();
        return Grpc.newChannelBuilder(PEER_ENDPOINT, credentials)
                .overrideAuthority(OVERRIDE_AUTH)
                .build();
    }

    // print Json Format
    public String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    public String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

}
