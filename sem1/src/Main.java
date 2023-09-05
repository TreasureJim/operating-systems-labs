package src;

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
			commandLine = scanner.nextLine();
			// if user entered a return, just loop again
			if (commandLine.equals("")) {
				continue;
			} else if (commandLine.equals("showerrlog")) {
				ProcessManager.printErrors();
				continue;
			} else if (commandLine.toLowerCase().equals("end")) { // User wants to end shell
				System.out.println("\n***** Command Shell Terminated. See you next time. BYE for now. *****\n");
				scanner.close();
				System.exit(0);
			}

			ProcessManager.StartCommand(commandLine);
		}
	}

}
