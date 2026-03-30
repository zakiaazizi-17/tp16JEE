package ma.ens.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    private static final Logger log = LoggerFactory.getLogger(DemoService.class);

    private final Counter requestCounter;
    private final Timer processingTimer;

    @Autowired
    public DemoService(MeterRegistry meterRegistry) {
        this.requestCounter = Counter.builder("custom.requests.count")
                .description("Nombre total de requêtes traitées")
                .register(meterRegistry);

        this.processingTimer = Timer.builder("custom.processing.time")
                .description("Temps de traitement des requêtes")
                .register(meterRegistry);
    }

    public String processData() {
        requestCounter.increment();
        log.info("Requête enregistrée dans Prometheus - Compteur: {}", requestCounter.count());

        return processingTimer.record(() -> {
            try {
                log.debug("Démarrage du traitement des données");
                long delay = (long) (Math.random() * 200);
                Thread.sleep(delay);
                log.debug("Traitement terminé en {} ms", delay);
            } catch (InterruptedException e) {
                log.error("Erreur lors du traitement", e);
            }
            return "Traitement terminé";
        });
    }

    public String processWithError() {
        requestCounter.increment();

        return processingTimer.record(() -> {
            try {
                Thread.sleep(50);
                if (Math.random() > 0.7) {
                    throw new RuntimeException("Erreur aléatoire simulée");
                }
                return "Succès";
            } catch (Exception e) {
                log.error("Erreur lors du traitement", e);
                throw new RuntimeException(e);
            }
        });
    }
}