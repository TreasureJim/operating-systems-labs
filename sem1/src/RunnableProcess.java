package src;

import java.io.*;
import java.util.*;

public class RunnableProcess implements Runnable {
	private String command;

	private int exit_code;

	public RunnableProcess(String command) {
		this.command = command;
	}

	public int start() {
		this.run();
		return this.exit_code;
	}

	@Override
	public void run() {
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

			this.exit_code = proc.waitFor();

			bufferReader.close();
		} catch (java.io.IOException ioe) {
			System.err.println("Error");
			System.err.println(ioe);
			this.exit_code = -1;
		} catch (InterruptedException e) {
			System.err.println("Error: process interrupted");
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