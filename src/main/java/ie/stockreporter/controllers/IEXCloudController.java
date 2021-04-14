package ie.stockreporter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ie.stockreporter.services.IEXCloudService;

@RestController
public class IEXCloudController {

    @Autowired
    private IEXCloudService iexCloudService;

    @GetMapping("/time-series")
    public ResponseEntity<Object> getAllTimeSeries() {
        return this.iexCloudService.getAllTimeSeries();
    }

    @GetMapping("/crypto-symbols")
    public ResponseEntity<Object> getAllCryptoSymbols() { return this.iexCloudService.getAllCryptoSymbols(); }
}
