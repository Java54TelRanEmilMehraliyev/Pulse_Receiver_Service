package telran.monitoring.pulse;

import java.util.logging.Logger;

public class EnvironmentConfig {
    public static final String LOGGING_LEVEL = System.getenv().getOrDefault("LOGGING_LEVEL", "INFO");
    public static final int MAX_THRESHOLD_PULSE_VALUE = Integer.parseInt(System.getenv().getOrDefault("MAX_THRESHOLD_PULSE_VALUE", "210"));
    public static final int MIN_THRESHOLD_PULSE_VALUE = Integer.parseInt(System.getenv().getOrDefault("MIN_THRESHOLD_PULSE_VALUE", "40"));
    public static final int WARN_MAX_PULSE_VALUE = Integer.parseInt(System.getenv().getOrDefault("WARN_MAX_PULSE_VALUE", "180"));
    public static final int WARN_MIN_PULSE_VALUE = Integer.parseInt(System.getenv().getOrDefault("WARN_MIN_PULSE_VALUE", "55"));

    
    public static void logConfig(Logger logger) {
        logger.config("LOGGING_LEVEL: " + LOGGING_LEVEL);
        logger.config("MAX_THRESHOLD_PULSE_VALUE: " + MAX_THRESHOLD_PULSE_VALUE);
        logger.config("MIN_THRESHOLD_PULSE_VALUE: " + MIN_THRESHOLD_PULSE_VALUE);
        logger.config("WARN_MAX_PULSE_VALUE: " + WARN_MAX_PULSE_VALUE);
        logger.config("WARN_MIN_PULSE_VALUE: " + WARN_MIN_PULSE_VALUE);
    }
}
