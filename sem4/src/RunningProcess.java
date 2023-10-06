package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

class RunningProcess extends Thread {

	public RunningProcess(String command, int id) {
		this.command = command;
		this.processId = id;
	}

	private String command;
	private int processId;
	private Integer exitCode;

	public int getProcessId() {
		return processId;
	}

	public String getCommand() {
		return command;
	}

	public Integer getExitCode() {
		return exitCode;
	}

	@Override
	public void run() {
		System.out.println("Starting process: #" + this.processId);

		List<String> input = Arrays.asList(command.split(" "));

		ProcessBuilder processBuilder = new ProcessBuilder(input);
		BufferedReader bufferReader = null;

		try {

			Process proc = processBuilder.start();
			InputStream inputStream = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(isr);

			String line;
			while ((line = bufferReader.readLine()) != null) {
				System.out.println(line);
			}
			bufferReader.close();

			System.out.println("Process #" + this.processId + " finished");
		} catch (java.io.IOException ioe) {
			System.err.println("Error");
			System.err.println(ioe);
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
				}
			}
		}
	}
}