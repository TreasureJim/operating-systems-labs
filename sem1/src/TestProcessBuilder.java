package src;

import java.util.*;

public class TestProcessBuilder {

	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n\n***** Welcome to the Java Command Shell *****");
		System.out.println("If you want to exit the shell, type END and press RETURN.\n");

		Vector<String> invalid_commands = new Vector<String>();

		while (true) {
			System.out.print("jsh>");
			commandLine = scanner.nextLine();
			// if user entered a return, just loop again
			switch (commandLine.toLowerCase()) {
				case "":
					continue;

				case "end":
					System.out.println(
							"\n***** Command Shell Terminated. See you next time. BYE for now. *****\n");
					scanner.close();
					System.exit(0);

				case "showerrlog":
					System.out.println("Errors:");
					for (String s : invalid_commands) {
						System.out.println("\t" + s);
					}
					continue;

				default:
					break;
			}

			RunnableProcess process = new RunnableProcess(commandLine);
			// if doesn't finish without error add to list of invalid commands
			int exit_code = process.start();
			if (exit_code != 0) {
				invalid_commands.add(commandLine);
				System.out.println("Bad command");
			}
			System.out.println("Exit code: " + exit_code);
		}
	}

}
