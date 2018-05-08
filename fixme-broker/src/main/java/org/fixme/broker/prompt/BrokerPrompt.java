package org.fixme.broker.prompt;

import java.util.Scanner;

import org.fixme.broker.Broker;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.protocol.types.MarketObject;

public class BrokerPrompt {
	
	public boolean waitResponse = false;
	public int id = -1;
	
	public void setUid(int id) {
		this.id = id;
	}
	
	public void start() {
		while (Broker.stopped == false) {
			System.out.print("<fix-me>(" + id + ") » ");
			execute(waitCommandLine());
		}
	}
	
	public void execute(String commandLine) {
		
		if (commandLine.isEmpty())
			return ;
		String[] args = commandLine.split(" ");
		
		switch (args[0]) {
		case "help":
				display_help();
			break ;
		case "markets":
			for (MarketObject o : Broker.markets) {
				System.out.println(o.name + " market" + o.marketID);
			}
//			waitResponse = true;
//			Broker.router.write(new MarketDataRequestMessage(Broker.router.getUid()));
//			waitTcpResponse();
			break ;
		case "create":
			createCommand(args);
			break ;
		case "exit":
			Broker.stopped = true;
			break ;
		default :
			System.out.println("fixme: command not found: " + args[0]);
			break ;
		}
	}
	
	private void createCommand(String[] args) {
		
		if (args.length >= 2 && args[1].equalsIgnoreCase("wallet")) {
			System.out.println("create wallet of " + args[2]);
		}
	}
	
	private void waitTcpResponse() {
		while (waitResponse) {
			try { Thread.sleep(50); } catch(Exception e) {}
		}
	}
	
	private void display_help() {
		System.out.println("Fixme Help :");
		System.out.println("Fixme Help :");
	}
	
	private static String waitCommandLine() {
		CommandLineGetter getter = new CommandLineGetter();
		
		//while (!menugetter.isValide()) {
			//System.out.println("COUCOU:");
			waitResponse(getter);
		//}
		return getter.rep;
    }
	
	public static void waitResponse(CommandListener rep) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		
		rep.onResponse(s.nextLine());
	}
    
    public static class CommandLineGetter implements CommandListener {

		public int id = -1;
		public String rep;
		
		@Override
		public void onResponse(String response) {
			rep = response;
		}
		
		public boolean isValide() {
			try {
				id = Integer.parseInt(rep);
			} catch (NumberFormatException e) {
				return false;
			}
			if (id > 2 || id < 1)
				return false;
			return true;
		}
	}

}
