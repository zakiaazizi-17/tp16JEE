package ma.ens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Tp8MonitoringApplication {

	private static final Logger log = LoggerFactory.getLogger(Tp8MonitoringApplication.class);

	public static void main(String[] args) {
		log.info("Démarrage de l'application Spring Monitoring");
		SpringApplication.run(Tp8MonitoringApplication.class, args);
		log.info("Application démarrée avec succès !");
	}
}