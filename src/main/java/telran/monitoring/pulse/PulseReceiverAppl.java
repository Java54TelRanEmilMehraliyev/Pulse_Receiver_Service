package telran.monitoring.pulse;

import java.net.*;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import telran.monitoring.pulse.dto.SensorData;

public class PulseReceiverAppl {
	private static final int PORT = 5005;
	private static final int MAX_BUFFER_SIZE = 1500;
	static DatagramSocket socket;
	private static final Logger logger = Logger.getLogger(PulseReceiverAppl.class.getName());

	public static void main(String[] args) throws Exception {
		EnvironmentConfig.logConfig(logger);
		

		socket = new DatagramSocket(PORT);
		byte[] buffer = new byte[MAX_BUFFER_SIZE];

		logger.info("PulseReceiver started on port: " + PORT);
		logger.config("Environment variables: LOGGING_LEVEL: " + EnvironmentConfig.LOGGING_LEVEL
				+ ", MAX_THRESHOLD_PULSE_VALUE: " + EnvironmentConfig.MAX_THRESHOLD_PULSE_VALUE
				+ ", MIN_THRESHOLD_PULSE_VALUE: " + EnvironmentConfig.MIN_THRESHOLD_PULSE_VALUE
				+ ", WARN_MAX_PULSE_VALUE: " + EnvironmentConfig.WARN_MAX_PULSE_VALUE + ", WARN_MIN_PULSE_VALUE: "
				+ EnvironmentConfig.WARN_MIN_PULSE_VALUE);
		while (true) {
			DatagramPacket packet = new DatagramPacket(buffer, MAX_BUFFER_SIZE);
			socket.receive(packet);
			processReceivedData(buffer, packet);
		}
	}

	private static void processReceivedData(byte[] buffer, DatagramPacket packet) {
		String json = new String(Arrays.copyOf(buffer, packet.getLength()));
		logger.fine("Received data: " + json);
		SensorData data = parseSensorData(json);

		int pulse = data.value();
		checkPulseValue(pulse, data.patientId());
		String formattedTimestamp = SensorData.formatTimestamp(data.timestamp());
		logger.log(Level.FINER, "Putting patiendId: {0}, timestamp: {1} into DynamoDB table",
				new Object[] { data.patientId(), data.timestamp() });
		logger.finer("Data successfully processed for patientId: " + data.patientId() + " at " + formattedTimestamp);
	}

	private static void checkPulseValue(int pulse, long patientId) {

		if (pulse > EnvironmentConfig.MAX_THRESHOLD_PULSE_VALUE) {
			logger.severe("Critical: PatientId " + patientId + " - Pulse value " + pulse
					+ " exceeds the maximum threshold of " + EnvironmentConfig.MAX_THRESHOLD_PULSE_VALUE);
		} else if (pulse < EnvironmentConfig.MIN_THRESHOLD_PULSE_VALUE) {
			logger.severe("Critical: PatientId " + patientId + " - Pulse value " + pulse
					+ " is below the minimum threshold of " + EnvironmentConfig.MIN_THRESHOLD_PULSE_VALUE);
		} else if (pulse > EnvironmentConfig.WARN_MAX_PULSE_VALUE) {
			logger.warning("Warning: PatientId " + patientId + " - Pulse value " + pulse
					+ " exceeds the warning level of " + EnvironmentConfig.WARN_MAX_PULSE_VALUE);
		} else if (pulse < EnvironmentConfig.WARN_MIN_PULSE_VALUE) {
			logger.warning("Warning: PatientId " + patientId + " - Pulse value " + pulse
					+ " is below the warning level of " + EnvironmentConfig.WARN_MIN_PULSE_VALUE);
		} else {
			logger.info("PatientId " + patientId + " - Pulse value " + pulse + " is within normal range.");
		}
	}

	private static SensorData parseSensorData(String json) {

		return SensorData.getSensorData(json);
	}
}
