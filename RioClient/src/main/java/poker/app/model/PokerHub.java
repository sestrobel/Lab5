package poker.app.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import exceptions.DeckException;
import netgame.common.Hub;
import pokerBase.Action;
import pokerBase.Card;
import pokerBase.CardDraw;
import pokerBase.Deck;
import pokerBase.GamePlay;
import pokerBase.GamePlayPlayerHand;
import pokerBase.Player;
import pokerBase.Rule;
import pokerBase.Table;
import pokerEnums.eAction;
import pokerEnums.eCardDestination;
import pokerEnums.eDrawCount;
import pokerEnums.eGame;
import pokerEnums.eGameState;

public class PokerHub extends Hub {

	private Table HubPokerTable = new Table();
	private GamePlay HubGamePlay;
	private int iDealNbr = 0;
	private eGameState eGameState;

	public PokerHub(int port) throws IOException {
		super(port);
	}

	protected void playerConnected(int playerID) {

		if (playerID == 2) {
			shutdownServerSocket();
		}
	}

	protected void playerDisconnected(int playerID) {
		shutDownHub();
	}

	protected void messageReceived(int ClientID, Object message) {

		if (message instanceof Action) {

			Action act = (Action) message;
			switch (act.getAction()) {
			case StartGame:
				// code needed to start game
				// should create an instance of GamePlay, set all the parameters
				Rule rle = new Rule((eGame) act.geteGame());
				UUID GameDealerID = UUID.randomUUID();
				GamePlay HubGamePlay = new GamePlay(rle, GameDealerID);
				sendToAll(HubPokerTable);
				break;
			case Sit:
				resetOutput();
				// sits player at table and sends back updated table
				HubPokerTable.AddPlayerToTable(act.getPlayer());
				sendToAll(HubPokerTable);
				break;
			case Leave:
				resetOutput();
				// has player leave table and sends back updated table
				HubPokerTable.RemovePlayerFromTable(act.getPlayer());
				sendToAll(HubPokerTable);
				break;
			case GameState:
				// sends back updated table
				sendToAll(HubPokerTable);
				break;
			default:
				break;
			}

		}

		System.out.println("Message Received by Hub");

		sendToAll("Sending Message Back to Client");
	}

}
