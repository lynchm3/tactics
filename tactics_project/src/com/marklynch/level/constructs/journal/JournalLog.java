package com.marklynch.level.constructs.journal;

import java.util.concurrent.CopyOnWriteArrayList;

import com.marklynch.Game;
import com.marklynch.level.constructs.area.Area;
import com.marklynch.level.squares.Square;
import com.marklynch.ui.button.Link;
import com.marklynch.utils.TextUtils;

public class JournalLog {

	public Object object;
	private int turn;
	private String turnString;
	private Area area;
	private Square square;
	public float height;
	public CopyOnWriteArrayList<Link> links;

	public JournalLog(Object object) {
		super();
		this.object = object;
		height = TextUtils.getDimensions(Integer.MAX_VALUE, TextUtils.NewLine.NEW_LINE, object)[1];
	}

	public void setTurn(int turn) {
		this.turn = turn;
		this.turnString = Game.level.timeString + " (Turn " + turn + ") ";
	}

	public String getTurnString() {
		return turnString;
	}

	public void setSquare(Square square) {
		this.square = square;
		links = TextUtils.getLinks(true, square, object);
	}

	public Square getSquare() {
		return square;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Area getArea() {
		return area;
	}

}
