package org.fixme.broker.prompt;

import java.util.ArrayList;
import java.util.Scanner;

import org.fixme.broker.Broker;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.messages.BuyOrderMessage;
import org.fixme.core.protocol.messages.CreateWalletMessage;
import org.fixme.core.protocol.messages.GetWalletContentMessage;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.protocol.messages.SellOrderMessage;
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
		case "tradelist":
			getMarkets(args);
			break ;
		case "buy":
			buyCommand(args);
			break ;
		case "sell":
			sellCommand(args);
			break ;
		case "wallet":
			getWallet(args);
			break ;
		case "generate":
			createWallet(args);
			break;
		case "exit":
			Broker.stopped = true;
			break ;
		default :
			System.out.println("fixme: command not found: " + args[0]);
			break ;
		}
	}
	
	private void getMarkets(String[] args) {
		this.waitResponse = true;
		Broker.callback = new CallBackRequestMessage() {

			@Override
			public void onExecutedRequest(SocketChannel channel, NetworkMessage message) {
				waitResponse = false;
			}

			@Override
			public void onRejectedRequest(SocketChannel channel, NetworkMessage message) {
				waitResponse = false;
			}
		};
		Broker.router.write(new MarketDataRequestMessage(this.id));
		waitTcpResponse();
		for (MarketObject o : Broker.markets.values()) {
			System.out.println("{"
						+ "\"name\": \"" + o.name + "\", "
						+ "\"marketId\": " + o.marketID + ", "
						+ "\"last\": " + o.last + ", "
						+ "\"buy\": " + o.buy + ", "
						+ "\"sell\": " + o.sell + ""
						+ "}");
		}
	}
	
	private void buyCommand(String[] args) {
		
		if (args.length < 4) {
			return ;
		}
		//1 ETH_BTC 10 945 WALLET_BUYER WALLET_SELLER
		int marketId = Integer.parseInt(args[1]);
		String instrument = args[2];
		float quantity = Float.parseFloat(args[3]);
		float price = Float.parseFloat(args[4]);
		String walleBuyer = args[5];
		String walleSeller = args[6];
		
		this.waitResponse = true;
		Broker.callback = new CallBackRequestMessage() {

			@Override
			public void onExecutedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}

			@Override
			public void onRejectedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}
		};
		Broker.router.write(new BuyOrderMessage(this.id, marketId, instrument, quantity, price, walleBuyer, walleSeller));
		waitTcpResponse();
	}
	
	private void sellCommand(String[] args) {
		
		if (args.length < 4) {
			return ;
		}
		//1 BTC_ETH 10 945 WALLET_SELLER WALLET_BUYER
		
		int marketId = Integer.parseInt(args[1]);
		String instrument = args[2];
		float quantity = Float.parseFloat(args[3]);
		float price = Float.parseFloat(args[4]);
		String walleSeller = args[5];
		String walleBuyer = args[6];
		
		this.waitResponse = true;
		Broker.callback = new CallBackRequestMessage() {

			@Override
			public void onExecutedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}

			@Override
			public void onRejectedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}
		};
		Broker.router.write(new SellOrderMessage(this.id, marketId, instrument, quantity, price, walleSeller, walleBuyer));
		waitTcpResponse();
	}
	
	private void getWallet(String[] args) {
		int marketId = Integer.parseInt(args[1]);
		String wallet = args[2];
		this.waitResponse = true;
		Broker.callback = new CallBackRequestMessage() {

			@Override
			public void onExecutedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}

			@Override
			public void onRejectedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}
			
		};
		Broker.router.write(new GetWalletContentMessage(this.id, marketId, wallet));
		waitTcpResponse();
	}
	
	private void createWallet(String[] args) {
		//generate 1 ETH 10
		int marketId = Integer.parseInt(args[1]);
		String instrument = args[2];
		int quantity = Integer.parseInt(args[3]);
		
		this.waitResponse = true;
		Broker.callback = new CallBackRequestMessage() {

			@Override
			public void onExecutedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}

			@Override
			public void onRejectedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}
			
		};
		Broker.router.write(new CreateWalletMessage(this.id, marketId, instrument, quantity));
		waitTcpResponse();
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