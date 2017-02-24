package com.marklynch.level.constructs.cave;

public class CaveSection {

	public String name;
	public int gridX1, gridY1, gridX2, gridY2;

	public CaveSection(String name, int gridX1, int gridY1, int gridX2, int gridY2) {
		super();
		this.name = name;
		this.gridX1 = gridX1;
		this.gridY1 = gridY1;
		this.gridX2 = gridX2;
		this.gridY2 = gridY2;
	}

}
