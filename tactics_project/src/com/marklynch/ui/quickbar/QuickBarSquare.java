package com.marklynch.ui.quickbar;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.Level.LevelMode;
import com.marklynch.level.constructs.power.Power;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.actions.ActionUsePower;
import com.marklynch.ui.Draggable;
import com.marklynch.ui.Scrollable;
import com.marklynch.ui.button.ClickListener;
import com.marklynch.ui.button.LevelButton;
import com.marklynch.utils.Color;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.TextureUtils;

public class QuickBarSquare extends LevelButton implements Draggable, Scrollable {

	private Object shortcut;
	public int index;
	public float x1, y1, x2, y2;

	public QuickBarSquare(int index) {
		super(QuickBar.positionX + index * QuickBar.shortcutWidth, QuickBar.positionY, QuickBar.shortcutWidth,
				QuickBar.shortcutWidth, null, null, "", true, true, Color.TRANSPARENT, Color.WHITE, "BUTTON");
		this.index = index;
		resetPosition();
	}

	public void resetPosition() {
		updatePosition(QuickBar.positionX + index * QuickBar.shortcutWidth, QuickBar.positionY);
	}

	public void drawStaticUI() {

		if (shortcut == null) {
			return;
		}

		QuadUtils.drawQuad(Color.BLACK, x1, y1, x2, y2);
		TextureUtils.drawTexture(Square.WHITE_SQUARE, x1, y1, x2, y2);

		if (shortcut instanceof Power) {
			drawPower((Power) shortcut);
		} else if (shortcut instanceof GameObject) {
			drawGameObject((GameObject) shortcut);
		}
	}

	public void setShortcut(Object shortcut) {
		this.shortcut = shortcut;
		if (shortcut == null) {
			this.setClickListener(null);
		} else if (shortcut instanceof Power) {
			this.setClickListener(new ClickListener() {
				@Override
				public void click() {
					Power power = (Power) QuickBarSquare.this.shortcut;
					Level.pausePlayer();
					if (power.selectTarget) {
						Level.levelMode = LevelMode.LEVEL_MODE_CAST;
						Game.level.selectedPower = power.makeCopy(Level.player);
					} else {
						new ActionUsePower(Level.player, Level.player.squareGameObjectIsOn,
								power.makeCopy(Level.player)).perform();
					}
					Game.level.popupMenuObjects.clear();
					Game.level.popupMenuActions.clear();
				}
			});
		} else if (shortcut instanceof GameObject) {
			this.setClickListener(null);
		}
	}

	public void drawPower(Power power) {
		TextureUtils.drawTexture(power.image, x1, y1, x2, y2);
	}

	public void drawGameObject(GameObject gameObject) {
		TextureUtils.drawTexture(gameObject.imageTexture, x1, y1, x2, y2);
	}

	public Object getShortcut() {
		return shortcut;
	}

	@Override
	public void scroll(float dragX, float dragY) {
		// System.out.println("SKILL TREE . SCROLL");
		// drag(dragX, dragY);

		// zooming buttons? fuck...
	}

	@Override
	public void drag(float dragX, float dragY) {

		// this.offsetX -= dragX;
		// this.offsetY -= dragY;

		System.out.println("POWER SQUARE . DRAG");

		// for (SkillTreeNode skillTreeNode : skillTreeNodes) {
		//
		updatePosition(x + dragX, y - dragY);

		// fixScroll();
		// resize2();
	}

	@Override
	public void updatePosition(float x, float y) {

		this.x = x;
		this.y = y;

		realX = x;
		if (this.xFromLeft == false)
			realX = Game.windowWidth - x;

		realY = y;
		if (this.yFromTop == false)
			realY = Game.windowHeight - y;
		x1 = x;
		y1 = y;
		x2 = x + QuickBar.shortcutWidth;
		y2 = y + QuickBar.shortcutWidth;

	}

	@Override
	public void dragDropped() {

		float centerX = x1 + QuickBar.shortcutWidth / 2f;
		float centerY = y1 + QuickBar.shortcutWidth / 2f;

		QuickBarSquare quickBarSquareToSwapWith = null;
		for (QuickBarSquare quickBarSquare : Game.level.quickBar.quickBarSquares) {
			if (quickBarSquare != this && quickBarSquare.calculateIfPointInBoundsOfButton(centerX, centerY))
				quickBarSquareToSwapWith = quickBarSquare;
		}

		if (quickBarSquareToSwapWith == null) {
			if (Game.level.quickBar.isMouseOver((int) centerX, (int) centerY)) {

			} else {
				shortcut = null;
			}
		} else {
			Object tempShortcut = this.shortcut;
			this.shortcut = quickBarSquareToSwapWith.shortcut;
			quickBarSquareToSwapWith.shortcut = tempShortcut;
		}
		resetPosition();
	}

}
