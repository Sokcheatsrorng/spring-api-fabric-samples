package co.istad.apihyperledgerfabricspring.services;


import co.istad.apihyperledgerfabricspring.utils.FabricUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {

    private final String assetId = "asset" + Instant.now().toEpochMilli();


    // inject Gateway and Contract
    private   final  Contract contract;

   private final  FabricUtil fabricUtil ;


    // initledger method
    public void initLedger() throws SubmitException, CommitException, EndorseException, CommitStatusException {

        log.info("Initializing ledger");

        contract.submitTransaction("InitLedger");

        log.info("Successfully initialized ledger");
    }


    public String getAllAssets() {
        try {

            byte[] result = contract.evaluateTransaction("GetAllAssets");

            return fabricUtil.prettyJson(result);

        } catch (Exception e) {

            throw new RuntimeException("Failed to get all assets: " + e.getMessage(), e);

        }
    }
  // create asset
    public String createAsset( String color, int size, String owner, int appraisedValue) {
        try {

            byte[] result = contract.submitTransaction("CreateAsset",

                    assetId, color, String.valueOf(size), owner, String.valueOf(appraisedValue));

            return fabricUtil.prettyJson(result);

        } catch (Exception e) {

            throw new RuntimeException("Failed to create asset: " + e.getMessage(), e);

        }

    }

    // get asset by assetId
    public String readAsset(String id) {
        try {

            byte[] result = contract.evaluateTransaction("ReadAsset", id);

            return fabricUtil.prettyJson(result);

        } catch (Exception e) {

            throw new RuntimeException("Failed to read asset: " + e.getMessage(), e);

        }
    }

    // update asset by assetId
    public String updateAsset(String id, String color, int size, String owner, int appraisedValue) {
        try {

            byte[] result = contract.submitTransaction("UpdateAsset",

                    id, color, String.valueOf(size), owner, String.valueOf(appraisedValue));

            return fabricUtil.prettyJson(result);

        } catch (Exception e) {

            throw new RuntimeException("Failed to update asset: " + e.getMessage(), e);

        }
    }

    // deleteAsset
    public String deleteAsset(String id) {
        try {

            byte[] result = contract.submitTransaction("DeleteAsset", id);

            return fabricUtil.prettyJson(result);

        } catch (Exception e) {

            throw new RuntimeException("Failed to delete asset: " + e.getMessage(), e);

        }
    }


}
