package com.marklynch.level.constructs.skilltree;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;

import com.marklynch.Game;
import com.marklynch.actions.ActionUsePower;
import com.marklynch.level.Level;
import com.marklynch.level.Level.LevelMode;
import com.marklynch.level.constructs.Stat;
import com.marklynch.level.constructs.characterscreen.CharacterScreen;
import com.marklynch.level.constructs.power.Power;
import com.marklynch.level.constructs.requirementtomeet.RequirementToMeet;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.ui.Draggable;
import com.marklynch.ui.Scrollable;
import com.marklynch.ui.button.ClickListener;
import com.marklynch.ui.button.LevelButton;
import com.marklynch.ui.button.Tooltip;
import com.marklynch.ui.quickbar.QuickBarSquare;
import com.marklynch.utils.Color;
import com.marklynch.utils.LineUtils;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.ResourceUtils;
import com.marklynch.utils.TextUtils;
import com.marklynch.utils.Texture;
import com.marklynch.utils.TextureUtils;

public class SkillTreeNode extends LevelButton {

	public static Texture textureCircle;

	private boolean activated = false;
	public String name;
	public String description;
	public CopyOnWriteArrayList<RequirementToMeet> requirementsToMeet = new CopyOnWriteArrayList<RequirementToMeet>();
	public CopyOnWriteArrayList<SkillTreeNode> linkedSkillTreeNodes = new CopyOnWriteArrayList<SkillTreeNode>();
	public CopyOnWriteArrayList<SkillTreeNodePower> powerButtons = new CopyOnWriteArrayList<SkillTreeNodePower>();
	public CopyOnWriteArrayList<SkillTreeNodeStat> statButtons = new CopyOnWriteArrayList<SkillTreeNodeStat>();
	public CopyOnWriteArrayList<Stat> statsUnlocked = new CopyOnWriteArrayList<Stat>();
	public float xInPixels, yInPixels, circleX1, circleY1, circleX2, circleY2, textX, textY;

	float powerOffsetX = 16;
	float powerOffsetY = 16;
	float statOffsetX = 16;
	float statOffsetY = -16 - SkillTreeNodeStat.statWidth;

	public CopyOnWriteArrayList<Power> powersUnlocked = new CopyOnWriteArrayList<Power>();;
	public static float circleRadius = 48;
	public static float circleCircumference = circleRadius * 2;
	// public float dragX = 0, dragY = 0;

	public SkillTreeNode(int xInGrid, int yInGrid) {
		super(xInGrid * 256 - circleRadius, yInGrid * 256 - circleRadius, circleCircumference, circleCircumference,
				null, null, "", true, true, Color.TRANSPARENT, Color.WHITE, "BUTTON");
		this.xInPixels = xInGrid * 256;
		this.yInPixels = yInGrid * 256;

	}

	private String getRequirementString() {
		String s = "Requires knowledge of ";
		for (int i = 0; i < linkedSkillTreeNodes.size(); i++) {
			s += linkedSkillTreeNodes.get(i).name;
			if (i < (linkedSkillTreeNodes.size() - 1))
				s += " or ";
		}
		return s;
	}

	public void updateTooltip() {
		tooltips.clear();

		CopyOnWriteArrayList<Object> tooltipItems = new CopyOnWriteArrayList<Object>();

		tooltipItems.add(this.name);
		tooltipItems.add(TextUtils.NewLine.NEW_LINE);
		tooltipItems.add(description);
		if (activated) {
			tooltipItems.add(TextUtils.NewLine.NEW_LINE);
			tooltipItems.add("You know this power");
		} else {
			if (!isAvailable()) {
				tooltipItems.add(TextUtils.NewLine.NEW_LINE);
				tooltipItems.add(getRequirementString());
			} else {
				tooltipItems.add(TextUtils.NewLine.NEW_LINE);
				tooltipItems.add("Click to learn");
			}
		}

		tooltips.add(new Tooltip(Color.WHITE, tooltipItems));

		for (final SkillTreeNodePower powerButton : powerButtons) {
			powerButton.updateTooltip();
		}

	}

	public void init() {

		for (final Power power : powersUnlocked) {
			SkillTreeNodePower skillTreeNodePowerButton = new SkillTreeNodePower(power, 0, 0);
			this.powerButtons.add(skillTreeNodePowerButton);

			skillTreeNodePowerButton.setClickListener(new ClickListener() {

				@Override
				public void click() {
					if (activated && power.passive == false) {
						Level.pausePlayer();
						if (power.selectTarget) {
							Level.levelMode = LevelMode.LEVEL_MODE_CAST;
							Game.level.selectedPower = power.makeCopy(Level.player);
						} else {
							new ActionUsePower(Level.player, Game.gameObjectMouseIsOver,
									Level.player.squareGameObjectIsOn, power.makeCopy(Level.player), true).perform();
						}
						Game.level.popupMenuObjects.clear();
						Game.level.popupMenuActions.clear();
						Game.level.openCloseSkillTree();
					} else if (activated && power.passive == true) {
						power.toggledOn = !power.toggledOn;
					}

				}
			});

			skillTreeNodePowerButton.setDoubleClickListener(new ClickListener() {

				@Override
				public void click() {
					if (activated) {
						addToQuickBar(power);
					}

				}
			});
		}

		for (Stat stat : statsUnlocked) {
			this.statButtons.add(new SkillTreeNodeStat(stat, 0, 0));
		}

		setLocation();

		this.setClickListener(new ClickListener() {

			@Override
			public void click() {

				if (activated || !isAvailable()) {
					return;
				} else {

					Level.showDialog("Unlock " + name + "?", "UNLOCK", "CANCEL", new ClickListener() {
						@Override
						public void click() {
							activate(Game.level.player);
							Level.closeDialog();
						}
					}, new ClickListener() {
						@Override
						public void click() {
							Level.closeDialog();
						}
					});
				}

			}

		});

		updateTooltip();
	}

	public void activate(Actor actor) {
		activated = true;
		for (SkillTreeNodePower skillTreeNodePower : powerButtons) {
			actor.powers.add(skillTreeNodePower.power);
			if (actor == Level.player) {
				addToQuickBar(skillTreeNodePower.power);
			}
		}

		for (Stat statUnlocked : statsUnlocked) {
			Level.player.highLevelStats.get(statUnlocked.type).value += statUnlocked.value;
		}

		updateTooltip();

		for (SkillTreeNode skillTreeNode : linkedSkillTreeNodes) {
			skillTreeNode.updateTooltip();
		}

	}

	public void addToQuickBar(Power power) {

		for (QuickBarSquare quickBarSquare : Level.quickBar.quickBarSquares) {

			if (power.getClass().isInstance(quickBarSquare.getShortcut()))
				return;

			if (quickBarSquare.getShortcut() == null) {
				quickBarSquare.setShortcut(power);
				break;
			}
		}
	}

	public void drawLines() {
		for (SkillTreeNode linkedSkillTreeNode : linkedSkillTreeNodes) {
			if (this.activated && linkedSkillTreeNode.activated)
				LineUtils.drawLine(Color.BLUE, this.xInPixels, this.yInPixels, linkedSkillTreeNode.xInPixels,
						linkedSkillTreeNode.yInPixels, 4);
			else if (this.activated || linkedSkillTreeNode.activated)
				LineUtils.drawLine(Color.LIGHT_GRAY, this.xInPixels, this.yInPixels, linkedSkillTreeNode.xInPixels,
						linkedSkillTreeNode.yInPixels, 4);
			else
				LineUtils.drawLine(Color.DARK_GRAY, this.xInPixels, this.yInPixels, linkedSkillTreeNode.xInPixels,
						linkedSkillTreeNode.yInPixels, 4);
		}

	}

	public void drawCircle() {
		if (activated) {
			TextureUtils.drawTexture(textureCircle, circleX1, circleY1, circleX2, circleY2, Color.BLUE);
		} else {
			if (isAvailable()) {
				TextureUtils.drawTexture(textureCircle, circleX1, circleY1, circleX2, circleY2, Color.LIGHT_GRAY);
			} else {
				TextureUtils.drawTexture(textureCircle, circleX1, circleY1, circleX2, circleY2, Color.DARK_GRAY);
			}
		}
		TextUtils.printTextWithImages(textX, textY, Integer.MAX_VALUE, false, null, Color.WHITE, 1f, name);

		for (SkillTreeNodePower skillTreeNodePower : powerButtons) {
			skillTreeNodePower.drawBackground();
			skillTreeNodePower.drawPower();
		}

		for (SkillTreeNodeStat skillTreeNodeStat : statButtons) {
			skillTreeNodeStat.drawBackground();
			skillTreeNodeStat.drawStat();
		}
	}

	public static void loadStaticImages() {
		textureCircle = ResourceUtils.getGlobalImage("skill_tree_circle.png", false);

	}

	@Override
	public boolean calculateIfPointInBoundsOfButton(float mouseX, float mouseY) {
		if (super.calculateIfPointInBoundsOfButton(mouseX, mouseY)) {
			return Math.hypot(this.xInPixels - mouseX, this.yInPixels - mouseY) <= circleRadius;
		}
		return false;
	}

	@Override
	public void updatePosition(float x, float y) {

		super.updatePosition(x - circleRadius, y - circleRadius);

		this.xInPixels = x;
		this.yInPixels = y;

		for (SkillTreeNodePower skillTreeNodePower : powerButtons) {
			skillTreeNodePower.updatePosition(x + powerOffsetX, y + powerOffsetY);
		}

		for (SkillTreeNodeStat skillTreeNodeStat : statButtons) {
			skillTreeNodeStat.updatePosition(x + statOffsetX, y + statOffsetY);
		}

		setLocation();

	}

	private void setLocation() {

		circleX1 = xInPixels - circleRadius;
		circleY1 = yInPixels - circleRadius;
		circleX2 = xInPixels + circleRadius;
		circleY2 = yInPixels + circleRadius;
		textX = xInPixels - Game.smallFont.getWidth(name) / 2;
		textY = yInPixels - 10;

		for (SkillTreeNodePower skillTreeNodePower : powerButtons) {
			skillTreeNodePower.setLocation(xInPixels + powerOffsetX, yInPixels + powerOffsetY);
		}

		for (SkillTreeNodeStat skillTreeNodeStat : statButtons) {
			skillTreeNodeStat.setLocation(xInPixels + statOffsetX, yInPixels + statOffsetY);
		}

		// powerX =

	}

	public boolean isAvailable() {

		if (activated)
			return false;

		for (SkillTreeNode linkedSkillTreeNode : linkedSkillTreeNodes) {
			if (linkedSkillTreeNode.activated) {
				return true;
			}
		}
		return false;
	}

	public class SkillTreeNodeStat extends LevelButton {

		Stat stat;
		public float dragX = 0, dragY = 0;
		public static final float statWidth = 64;
		public float powerHalfWidth = statWidth / 2;
		public float x1, y1, x2, y2;

		public SkillTreeNodeStat(Stat stat, int x, int y) {
			super(x, y, statWidth, statWidth, null, null, "", true, true, Color.TRANSPARENT, Color.WHITE, "");
			this.stat = stat;
			this.x = x;
			this.y = y;
			this.setTooltipText(stat);

		}

		private void setLocation(float x, float y) {
			this.x = x;
			this.y = y;
			this.x1 = x;
			this.y1 = y;
			this.x2 = x + statWidth;
			this.y2 = y + statWidth;

		}

		public void drawBackground() {

			if (activated) {
				TextureUtils.drawTexture(textureCircle, x1, y1, x2, y2, Color.BLUE);
			} else {
				if (isAvailable()) {
					TextureUtils.drawTexture(textureCircle, x1, y1, x2, y2, Color.LIGHT_GRAY);
				} else {
					TextureUtils.drawTexture(textureCircle, x1, y1, x2, y2, Color.DARK_GRAY);
				}
			}
		}

		public void drawStat() {
			TextureUtils.drawTexture(CharacterScreen.highLevelStatImages.get(this.stat.type), x1, y1, x2, y2);
		}

		@Override
		public boolean calculateIfPointInBoundsOfButton(float mouseX, float mouseY) {
			if (super.calculateIfPointInBoundsOfButton(mouseX, mouseY)) {
				return Math.hypot(this.x - mouseX, this.y - mouseY) <= circleRadius;
			}
			return false;
		}
	}

	public class SkillTreeNodePower extends LevelButton implements Draggable, Scrollable {

		Power power;
		public float dragX = 0, dragY = 0;
		public static final float powerWidth = 64;
		public float powerHalfWidth = powerWidth / 2;
		public float circleRadius = powerHalfWidth;
		public float x1, y1, x2, y2;

		public SkillTreeNodePower(Power power, int x, int y) {
			super(x, y, powerWidth, powerWidth, null, null, "", true, true, Color.TRANSPARENT, Color.WHITE, "");
			this.power = power;
			this.x = x;
			this.y = y;
			updateTooltip();

		}

		public void updateTooltip() {

			tooltips.clear();

			CopyOnWriteArrayList<Object> tooltipItems = new CopyOnWriteArrayList<Object>();

			tooltipItems.add(power);
			if (activated && !power.passive) {
				tooltipItems.add(TextUtils.NewLine.NEW_LINE);
				tooltipItems.add("Click to cast");
			} else if (activated && power.passive) {
				tooltipItems.add(TextUtils.NewLine.NEW_LINE);
				tooltipItems.add("Click to toggle on/off");
			} else {
			}

			tooltips.add(new Tooltip(Color.WHITE, tooltipItems));
			// setTooltipText(power);
		}

		private void setLocation(float x, float y) {
			this.x = x;
			this.y = y;
			this.x1 = x;
			this.y1 = y;
			this.x2 = x + powerWidth;
			this.y2 = y + powerWidth;

		}

		@Override
		public boolean calculateIfPointInBoundsOfButton(float mouseX, float mouseY) {
			if (super.calculateIfPointInBoundsOfButton(mouseX, mouseY)) {
				if (power.passive) {
					return Math.hypot((this.x + circleRadius) - mouseX,
							(this.y + +circleRadius) - mouseY) <= circleRadius;
				} else {
					return true;
				}
			}
			return false;
		}

		public void drawBackground() {

			if (power.passive) {

				if (activated) {
					TextureUtils.drawTexture(textureCircle, x1, y1, x2, y2, Color.BLUE);
				} else {
					if (isAvailable()) {
						TextureUtils.drawTexture(textureCircle, x1, y1, x2, y2, Color.LIGHT_GRAY);
					} else {
						TextureUtils.drawTexture(textureCircle, x1, y1, x2, y2, Color.DARK_GRAY);
					}
				}

			} else {
				QuadUtils.drawQuad(Color.BLACK, x1, y1, x2, y2);
				if (activated) {
					TextureUtils.drawTexture(Square.WHITE_SQUARE, x1, y1, x2, y2, Color.BLUE);
				} else {
					if (isAvailable()) {
						TextureUtils.drawTexture(Square.WHITE_SQUARE, x1, y1, x2, y2, Color.LIGHT_GRAY);
					} else {
						TextureUtils.drawTexture(Square.WHITE_SQUARE, x1, y1, x2, y2, Color.DARK_GRAY);
					}
				}
			}
		}

		public void drawPower() {
			TextureUtils.drawTexture(this.power.image, x1, y1, x2, y2);
		}

		public void drawDragged() {

			if (!SkillTreeNode.this.activated)
				return;

			TextureUtils.drawTexture(this.power.image, Mouse.getX() - powerHalfWidth,
					Game.windowHeight - Mouse.getY() - powerHalfWidth, Mouse.getX() + powerHalfWidth,
					Game.windowHeight - Mouse.getY() + powerHalfWidth);
		}

		@Override
		public void scroll(float dragX, float dragY) {

		}

		@Override
		public void drag(float drawOffsetX, float dragOffsetY) {

			if (!SkillTreeNode.this.activated)
				return;

			this.dragX = this.dragX + drawOffsetX;
			this.dragY = this.dragY - dragOffsetY;

			float centerX = this.x + this.dragX;
			float centerY = this.y + this.dragY;

			for (QuickBarSquare quickBarSquare : Game.level.quickBar.quickBarSquares) {
				if (quickBarSquare.calculateIfPointInBoundsOfButton(centerX, centerY)) {
				} else {
				}
			}
		}

		@Override
		public void dragDropped() {

			if (!SkillTreeNode.this.activated)
				return;

			float centerX = Mouse.getX();
			float centerY = Game.windowHeight - Mouse.getY();

			QuickBarSquare quickBarSquareToSwapWith = null;
			for (QuickBarSquare quickBarSquare : Game.level.quickBar.quickBarSquares) {
				if (quickBarSquare.calculateIfPointInBoundsOfButton(centerX, centerY)) {
					quickBarSquareToSwapWith = quickBarSquare;
				}
				quickBarSquare.tempSwap = null;
			}

			if (quickBarSquareToSwapWith == null) {

			} else if (SkillTreeNode.this.activated) {
				quickBarSquareToSwapWith.setShortcut(this.power);
			}

			this.dragX = 0;
			this.dragY = 0;
		}
	}

}
