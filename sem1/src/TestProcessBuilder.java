package src;

import java.util.*;

public class TestProcessBuilder {

	private static class ProcessManager implements Runnable {

		Vector<String> invalid_commands = new Vector<String>();
		Vector<RunnableProcess> running_commands = new Vector<RunnableProcess>();
		Vector<RunnableProcess> finished_commands = new Vector<RunnableProcess>();

		public void print_invalid_commands() {
			this.query_running_commands();

			System.out.println("Errors:");
			for (String s : this.invalid_commands) {
				System.out.println("\t" + s);
			}
		}

		private void query_running_commands() {
			this.running_commands.removeIf(p -> {
				if (p.get_exit_code() == null)
					return false;
				else if (p.get_exit_code() == 0)
					return true;
				else {
					this.invalid_commands.add(p.get_command());
					return true;
				}
			});
		}

		@Override
		public void run() {
			/**
			 * loop through running commands
			 * if finished remove from list and optionally add to invalid commands
			 * else print output
			 */
		}
	}

	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n\n***** Welcome to the Java Command Shell *****");
		System.out.println("If you want to exit the shell, type END and press RETURN.\n");

		ProcessManager p_manager = new ProcessManager();

		while (true) {
			System.out.print("jsh>");
			commandLine = scanner.nextLine();

			switch (commandLine.toLowerCase()) {
				case "":
					continue;

				case "end":
					System.out.println(
							"\n***** Command Shell Terminated. See you next time. BYE for now. *****\n");
					scanner.close();
					System.exit(0);

				case "showerrlog":
					p_manager.print_invalid_commands();
					continue;

				default:
					break;
			}

			RunnableProcess process = new RunnableProcess(commandLine);
			p_manager.running_commands.add(process);
			process.run();
		}
	}

}
