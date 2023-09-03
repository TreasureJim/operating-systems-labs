package src;

import java.io.*;
import java.util.*;

public class RunnableProcess implements Runnable {
	private String command;
	private Integer exit_code = null;
	private Process process;

	public String get_command() {
		return command;
	}

	public Integer get_exit_code() {
		if (this.exit_code == null) {
			try {
				exit_code = this.process.exitValue();
			} catch (IllegalThreadStateException e) {
				return null;
			}
		}

		return exit_code;
	}

	public RunnableProcess(String command) {
		this.command = command;
	}

	@Override
	public void run() {
		List<String> input = Arrays.asList(command.split(" "));

		ProcessBuilder processBuilder = new ProcessBuilder(input);
		BufferedReader bufferReader = null;
		try {
			this.process = processBuilder.start();
			InputStream inputStream = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(isr);

			String line;
			while ((line = bufferReader.readLine()) != null) {
				System.out.println(line);
			}

			bufferReader.close();
		} catch (java.io.IOException ioe) {
			System.err.println("Error");
			System.err.println(ioe);
			this.exit_code = -1;
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					System.err.println("Couldn't close reader");
				}
			}
		}
	}
}