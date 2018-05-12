package org.fixme.broker.prompt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.fixme.broker.Broker;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.protocol.messages.BuyOrderMessage;
import org.fixme.core.protocol.messages.CreateWalletMessage;
import org.fixme.core.protocol.messages.ExecutedRequestMessage;
import org.fixme.core.protocol.messages.GetOrdersMessage;
import org.fixme.core.protocol.messages.GetWalletContentMessage;
import org.fixme.core.protocol.messages.MarketDataRequestMessage;
import org.fixme.core.protocol.messages.NewWalletMessage;
import org.fixme.core.protocol.messages.OrdersMessage;
import org.fixme.core.protocol.messages.SellOrderMessage;
import org.fixme.core.protocol.messages.WalletContentMessage;
import org.fixme.core.protocol.types.MarketObject;
import org.fixme.core.utils.Utils;

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
		case "orders":
			getMarketOrders(args);
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
				Map<String, ArrayList<MarketObject>> markets = new HashMap<String, ArrayList<MarketObject>>();
				for (MarketObject o : Broker.markets.values()) {
					if (markets.containsKey(o.fullname.toUpperCase()) == false)
						markets.put(o.fullname.toUpperCase(), new ArrayList<MarketObject>());
					markets.get(o.fullname.toUpperCase()).add(o);
				}
				
				for (Entry<String, ArrayList<MarketObject>> entry : markets.entrySet()) {
					System.out.println(entry.getKey() + " MARKETS");
					System.out.printf("%-15s %12s %12s %12s %12s", "MARKET", "MARKET_ID", "LASTS", "BIDS", "ASKS");
					System.out.println("");
					for (MarketObject market : entry.getValue()) {
						System.out.printf("%-15s %12d %12s %12s %12s", market.name, market.marketID, toStringFloat(market.last), toStringFloat(market.buy), toStringFloat(market.sell));
						System.out.println("");
					}
				}
				waitResponse = false;
			}

			@Override
			public void onRejectedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}
		};
		Broker.router.write(new MarketDataRequestMessage(this.id));
		waitTcpResponse();
	}
	
	public void getMarketOrders(String[] args) {
		boolean valid = true;
		
		if (args.length != 3) {
			valid = false;
		}
		if (valid == true && Utils.isnumeric(args[1]) == false)
			valid = false;
		for (int i = 0; valid == true && i < args.length; i++) {
			if (args[i].trim().isEmpty())
				valid = false;
		}
		if (valid == false) {
			System.out.println("Error [orders MarketId MarketName]");
			return ;
		}
		int marketId = Integer.parseInt(args[1]);
		String marketname = args[2];
		
		this.waitResponse = true;
		Broker.callback = new CallBackRequestMessage() {

			@Override
			public void onExecutedRequest(SocketChannel channel, NetworkMessage message) {
				OrdersMessage m = (OrdersMessage) message;
				String[] asksline = new String[5];
				String[] bidsline = new String[5];
				
				for (int i = 0; i < 5; i++) {
					if (m.bids.length > i)
						bidsline[i] = String.format("%15s %15s", toStringFloat(m.bids[i].quantity), toStringFloat(m.bids[i].price));
					else
						bidsline[i] = "               " + " " + "               ";
				}
				
				for (int i = 0; i < 5; i++) {
					if (m.asks.length > i)
						asksline[i] = String.format("%15s %15s", toStringFloat(m.asks[i].price), toStringFloat(m.asks[i].quantity));
					else
						asksline[i] = "               " + " " + "               ";
				}
				
				String sellmoney = marketname.split("_")[0];
				String buymoney = marketname.split("_")[1];
				
				System.out.printf("%-15s %-15s  %-15s  %-15s %-15s", "SIZE(" + buymoney + ")", "BID(" + sellmoney + ")", marketname, "ASK(" + sellmoney + ")", "SIZE(" + buymoney + ")");
				System.out.println("");
				for (int i = 0; i < 5; i++) {
					System.out.println(bidsline[i] + "  " + "            " + "  " + asksline[i]);
				}
				waitResponse = false;
			}

			@Override
			public void onRejectedRequest(SocketChannel channel, NetworkMessage message) {
				System.out.println(message.toString());
				waitResponse = false;
			}
		};
		Broker.router.write(new GetOrdersMessage(this.id, marketId, marketname));
		waitTcpResponse();
	}
	
	private void buyCommand(String[] args) {
		boolean valid = true;
		
		if (args.length != 7) {
			valid = false;
		}
		//1 ETH_BTC 10 945 WALLET_FROM WALLET_TO
		if (valid == true && Utils.isnumeric(args[1]) == false)
			valid = false;
		if (valid == true && Utils.isnumeric(args[3]) == false)
			valid = false;
		if (valid == true && Utils.isnumeric(args[4]) == false)
			valid = false;
		for (int i = 0; valid == true && i < args.length; i++) {
			if (args[i].trim().isEmpty())
				valid = false;
		}
		if (valid == false) {
			System.out.println("Error [buy MarketID InstrumentFrom_InstrumentTo Quantity Price WalletFrom WalletTo]");
			return ;
		}
		
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
				ExecutedRequestMessage m = (ExecutedRequestMessage)message;
				System.out.println(m.response);
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
		boolean valid = true;
		
		if (args.length != 7) {
			valid = false;
		}
		//1 BTC_ETH 10 945 WALLET_FROM WALLET_TO
		if (valid == true && Utils.isnumeric(args[1]) == false)
			valid = false;
		if (valid == true && Utils.isnumeric(args[3]) == false)
			valid = false;
		if (valid == true && Utils.isnumeric(args[4]) == false)
			valid = false;
		for (int i = 0; valid == true && i < args.length; i++) {
			if (args[i].trim().isEmpty())
				valid = false;
		}
		
		if (valid == false) {
			System.out.println("Error [sell MarketID InstrumentFrom_InstrumentTo Quantity Price WalletFrom WalletTo]");
			return ;
		}
		
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
				ExecutedRequestMessage m = (ExecutedRequestMessage)message;
				System.out.println(m.response);
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
		boolean valid = true;
		
		if (args.length != 3) {
			valid = false;
		}
		//1 BTC_ETH 10 945 WALLET_FROM WALLET_TO
		if (valid == true && Utils.isnumeric(args[1]) == false)
			valid = false;
		for (int i = 0; valid == true && i < args.length; i++) {
			if (args[i].trim().isEmpty())
				valid = false;
		}
		
		if (valid == false) {
			System.out.println("Error [wallet MarketID WalletKey]");
			return ;
		}
		int marketId = Integer.parseInt(args[1]);
		String wallet = args[2];
		this.waitResponse = true;
		Broker.callback = new CallBackRequestMessage() {

			@Override
			public void onExecutedRequest(SocketChannel channel, NetworkMessage message) {
				WalletContentMessage m = (WalletContentMessage)message;
				
				String in_orders = "";
				if (m.in_orders != 0) {
					in_orders = " In orders (" + m.in_orders + ")";
				}
				System.out.println(toStringFloat(m.wallet.quantity) + " " + m.wallet.instrument + in_orders);
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
		boolean valid = true;
		
		if (args.length != 4) {
			valid = false;
		}
		//generate 1 ETH 10
		if (valid == true && Utils.isnumeric(args[1]) == false)
			valid = false;
		if (valid == true && Utils.isnumeric(args[3]) == false)
			valid = false;
		for (int i = 0; valid == true && i < args.length; i++) {
			if (args[i].trim().isEmpty())
				valid = false;
		}
		
		if (valid == false) {
			System.out.println("Error [generate MarketID Instrument Quantity]");
			return ;
		}
		int marketId = Integer.parseInt(args[1]);
		String instrument = args[2];
		int quantity = Integer.parseInt(args[3]);
		
		this.waitResponse = true;
		Broker.callback = new CallBackRequestMessage() {

			@Override
			public void onExecutedRequest(SocketChannel channel, NetworkMessage message) {
				NewWalletMessage m = (NewWalletMessage)message;
				System.out.println("Wallet generated :");
				System.out.println("    " + toStringFloat(m.wallet.quantity) + " " + m.wallet.instrument);
				System.out.println("    key : " + m.wallet.wallet);
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
		System.out.println("  Markets commands ->");
		System.out.println("    markets");
		System.out.println("    tradelist");
		System.out.println("    orders MarketID InstrumentFrom_InstrumentTo");
		System.out.println("  Wallets commands ->");
		System.out.println("    generate MarketID Instrument");
		System.out.println("    wallet MarketID WalletKey");
		System.out.println("  Trades commands ->");
		System.out.println("    buy MarketID InstrumentFrom_InstrumentTo Quantity Price WalletFrom WalletTo");
		System.out.println("    sell MarketID InstrumentFrom_InstrumentTo Quantity Price WalletFrom WalletTo");
	}
	
	private static String waitCommandLine() {
		CommandLineGetter getter = new CommandLineGetter();
		
		waitResponse(getter);
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
    
    public static String toStringFloat(float value)
	{
		return String.format("%.8f", value).replace(',', '.');
	}

}
