package ma.ens.controller;

import io.micrometer.core.instrument.MeterRegistry;
import ma.ens.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private DemoService demoService;

    @Autowired
    private MeterRegistry meterRegistry;

    @GetMapping("/demo")
    public String demo() {
        log.info("Requête reçue sur /demo");
        meterRegistry.counter("http.requests.total", "endpoint", "demo").increment();
        String result = demoService.processData();
        log.debug("Réponse envoyée avec succès");
        return result;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "monde") String name) {
        log.info("Hello appelé avec paramètre : {}", name);
        meterRegistry.counter("http.requests.total", "endpoint", "hello").increment();
        return "Hello " + name + "!";
    }

    @GetMapping("/process")
    public String process() {
        log.info("Requête sur /process");
        meterRegistry.counter("http.requests.total", "endpoint", "process").increment();
        meterRegistry.gauge("custom.active.requests", 1);
        return demoService.processData();
    }

    @GetMapping("/error-simulator")
    public String errorSimulator() {
        log.info("Requête sur /error-simulator");
        meterRegistry.counter("http.requests.total", "endpoint", "error", "status", "error").increment();

        try {
            return demoService.processWithError();
        } catch (Exception e) {
            meterRegistry.counter("custom.errors.count", "type", "business").increment();
            return "Erreur: " + e.getMessage();
        }
    }
}