package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

	private static class ProcessManager {
		public static ArrayList<RunningProcess> running_commands = new ArrayList<RunningProcess>();
		public static ArrayList<String> error_commands = new ArrayList<String>();

		private static int process_count = 0;

		public static void StartCommand(String command) {
			process_count++;
			RunningProcess process = new RunningProcess(command, process_count);
			process.start();
			running_commands.add(process);
		}

		public static void printErrors() {
			int size = running_commands.size();
			for (int i = 0; i < size; i++) {
				RunningProcess p = running_commands.get(i);
				Integer exit_code = p.getExitCode();
				if (exit_code == null) {
					continue;
				} else if (exit_code == 0) {
					running_commands.remove(i);
					i--;
					size--;
				} else {
					error_commands.add(p.getCommand());
					running_commands.remove(i);
					i--;
					size--;
				}
			}

			for (String error : error_commands) {
				System.out.println(error);
			}
		}

	}

	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n\n***** Welcome to the Java Command Shell *****");
		System.out.println("If you want to exit the shell, type END and press RETURN.\n");

		while (true) {
			System.out.print("jsh>");
			commandLine = scanner.nextLine().toLowerCase();
			String[] commands = commandLine.split(" ");
			// if user entered a return, just loop again
			if (commands[0].equals("")) {
				continue;
			} else if (commands[0].equals("showerrlog")) {
				ProcessManager.printErrors();
				continue;
			} else if (commands[0].equals("end")) { // User wants to end shell
				System.out.println("\n***** Command Shell Terminated. See you next time. BYE for now. *****\n");
				scanner.close();
				System.exit(0);
			} else if (commands[0].equals("filedump")) {
				DumpFile(commands[1]);
				continue;
			} else if (commands[0].equals("copyfile")) {

			}

			ProcessManager.StartCommand(commandLine);
		}
	}

	private static void DumpFile(String filepath) {
		FileInputStream inStream;
		byte[] buffer = new byte[1024];

		try {
			inStream = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			System.err.println("File doesn't exist");
			return;
		}

		try {
			while (inStream.read(buffer) != -1) {
				System.out.write(buffer);
			}

			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void CopyFile(String src, String dest) throws IOException {
		FileInputStream inStream;
		FileOutputStream outStream;
		byte[] buffer = new byte[1024];

		try {
			inStream = new FileInputStream(src);
		} catch (FileNotFoundException e) {
			System.err.println("File doesn't exist");
			return;
		}

		File dFile = new File(dest);
		if (!dFile.exists()) {
			try {
				dFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				inStream.close();
				return;
			}
		}

		try {
			outStream = new FileOutputStream(dFile);
		} catch (FileNotFoundException e) {
			System.err.println("What??");
			e.printStackTrace();
			inStream.close();
			return;
		}

		while (inStream.read(buffer) != -1) {
			// Write out to file
		}

		inStream.close();
		outStream.close();
	}
}
