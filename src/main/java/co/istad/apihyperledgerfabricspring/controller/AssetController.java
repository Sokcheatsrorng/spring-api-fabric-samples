package co.istad.apihyperledgerfabricspring.controller;

import co.istad.apihyperledgerfabricspring.services.AssetService;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/asset/api/v1")
public class AssetController {

    // inject bean from service
    private final AssetService assetService;

    // initLedger
    @PostMapping("/init")
    public ResponseEntity<String> initLedger() throws GatewayException, CommitException {
        assetService.initLedger();
        return new ResponseEntity<String>(HttpStatusCode.valueOf(201));

    }

    // get all asset
    @GetMapping("/all")
    public ResponseEntity<String> getAllAsset() throws GatewayException {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

//     get asset By assetId
    @GetMapping("/{assetId}")
    public ResponseEntity<String> getAsset(@PathVariable String assetId) {
        return ResponseEntity.ok(assetService.readAsset(assetId));
    }

    //create asset
    @PostMapping
    public ResponseEntity<String> createAsset(
            @RequestParam String color,
            @RequestParam int size,
            @RequestParam String owner,
            @RequestParam int appraisedValue) {
        return ResponseEntity.ok(assetService.createAsset( color, size, owner, appraisedValue));
    }


    // update asset by assetId
    @PutMapping("/{assetId}")
    public ResponseEntity<String> updateAsset(
            @PathVariable String assetId,
            @RequestParam String color,
            @RequestParam int size,
            @RequestParam String owner,
            @RequestParam int appraisedValue) {
        return ResponseEntity.ok(assetService.updateAsset(assetId, color, size, owner, appraisedValue));
    }

     // delete asset by assetId
    @DeleteMapping("/{assetId}")
    public ResponseEntity<String> deleteAsset(@PathVariable String assetId) {
        return ResponseEntity.ok(assetService.deleteAsset(assetId));
    }
}
