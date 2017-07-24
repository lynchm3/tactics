package com.marklynch.level.quest.smallgame;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.ai.utils.AIRoutineUtils;
import com.marklynch.level.constructs.Area;
import com.marklynch.level.constructs.Group;
import com.marklynch.level.conversation.Conversation;
import com.marklynch.level.conversation.ConversationPart;
import com.marklynch.level.conversation.ConversationResponse;
import com.marklynch.level.quest.Quest;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.Templates;
import com.marklynch.objects.actions.ActionGive;
import com.marklynch.objects.actions.ActionTalk;
import com.marklynch.objects.units.Actor;

public class QuestSmallGame extends Quest {

	// Needs to be a large forest w/ animals near te lodge for the hunters after
	// the quest, if they survive.... it just makes sense. Another village could
	// be a fishing village and another one could be a farming village or
	// another could be a big trading hub and they trade to get food, and a
	// foraging village, and mix and match some..... make it clear in
	// conversations that this is a farming/fishing/hunting/trading village.
	// There could be two farming villages, one of them does only veg and
	// foraging, and theyre all hippy vegans. Another could be deep in the
	// woods, and the houses are made out of giant trees and they forage and
	// hunt.

	// The hunting party leader says out loud to the pack, in a way that u can
	// see it "so, use the water imbued weapons on the the beast. No fire, it
	// feeds off the stuff."

	// Then at the start of the conversation it's him talking to the gorup going
	// "...and that's how we're going to pull off the greatest hunt of all
	// time." Then turning to u.... begin explaining quest.

	// Hunting plan could have a crude drawying of the wolf. I think all signs
	// should have pngs to display instead of text.
	// "Hello stranger. I'm (creating) the most formidable hunting party this
	// town has ever seen, would you be interested in joining us? -yes -no
	// .Living in the craggle cave just south of here is a ferocious beast. She
	// may look nothing more than an obersizef wolf, but dpnt let her fool you.
	// Flames spit forth from his eyes and she can kill a man with nary a howl"
	// -why would u want to kill this beast? -from what u say it soounds like an
	// impossible task -i love a challenge, im in." I love the ridiculous
	// description of the beast, make it more ridiculous, theres definitely
	// dialogue like this in the witcher, look it up. AND THEN make the
	// ridiculous description true :D. Also new condition where u tell them u
	// wont be part of it, and they say last chance, u say ur sure, then the
	// mage runs over to u to tell u to save the beast. Everytging the hunter
	// says should be over the top, pirate weaving tales in a bar stupid. Look
	// up some similar adventure time and pirate show dialogs like this.
	// And the head hunter lost and arm to the beast! :p "-no beast like that
	// exists, you're ridiculous". "Tis the same beast that claimed my arm! Well
	// come with us and see with your own eyes". In the cave theres his arm!!!
	// :'D "rotted arm" With a ring beside it. U could give him the ring if u
	// want and he goes "nono, that's very kind of you, but keep it laddie, it
	// just brings back painful memories for me".
	// On the sign for the hunt plan have even more ridiculous facts about the
	// beast that are still actually facts.

	// "There should be some spare equipment round the back, help yourself"

	// Need to change ownership of the stuff around the back from master hunter
	// to you when you accept the quest. 2 guys around the back to stop u
	// stealing shit beforehand.

	// They're all standing around in a circle with awesome equipment in the
	// middle

	// For stealing show a dialogue if u'll get caught doing it.

	// O M F G the enviromentalist wants to enslave super wolf. O M F G
	// O M F G. And he like tried to get the wolves power before but he was
	// rejected.
	// And now that the wolf is weakened after the fight he puts him in an evil
	// restriction
	// And u can choose to fight the environmentalist or not. If u fight him he
	// has a skill "entrap" or "restriction" or something, and he has the skill
	// book for that power on his body. And he has a note on his body,
	// instructions on how to entrap a wolf spirit god thing. SO u can try
	// entrap the wolf or let it go. Entrap it to get ++fire resistance and if u
	// let the wolf go he'll give u something deadly from the cave.
	// And if u kill the wolf he attacks u.. and hes powerful and it'll take a
	// bunch of ur new hunter pals to save ur ass. Or something.

	// SO powerwise - hunters == wolfpack > envirnmentalist.

	// special case if u kill enviromentalist early on :P

	// Could lead in to larger overarching story with spirit animals

	// U can rob the hunters blind when they leave on the hunt, u can go back
	// and steal all their shit :P I like this.

	// quest flag on the weapons round back should be enough to stop AIs
	// stealing shit + guards nearby + put ownership = hunters on it until ur
	// told to go get them

	// Make the enviromentalist seem dopey until the end. What do u call an
	// enviromentalist magic user? Sage? Forest Sage? Woodland sage? Sage is
	// good, makes him sound benevolent and omniscient.

	// Some sort of reaction from the wolves if u see them before accepting the
	// hunt.Walk over and have a look at u? Nothing agressive. Maybe
	// unintentionally intimidating?

	// Quest text
	final String OBJECTIVE_FOLLOW_THE_HUNTERS_TO_SUPERWOLF = "Follow the hunters to Superwolf";

	// Activity Strings
	final String ACTIVITY_PLANNING_A_HUNT = "Planning a hunt";
	final String ACTIVITY_DESCRIPTION_HUNTING = "Goin' hunting";
	final String ACTIVITY_SPYING = "Spying";
	final String ACTIVITY_SAVING_THE_WORLD = "Saving the world";
	final String ACTIVITY_WAITING_FOR_YOU = "Waiting for you";
	final String ACTIVITY_DESCRIPTION_GOING_HOME = "Going home";

	// Flags
	boolean questAcceptedFromHunters;
	boolean talkedToEnvironmentalist;
	boolean readyToGo;
	boolean playerAttackedHunters;
	boolean playerAttackedWolves;
	boolean huntersDead;
	boolean wolvesDead;

	// End
	boolean huntersReleasedFromQuest;

	boolean huntersAndWolvesFought = false;

	// Actors
	Group hunterPack;
	Actor environmentalist;
	Group wolfPack;
	Actor superWolf;
	Actor cub;

	// GameObjects
	ArrayList<GameObject> weaponsBehindLodge;

	// Squares
	Square squareBehindLodge;
	Square huntPlanningArea;

	// Conversations
	public static Conversation conversationHuntersJoinTheHunt;
	public static Conversation conversationEnviromentalistImNotSpying;
	public static Conversation conversationEnviromentalistSaveTheWolf;
	public static Conversation conversationHuntersReadyToGo;
	public static Conversation conversationHuntersOnlyHuntersGetLoot;

	public QuestSmallGame(Group hunterPack, Actor enviromenmentalist, Actor superWolf, Group wolfPack, Actor cub,
			ArrayList<GameObject> weaponsBehindLodge) {
		super();
		this.hunterPack = hunterPack;
		this.hunterPack.quest = this;
		for (GameObject hunter : hunterPack.getMembers()) {
			hunter.quest = this;
		}

		this.environmentalist = enviromenmentalist;
		enviromenmentalist.quest = this;

		this.wolfPack = wolfPack;
		this.wolfPack.quest = this;
		for (GameObject wolf : wolfPack.getMembers()) {
			wolf.quest = this;
			wolf.inventory.add(Templates.CLEAVER.makeCopy(null, null));
		}

		this.superWolf = superWolf;
		this.superWolf.quest = this;

		if (cub != null) {
			this.cub = cub;
			cub.quest = this;
		}

		this.weaponsBehindLodge = weaponsBehindLodge;
		for (GameObject weaponBehindLodge : weaponsBehindLodge) {
			weaponBehindLodge.quest = this;
		}

		squareBehindLodge = Game.level.squares[12][9];
		huntPlanningArea = Game.level.squares[5][8];

		createForest();

		setUpConversationJoinTheHunt();
		setUpConversationImNotSpying();
		setUpConversationSaveTheWolf();
		setUpConversationReadyToGo();
		setUpConversationYouDidntHelp();

	}

	public void createForest() {

		// How I mafe the forest shape -

		// Rough calculations;
		// int centerX = 160;
		// int centerY = 50;
		//
		// int maxDiffFromCenterX = 50;
		// int maxDiffFromCenterY = 50;
		//
		// for (int i = 0; i < 10000; i++) {
		//
		// int randomX = (int) (103 + (113 * Math.random()));
		// int randomY = (int) (3 + (95 * Math.random()));
		//
		// if (Math.random() * maxDiffFromCenterX < Math.abs(randomX - centerX))
		// continue;
		// if (Math.random() * maxDiffFromCenterY < Math.abs(randomY - centerY))
		// continue;
		//
		// System.out.println("Templates.TREE.makeCopy(Game.level.squares[" +
		// randomX + "][" + randomY + "], null);");
		// Templates.TREE.makeCopy(Game.level.squares[randomX][randomY], null);
		//
		// }

		Area area = new Area("Town Forest", 121, 11, 199, 90);
		Templates.TREE.makeCopy(Game.level.squares[112][24], null);
		Templates.TREE.makeCopy(Game.level.squares[112][32], null);
		Templates.TREE.makeCopy(Game.level.squares[112][70], null);
		Templates.TREE.makeCopy(Game.level.squares[113][42], null);
		Templates.TREE.makeCopy(Game.level.squares[114][36], null);
		Templates.TREE.makeCopy(Game.level.squares[114][44], null);
		Templates.TREE.makeCopy(Game.level.squares[114][48], null);
		Templates.TREE.makeCopy(Game.level.squares[114][68], null);
		Templates.TREE.makeCopy(Game.level.squares[114][79], null);
		Templates.TREE.makeCopy(Game.level.squares[115][78], null);
		Templates.TREE.makeCopy(Game.level.squares[115][84], null);
		Templates.TREE.makeCopy(Game.level.squares[116][41], null);
		Templates.TREE.makeCopy(Game.level.squares[116][46], null);
		Templates.TREE.makeCopy(Game.level.squares[116][52], null);
		Templates.TREE.makeCopy(Game.level.squares[116][56], null);
		Templates.TREE.makeCopy(Game.level.squares[116][6], null);
		Templates.TREE.makeCopy(Game.level.squares[116][72], null);
		Templates.TREE.makeCopy(Game.level.squares[116][76], null);
		Templates.TREE.makeCopy(Game.level.squares[116][80], null);
		Templates.TREE.makeCopy(Game.level.squares[116][87], null);
		Templates.TREE.makeCopy(Game.level.squares[117][37], null);
		Templates.TREE.makeCopy(Game.level.squares[117][38], null);
		Templates.TREE.makeCopy(Game.level.squares[117][66], null);
		Templates.TREE.makeCopy(Game.level.squares[117][79], null);
		Templates.TREE.makeCopy(Game.level.squares[117][85], null);
		Templates.TREE.makeCopy(Game.level.squares[118][40], null);
		Templates.TREE.makeCopy(Game.level.squares[118][43], null);
		Templates.TREE.makeCopy(Game.level.squares[118][58], null);
		Templates.TREE.makeCopy(Game.level.squares[118][60], null);
		Templates.TREE.makeCopy(Game.level.squares[119][24], null);
		Templates.TREE.makeCopy(Game.level.squares[119][30], null);
		Templates.TREE.makeCopy(Game.level.squares[119][42], null);
		Templates.TREE.makeCopy(Game.level.squares[119][67], null);
		Templates.TREE.makeCopy(Game.level.squares[119][68], null);
		Templates.TREE.makeCopy(Game.level.squares[119][73], null);
		Templates.TREE.makeCopy(Game.level.squares[119][85], null);
		Templates.TREE.makeCopy(Game.level.squares[120][10], null);
		Templates.TREE.makeCopy(Game.level.squares[120][51], null);
		Templates.TREE.makeCopy(Game.level.squares[120][95], null);
		Templates.TREE.makeCopy(Game.level.squares[121][23], null);
		Templates.TREE.makeCopy(Game.level.squares[121][32], null);
		Templates.TREE.makeCopy(Game.level.squares[121][36], null);
		Templates.TREE.makeCopy(Game.level.squares[121][40], null);
		Templates.TREE.makeCopy(Game.level.squares[121][42], null);
		Templates.TREE.makeCopy(Game.level.squares[121][43], null);
		Templates.TREE.makeCopy(Game.level.squares[121][46], null);
		Templates.TREE.makeCopy(Game.level.squares[121][50], null);
		Templates.TREE.makeCopy(Game.level.squares[121][62], null);
		Templates.TREE.makeCopy(Game.level.squares[121][65], null);
		Templates.TREE.makeCopy(Game.level.squares[121][68], null);
		Templates.TREE.makeCopy(Game.level.squares[121][96], null);
		Templates.TREE.makeCopy(Game.level.squares[122][22], null);
		Templates.TREE.makeCopy(Game.level.squares[122][35], null);
		Templates.TREE.makeCopy(Game.level.squares[122][40], null);
		Templates.TREE.makeCopy(Game.level.squares[122][45], null);
		Templates.TREE.makeCopy(Game.level.squares[122][59], null);
		Templates.TREE.makeCopy(Game.level.squares[122][76], null);
		Templates.TREE.makeCopy(Game.level.squares[122][84], null);
		Templates.TREE.makeCopy(Game.level.squares[122][85], null);
		Templates.TREE.makeCopy(Game.level.squares[123][35], null);
		Templates.TREE.makeCopy(Game.level.squares[123][44], null);
		Templates.TREE.makeCopy(Game.level.squares[123][49], null);
		Templates.TREE.makeCopy(Game.level.squares[123][55], null);
		Templates.TREE.makeCopy(Game.level.squares[123][60], null);
		Templates.TREE.makeCopy(Game.level.squares[124][33], null);
		Templates.TREE.makeCopy(Game.level.squares[124][54], null);
		Templates.TREE.makeCopy(Game.level.squares[124][74], null);
		Templates.TREE.makeCopy(Game.level.squares[124][83], null);
		Templates.TREE.makeCopy(Game.level.squares[124][88], null);
		Templates.TREE.makeCopy(Game.level.squares[125][13], null);
		Templates.TREE.makeCopy(Game.level.squares[125][14], null);
		Templates.TREE.makeCopy(Game.level.squares[125][31], null);
		Templates.TREE.makeCopy(Game.level.squares[125][32], null);
		Templates.TREE.makeCopy(Game.level.squares[125][33], null);
		Templates.TREE.makeCopy(Game.level.squares[125][34], null);
		Templates.TREE.makeCopy(Game.level.squares[125][39], null);
		Templates.TREE.makeCopy(Game.level.squares[125][41], null);
		Templates.TREE.makeCopy(Game.level.squares[125][57], null);
		Templates.TREE.makeCopy(Game.level.squares[125][66], null);
		Templates.TREE.makeCopy(Game.level.squares[125][69], null);
		Templates.TREE.makeCopy(Game.level.squares[125][70], null);
		Templates.TREE.makeCopy(Game.level.squares[126][30], null);
		Templates.TREE.makeCopy(Game.level.squares[126][34], null);
		Templates.TREE.makeCopy(Game.level.squares[126][41], null);
		Templates.TREE.makeCopy(Game.level.squares[126][44], null);
		Templates.TREE.makeCopy(Game.level.squares[126][50], null);
		Templates.TREE.makeCopy(Game.level.squares[126][51], null);
		Templates.TREE.makeCopy(Game.level.squares[126][55], null);
		Templates.TREE.makeCopy(Game.level.squares[126][57], null);
		Templates.TREE.makeCopy(Game.level.squares[126][59], null);
		Templates.TREE.makeCopy(Game.level.squares[126][61], null);
		Templates.TREE.makeCopy(Game.level.squares[126][64], null);
		Templates.TREE.makeCopy(Game.level.squares[126][78], null);
		Templates.TREE.makeCopy(Game.level.squares[127][18], null);
		Templates.TREE.makeCopy(Game.level.squares[127][30], null);
		Templates.TREE.makeCopy(Game.level.squares[127][39], null);
		Templates.TREE.makeCopy(Game.level.squares[127][44], null);
		Templates.TREE.makeCopy(Game.level.squares[127][45], null);
		Templates.TREE.makeCopy(Game.level.squares[127][55], null);
		Templates.TREE.makeCopy(Game.level.squares[127][58], null);
		Templates.TREE.makeCopy(Game.level.squares[127][62], null);
		Templates.TREE.makeCopy(Game.level.squares[127][69], null);
		Templates.TREE.makeCopy(Game.level.squares[127][73], null);
		Templates.TREE.makeCopy(Game.level.squares[127][80], null);
		Templates.TREE.makeCopy(Game.level.squares[127][85], null);
		Templates.TREE.makeCopy(Game.level.squares[128][30], null);
		Templates.TREE.makeCopy(Game.level.squares[128][32], null);
		Templates.TREE.makeCopy(Game.level.squares[128][38], null);
		Templates.TREE.makeCopy(Game.level.squares[128][44], null);
		Templates.TREE.makeCopy(Game.level.squares[128][46], null);
		Templates.TREE.makeCopy(Game.level.squares[128][47], null);
		Templates.TREE.makeCopy(Game.level.squares[128][48], null);
		Templates.TREE.makeCopy(Game.level.squares[128][49], null);
		Templates.TREE.makeCopy(Game.level.squares[128][60], null);
		Templates.TREE.makeCopy(Game.level.squares[128][78], null);
		Templates.TREE.makeCopy(Game.level.squares[128][79], null);
		Templates.TREE.makeCopy(Game.level.squares[129][23], null);
		Templates.TREE.makeCopy(Game.level.squares[129][31], null);
		Templates.TREE.makeCopy(Game.level.squares[129][39], null);
		Templates.TREE.makeCopy(Game.level.squares[129][41], null);
		Templates.TREE.makeCopy(Game.level.squares[129][45], null);
		Templates.TREE.makeCopy(Game.level.squares[129][47], null);
		Templates.TREE.makeCopy(Game.level.squares[129][51], null);
		Templates.TREE.makeCopy(Game.level.squares[129][53], null);
		Templates.TREE.makeCopy(Game.level.squares[129][58], null);
		Templates.TREE.makeCopy(Game.level.squares[129][62], null);
		Templates.TREE.makeCopy(Game.level.squares[129][72], null);
		Templates.TREE.makeCopy(Game.level.squares[130][42], null);
		Templates.TREE.makeCopy(Game.level.squares[130][44], null);
		Templates.TREE.makeCopy(Game.level.squares[130][47], null);
		Templates.TREE.makeCopy(Game.level.squares[130][56], null);
		Templates.TREE.makeCopy(Game.level.squares[130][58], null);
		Templates.TREE.makeCopy(Game.level.squares[130][66], null);
		Templates.TREE.makeCopy(Game.level.squares[130][67], null);
		Templates.TREE.makeCopy(Game.level.squares[130][68], null);
		Templates.TREE.makeCopy(Game.level.squares[130][80], null);
		Templates.TREE.makeCopy(Game.level.squares[130][82], null);
		Templates.TREE.makeCopy(Game.level.squares[130][87], null);
		Templates.TREE.makeCopy(Game.level.squares[131][20], null);
		Templates.TREE.makeCopy(Game.level.squares[131][22], null);
		Templates.TREE.makeCopy(Game.level.squares[131][27], null);
		Templates.TREE.makeCopy(Game.level.squares[131][33], null);
		Templates.TREE.makeCopy(Game.level.squares[131][34], null);
		Templates.TREE.makeCopy(Game.level.squares[131][37], null);
		Templates.TREE.makeCopy(Game.level.squares[131][41], null);
		Templates.TREE.makeCopy(Game.level.squares[131][45], null);
		Templates.TREE.makeCopy(Game.level.squares[131][46], null);
		Templates.TREE.makeCopy(Game.level.squares[131][59], null);
		Templates.TREE.makeCopy(Game.level.squares[131][61], null);
		Templates.TREE.makeCopy(Game.level.squares[131][65], null);
		Templates.TREE.makeCopy(Game.level.squares[131][66], null);
		Templates.TREE.makeCopy(Game.level.squares[131][69], null);
		Templates.TREE.makeCopy(Game.level.squares[131][80], null);
		Templates.TREE.makeCopy(Game.level.squares[131][86], null);
		Templates.TREE.makeCopy(Game.level.squares[132][15], null);
		Templates.TREE.makeCopy(Game.level.squares[132][17], null);
		Templates.TREE.makeCopy(Game.level.squares[132][28], null);
		Templates.TREE.makeCopy(Game.level.squares[132][39], null);
		Templates.TREE.makeCopy(Game.level.squares[132][57], null);
		Templates.TREE.makeCopy(Game.level.squares[132][61], null);
		Templates.TREE.makeCopy(Game.level.squares[132][66], null);
		Templates.TREE.makeCopy(Game.level.squares[132][80], null);
		Templates.TREE.makeCopy(Game.level.squares[133][18], null);
		Templates.TREE.makeCopy(Game.level.squares[133][29], null);
		Templates.TREE.makeCopy(Game.level.squares[133][32], null);
		Templates.TREE.makeCopy(Game.level.squares[133][38], null);
		Templates.TREE.makeCopy(Game.level.squares[133][41], null);
		Templates.TREE.makeCopy(Game.level.squares[133][51], null);
		Templates.TREE.makeCopy(Game.level.squares[133][52], null);
		Templates.TREE.makeCopy(Game.level.squares[133][53], null);
		Templates.TREE.makeCopy(Game.level.squares[133][54], null);
		Templates.TREE.makeCopy(Game.level.squares[133][60], null);
		Templates.TREE.makeCopy(Game.level.squares[133][62], null);
		Templates.TREE.makeCopy(Game.level.squares[133][65], null);
		Templates.TREE.makeCopy(Game.level.squares[133][66], null);
		Templates.TREE.makeCopy(Game.level.squares[133][83], null);
		Templates.TREE.makeCopy(Game.level.squares[134][14], null);
		Templates.TREE.makeCopy(Game.level.squares[134][24], null);
		Templates.TREE.makeCopy(Game.level.squares[134][31], null);
		Templates.TREE.makeCopy(Game.level.squares[134][43], null);
		Templates.TREE.makeCopy(Game.level.squares[134][48], null);
		Templates.TREE.makeCopy(Game.level.squares[134][51], null);
		Templates.TREE.makeCopy(Game.level.squares[134][56], null);
		Templates.TREE.makeCopy(Game.level.squares[134][62], null);
		Templates.TREE.makeCopy(Game.level.squares[134][77], null);
		Templates.TREE.makeCopy(Game.level.squares[134][93], null);
		Templates.TREE.makeCopy(Game.level.squares[135][22], null);
		Templates.TREE.makeCopy(Game.level.squares[135][23], null);
		Templates.TREE.makeCopy(Game.level.squares[135][25], null);
		Templates.TREE.makeCopy(Game.level.squares[135][32], null);
		Templates.TREE.makeCopy(Game.level.squares[135][36], null);
		Templates.TREE.makeCopy(Game.level.squares[135][37], null);
		Templates.TREE.makeCopy(Game.level.squares[135][39], null);
		Templates.TREE.makeCopy(Game.level.squares[135][45], null);
		Templates.TREE.makeCopy(Game.level.squares[135][50], null);
		Templates.TREE.makeCopy(Game.level.squares[135][51], null);
		Templates.TREE.makeCopy(Game.level.squares[135][52], null);
		Templates.TREE.makeCopy(Game.level.squares[135][53], null);
		Templates.TREE.makeCopy(Game.level.squares[135][57], null);
		Templates.TREE.makeCopy(Game.level.squares[135][58], null);
		Templates.TREE.makeCopy(Game.level.squares[135][72], null);
		Templates.TREE.makeCopy(Game.level.squares[135][76], null);
		Templates.TREE.makeCopy(Game.level.squares[135][80], null);
		Templates.TREE.makeCopy(Game.level.squares[135][86], null);
		Templates.TREE.makeCopy(Game.level.squares[136][22], null);
		Templates.TREE.makeCopy(Game.level.squares[136][25], null);
		Templates.TREE.makeCopy(Game.level.squares[136][26], null);
		Templates.TREE.makeCopy(Game.level.squares[136][30], null);
		Templates.TREE.makeCopy(Game.level.squares[136][31], null);
		Templates.TREE.makeCopy(Game.level.squares[136][32], null);
		Templates.TREE.makeCopy(Game.level.squares[136][39], null);
		Templates.TREE.makeCopy(Game.level.squares[136][40], null);
		Templates.TREE.makeCopy(Game.level.squares[136][46], null);
		Templates.TREE.makeCopy(Game.level.squares[136][48], null);
		Templates.TREE.makeCopy(Game.level.squares[136][49], null);
		Templates.TREE.makeCopy(Game.level.squares[136][52], null);
		Templates.TREE.makeCopy(Game.level.squares[136][64], null);
		Templates.TREE.makeCopy(Game.level.squares[136][66], null);
		Templates.TREE.makeCopy(Game.level.squares[136][87], null);
		Templates.TREE.makeCopy(Game.level.squares[137][11], null);
		Templates.TREE.makeCopy(Game.level.squares[137][32], null);
		Templates.TREE.makeCopy(Game.level.squares[137][40], null);
		Templates.TREE.makeCopy(Game.level.squares[137][47], null);
		Templates.TREE.makeCopy(Game.level.squares[137][53], null);
		Templates.TREE.makeCopy(Game.level.squares[137][61], null);
		Templates.TREE.makeCopy(Game.level.squares[137][63], null);
		Templates.TREE.makeCopy(Game.level.squares[137][64], null);
		Templates.TREE.makeCopy(Game.level.squares[137][69], null);
		Templates.TREE.makeCopy(Game.level.squares[137][76], null);
		Templates.TREE.makeCopy(Game.level.squares[138][15], null);
		Templates.TREE.makeCopy(Game.level.squares[138][17], null);
		Templates.TREE.makeCopy(Game.level.squares[138][22], null);
		Templates.TREE.makeCopy(Game.level.squares[138][34], null);
		Templates.TREE.makeCopy(Game.level.squares[138][42], null);
		Templates.TREE.makeCopy(Game.level.squares[138][43], null);
		Templates.TREE.makeCopy(Game.level.squares[138][48], null);
		Templates.TREE.makeCopy(Game.level.squares[138][59], null);
		Templates.TREE.makeCopy(Game.level.squares[138][85], null);
		Templates.TREE.makeCopy(Game.level.squares[138][87], null);
		Templates.TREE.makeCopy(Game.level.squares[138][89], null);
		Templates.TREE.makeCopy(Game.level.squares[138][9], null);
		Templates.TREE.makeCopy(Game.level.squares[138][90], null);
		Templates.TREE.makeCopy(Game.level.squares[138][96], null);
		Templates.TREE.makeCopy(Game.level.squares[139][18], null);
		Templates.TREE.makeCopy(Game.level.squares[139][24], null);
		Templates.TREE.makeCopy(Game.level.squares[139][26], null);
		Templates.TREE.makeCopy(Game.level.squares[139][29], null);
		Templates.TREE.makeCopy(Game.level.squares[139][31], null);
		Templates.TREE.makeCopy(Game.level.squares[139][33], null);
		Templates.TREE.makeCopy(Game.level.squares[139][36], null);
		Templates.TREE.makeCopy(Game.level.squares[139][37], null);
		Templates.TREE.makeCopy(Game.level.squares[139][39], null);
		Templates.TREE.makeCopy(Game.level.squares[139][43], null);
		Templates.TREE.makeCopy(Game.level.squares[139][44], null);
		Templates.TREE.makeCopy(Game.level.squares[139][47], null);
		Templates.TREE.makeCopy(Game.level.squares[139][51], null);
		Templates.TREE.makeCopy(Game.level.squares[139][54], null);
		Templates.TREE.makeCopy(Game.level.squares[139][55], null);
		Templates.TREE.makeCopy(Game.level.squares[139][61], null);
		Templates.TREE.makeCopy(Game.level.squares[139][62], null);
		Templates.TREE.makeCopy(Game.level.squares[139][63], null);
		Templates.TREE.makeCopy(Game.level.squares[139][73], null);
		Templates.TREE.makeCopy(Game.level.squares[139][81], null);
		Templates.TREE.makeCopy(Game.level.squares[139][87], null);
		Templates.TREE.makeCopy(Game.level.squares[140][27], null);
		Templates.TREE.makeCopy(Game.level.squares[140][32], null);
		Templates.TREE.makeCopy(Game.level.squares[140][33], null);
		Templates.TREE.makeCopy(Game.level.squares[140][37], null);
		Templates.TREE.makeCopy(Game.level.squares[140][38], null);
		Templates.TREE.makeCopy(Game.level.squares[140][42], null);
		Templates.TREE.makeCopy(Game.level.squares[140][48], null);
		Templates.TREE.makeCopy(Game.level.squares[140][49], null);
		Templates.TREE.makeCopy(Game.level.squares[140][52], null);
		Templates.TREE.makeCopy(Game.level.squares[140][63], null);
		Templates.TREE.makeCopy(Game.level.squares[140][72], null);
		Templates.TREE.makeCopy(Game.level.squares[140][77], null);
		Templates.TREE.makeCopy(Game.level.squares[140][94], null);
		Templates.TREE.makeCopy(Game.level.squares[141][13], null);
		Templates.TREE.makeCopy(Game.level.squares[141][27], null);
		Templates.TREE.makeCopy(Game.level.squares[141][29], null);
		Templates.TREE.makeCopy(Game.level.squares[141][30], null);
		Templates.TREE.makeCopy(Game.level.squares[141][31], null);
		Templates.TREE.makeCopy(Game.level.squares[141][33], null);
		Templates.TREE.makeCopy(Game.level.squares[141][40], null);
		Templates.TREE.makeCopy(Game.level.squares[141][43], null);
		Templates.TREE.makeCopy(Game.level.squares[141][45], null);
		Templates.TREE.makeCopy(Game.level.squares[141][46], null);
		Templates.TREE.makeCopy(Game.level.squares[141][49], null);
		Templates.TREE.makeCopy(Game.level.squares[141][50], null);
		Templates.TREE.makeCopy(Game.level.squares[141][53], null);
		Templates.TREE.makeCopy(Game.level.squares[141][54], null);
		Templates.TREE.makeCopy(Game.level.squares[141][60], null);
		Templates.TREE.makeCopy(Game.level.squares[141][61], null);
		Templates.TREE.makeCopy(Game.level.squares[141][63], null);
		Templates.TREE.makeCopy(Game.level.squares[141][66], null);
		Templates.TREE.makeCopy(Game.level.squares[141][69], null);
		Templates.TREE.makeCopy(Game.level.squares[141][79], null);
		Templates.TREE.makeCopy(Game.level.squares[141][83], null);
		Templates.TREE.makeCopy(Game.level.squares[141][95], null);
		Templates.TREE.makeCopy(Game.level.squares[142][10], null);
		Templates.TREE.makeCopy(Game.level.squares[142][20], null);
		Templates.TREE.makeCopy(Game.level.squares[142][23], null);
		Templates.TREE.makeCopy(Game.level.squares[142][24], null);
		Templates.TREE.makeCopy(Game.level.squares[142][35], null);
		Templates.TREE.makeCopy(Game.level.squares[142][36], null);
		Templates.TREE.makeCopy(Game.level.squares[142][37], null);
		Templates.TREE.makeCopy(Game.level.squares[142][39], null);
		Templates.TREE.makeCopy(Game.level.squares[142][40], null);
		Templates.TREE.makeCopy(Game.level.squares[142][43], null);
		Templates.TREE.makeCopy(Game.level.squares[142][44], null);
		Templates.TREE.makeCopy(Game.level.squares[142][45], null);
		Templates.TREE.makeCopy(Game.level.squares[142][48], null);
		Templates.TREE.makeCopy(Game.level.squares[142][49], null);
		Templates.TREE.makeCopy(Game.level.squares[142][59], null);
		Templates.TREE.makeCopy(Game.level.squares[142][61], null);
		Templates.TREE.makeCopy(Game.level.squares[142][62], null);
		Templates.TREE.makeCopy(Game.level.squares[142][71], null);
		Templates.TREE.makeCopy(Game.level.squares[142][75], null);
		Templates.TREE.makeCopy(Game.level.squares[142][76], null);
		Templates.TREE.makeCopy(Game.level.squares[142][91], null);
		Templates.TREE.makeCopy(Game.level.squares[142][94], null);
		Templates.TREE.makeCopy(Game.level.squares[143][10], null);
		Templates.TREE.makeCopy(Game.level.squares[143][11], null);
		Templates.TREE.makeCopy(Game.level.squares[143][23], null);
		Templates.TREE.makeCopy(Game.level.squares[143][25], null);
		Templates.TREE.makeCopy(Game.level.squares[143][26], null);
		Templates.TREE.makeCopy(Game.level.squares[143][31], null);
		Templates.TREE.makeCopy(Game.level.squares[143][32], null);
		Templates.TREE.makeCopy(Game.level.squares[143][38], null);
		Templates.TREE.makeCopy(Game.level.squares[143][40], null);
		Templates.TREE.makeCopy(Game.level.squares[143][47], null);
		Templates.TREE.makeCopy(Game.level.squares[143][48], null);
		Templates.TREE.makeCopy(Game.level.squares[143][49], null);
		Templates.TREE.makeCopy(Game.level.squares[143][50], null);
		Templates.TREE.makeCopy(Game.level.squares[143][51], null);
		Templates.TREE.makeCopy(Game.level.squares[143][52], null);
		Templates.TREE.makeCopy(Game.level.squares[143][54], null);
		Templates.TREE.makeCopy(Game.level.squares[143][55], null);
		Templates.TREE.makeCopy(Game.level.squares[143][64], null);
		Templates.TREE.makeCopy(Game.level.squares[143][66], null);
		Templates.TREE.makeCopy(Game.level.squares[143][71], null);
		Templates.TREE.makeCopy(Game.level.squares[143][73], null);
		Templates.TREE.makeCopy(Game.level.squares[144][13], null);
		Templates.TREE.makeCopy(Game.level.squares[144][20], null);
		Templates.TREE.makeCopy(Game.level.squares[144][21], null);
		Templates.TREE.makeCopy(Game.level.squares[144][23], null);
		Templates.TREE.makeCopy(Game.level.squares[144][26], null);
		Templates.TREE.makeCopy(Game.level.squares[144][28], null);
		Templates.TREE.makeCopy(Game.level.squares[144][31], null);
		Templates.TREE.makeCopy(Game.level.squares[144][33], null);
		Templates.TREE.makeCopy(Game.level.squares[144][34], null);
		Templates.TREE.makeCopy(Game.level.squares[144][43], null);
		Templates.TREE.makeCopy(Game.level.squares[144][48], null);
		Templates.TREE.makeCopy(Game.level.squares[144][49], null);
		Templates.TREE.makeCopy(Game.level.squares[144][50], null);
		Templates.TREE.makeCopy(Game.level.squares[144][51], null);
		Templates.TREE.makeCopy(Game.level.squares[144][54], null);
		Templates.TREE.makeCopy(Game.level.squares[144][56], null);
		Templates.TREE.makeCopy(Game.level.squares[144][57], null);
		Templates.TREE.makeCopy(Game.level.squares[144][58], null);
		Templates.TREE.makeCopy(Game.level.squares[144][59], null);
		Templates.TREE.makeCopy(Game.level.squares[144][68], null);
		Templates.TREE.makeCopy(Game.level.squares[144][95], null);
		Templates.TREE.makeCopy(Game.level.squares[145][21], null);
		Templates.TREE.makeCopy(Game.level.squares[145][27], null);
		Templates.TREE.makeCopy(Game.level.squares[145][30], null);
		Templates.TREE.makeCopy(Game.level.squares[145][32], null);
		Templates.TREE.makeCopy(Game.level.squares[145][36], null);
		Templates.TREE.makeCopy(Game.level.squares[145][41], null);
		Templates.TREE.makeCopy(Game.level.squares[145][47], null);
		Templates.TREE.makeCopy(Game.level.squares[145][48], null);
		Templates.TREE.makeCopy(Game.level.squares[145][54], null);
		Templates.TREE.makeCopy(Game.level.squares[145][57], null);
		Templates.TREE.makeCopy(Game.level.squares[145][59], null);
		Templates.TREE.makeCopy(Game.level.squares[145][60], null);
		Templates.TREE.makeCopy(Game.level.squares[145][66], null);
		Templates.TREE.makeCopy(Game.level.squares[145][72], null);
		Templates.TREE.makeCopy(Game.level.squares[145][75], null);
		Templates.TREE.makeCopy(Game.level.squares[145][78], null);
		Templates.TREE.makeCopy(Game.level.squares[145][93], null);
		Templates.TREE.makeCopy(Game.level.squares[146][19], null);
		Templates.TREE.makeCopy(Game.level.squares[146][33], null);
		Templates.TREE.makeCopy(Game.level.squares[146][35], null);
		Templates.TREE.makeCopy(Game.level.squares[146][37], null);
		Templates.TREE.makeCopy(Game.level.squares[146][39], null);
		Templates.TREE.makeCopy(Game.level.squares[146][43], null);
		Templates.TREE.makeCopy(Game.level.squares[146][44], null);
		Templates.TREE.makeCopy(Game.level.squares[146][46], null);
		Templates.TREE.makeCopy(Game.level.squares[146][47], null);
		Templates.TREE.makeCopy(Game.level.squares[146][51], null);
		Templates.TREE.makeCopy(Game.level.squares[146][52], null);
		Templates.TREE.makeCopy(Game.level.squares[146][61], null);
		Templates.TREE.makeCopy(Game.level.squares[146][63], null);
		Templates.TREE.makeCopy(Game.level.squares[146][69], null);
		Templates.TREE.makeCopy(Game.level.squares[146][71], null);
		Templates.TREE.makeCopy(Game.level.squares[146][72], null);
		Templates.TREE.makeCopy(Game.level.squares[146][73], null);
		Templates.TREE.makeCopy(Game.level.squares[146][80], null);
		Templates.TREE.makeCopy(Game.level.squares[146][86], null);
		Templates.TREE.makeCopy(Game.level.squares[146][87], null);
		Templates.TREE.makeCopy(Game.level.squares[146][88], null);
		Templates.TREE.makeCopy(Game.level.squares[146][90], null);
		Templates.TREE.makeCopy(Game.level.squares[147][10], null);
		Templates.TREE.makeCopy(Game.level.squares[147][11], null);
		Templates.TREE.makeCopy(Game.level.squares[147][19], null);
		Templates.TREE.makeCopy(Game.level.squares[147][23], null);
		Templates.TREE.makeCopy(Game.level.squares[147][25], null);
		Templates.TREE.makeCopy(Game.level.squares[147][33], null);
		Templates.TREE.makeCopy(Game.level.squares[147][36], null);
		Templates.TREE.makeCopy(Game.level.squares[147][42], null);
		Templates.TREE.makeCopy(Game.level.squares[147][44], null);
		Templates.TREE.makeCopy(Game.level.squares[147][46], null);
		Templates.TREE.makeCopy(Game.level.squares[147][47], null);
		Templates.TREE.makeCopy(Game.level.squares[147][49], null);
		Templates.TREE.makeCopy(Game.level.squares[147][53], null);
		Templates.TREE.makeCopy(Game.level.squares[147][55], null);
		Templates.TREE.makeCopy(Game.level.squares[147][58], null);
		Templates.TREE.makeCopy(Game.level.squares[147][60], null);
		Templates.TREE.makeCopy(Game.level.squares[147][61], null);
		Templates.TREE.makeCopy(Game.level.squares[147][63], null);
		Templates.TREE.makeCopy(Game.level.squares[147][64], null);
		Templates.TREE.makeCopy(Game.level.squares[147][71], null);
		Templates.TREE.makeCopy(Game.level.squares[147][80], null);
		Templates.TREE.makeCopy(Game.level.squares[147][81], null);
		Templates.TREE.makeCopy(Game.level.squares[147][84], null);
		Templates.TREE.makeCopy(Game.level.squares[147][86], null);
		Templates.TREE.makeCopy(Game.level.squares[147][88], null);
		Templates.TREE.makeCopy(Game.level.squares[148][11], null);
		Templates.TREE.makeCopy(Game.level.squares[148][16], null);
		Templates.TREE.makeCopy(Game.level.squares[148][31], null);
		Templates.TREE.makeCopy(Game.level.squares[148][39], null);
		Templates.TREE.makeCopy(Game.level.squares[148][45], null);
		Templates.TREE.makeCopy(Game.level.squares[148][47], null);
		Templates.TREE.makeCopy(Game.level.squares[148][48], null);
		Templates.TREE.makeCopy(Game.level.squares[148][52], null);
		Templates.TREE.makeCopy(Game.level.squares[148][54], null);
		Templates.TREE.makeCopy(Game.level.squares[148][55], null);
		Templates.TREE.makeCopy(Game.level.squares[148][58], null);
		Templates.TREE.makeCopy(Game.level.squares[148][67], null);
		Templates.TREE.makeCopy(Game.level.squares[148][70], null);
		Templates.TREE.makeCopy(Game.level.squares[148][72], null);
		Templates.TREE.makeCopy(Game.level.squares[148][87], null);
		Templates.TREE.makeCopy(Game.level.squares[149][14], null);
		Templates.TREE.makeCopy(Game.level.squares[149][17], null);
		Templates.TREE.makeCopy(Game.level.squares[149][29], null);
		Templates.TREE.makeCopy(Game.level.squares[149][32], null);
		Templates.TREE.makeCopy(Game.level.squares[149][33], null);
		Templates.TREE.makeCopy(Game.level.squares[149][38], null);
		Templates.TREE.makeCopy(Game.level.squares[149][39], null);
		Templates.TREE.makeCopy(Game.level.squares[149][40], null);
		Templates.TREE.makeCopy(Game.level.squares[149][42], null);
		Templates.TREE.makeCopy(Game.level.squares[149][44], null);
		Templates.TREE.makeCopy(Game.level.squares[149][47], null);
		Templates.TREE.makeCopy(Game.level.squares[149][49], null);
		Templates.TREE.makeCopy(Game.level.squares[149][50], null);
		Templates.TREE.makeCopy(Game.level.squares[149][51], null);
		Templates.TREE.makeCopy(Game.level.squares[149][52], null);
		Templates.TREE.makeCopy(Game.level.squares[149][54], null);
		Templates.TREE.makeCopy(Game.level.squares[149][55], null);
		Templates.TREE.makeCopy(Game.level.squares[149][56], null);
		Templates.TREE.makeCopy(Game.level.squares[149][66], null);
		Templates.TREE.makeCopy(Game.level.squares[149][67], null);
		Templates.TREE.makeCopy(Game.level.squares[149][70], null);
		Templates.TREE.makeCopy(Game.level.squares[149][82], null);
		Templates.TREE.makeCopy(Game.level.squares[149][89], null);
		Templates.TREE.makeCopy(Game.level.squares[149][93], null);
		Templates.TREE.makeCopy(Game.level.squares[149][94], null);
		Templates.TREE.makeCopy(Game.level.squares[149][95], null);
		Templates.TREE.makeCopy(Game.level.squares[150][12], null);
		Templates.TREE.makeCopy(Game.level.squares[150][21], null);
		Templates.TREE.makeCopy(Game.level.squares[150][22], null);
		Templates.TREE.makeCopy(Game.level.squares[150][26], null);
		Templates.TREE.makeCopy(Game.level.squares[150][30], null);
		Templates.TREE.makeCopy(Game.level.squares[150][37], null);
		Templates.TREE.makeCopy(Game.level.squares[150][39], null);
		Templates.TREE.makeCopy(Game.level.squares[150][42], null);
		Templates.TREE.makeCopy(Game.level.squares[150][49], null);
		Templates.TREE.makeCopy(Game.level.squares[150][50], null);
		Templates.TREE.makeCopy(Game.level.squares[150][51], null);
		Templates.TREE.makeCopy(Game.level.squares[150][56], null);
		Templates.TREE.makeCopy(Game.level.squares[150][57], null);
		Templates.TREE.makeCopy(Game.level.squares[150][58], null);
		Templates.TREE.makeCopy(Game.level.squares[150][60], null);
		Templates.TREE.makeCopy(Game.level.squares[150][63], null);
		Templates.TREE.makeCopy(Game.level.squares[150][64], null);
		Templates.TREE.makeCopy(Game.level.squares[150][65], null);
		Templates.TREE.makeCopy(Game.level.squares[150][67], null);
		Templates.TREE.makeCopy(Game.level.squares[150][70], null);
		Templates.TREE.makeCopy(Game.level.squares[150][71], null);
		Templates.TREE.makeCopy(Game.level.squares[150][73], null);
		Templates.TREE.makeCopy(Game.level.squares[150][76], null);
		Templates.TREE.makeCopy(Game.level.squares[150][80], null);
		Templates.TREE.makeCopy(Game.level.squares[150][83], null);
		Templates.TREE.makeCopy(Game.level.squares[150][87], null);
		Templates.TREE.makeCopy(Game.level.squares[150][89], null);
		Templates.TREE.makeCopy(Game.level.squares[151][14], null);
		Templates.TREE.makeCopy(Game.level.squares[151][18], null);
		Templates.TREE.makeCopy(Game.level.squares[151][21], null);
		Templates.TREE.makeCopy(Game.level.squares[151][24], null);
		Templates.TREE.makeCopy(Game.level.squares[151][28], null);
		Templates.TREE.makeCopy(Game.level.squares[151][31], null);
		Templates.TREE.makeCopy(Game.level.squares[151][33], null);
		Templates.TREE.makeCopy(Game.level.squares[151][34], null);
		Templates.TREE.makeCopy(Game.level.squares[151][35], null);
		Templates.TREE.makeCopy(Game.level.squares[151][39], null);
		Templates.TREE.makeCopy(Game.level.squares[151][40], null);
		Templates.TREE.makeCopy(Game.level.squares[151][41], null);
		Templates.TREE.makeCopy(Game.level.squares[151][43], null);
		Templates.TREE.makeCopy(Game.level.squares[151][45], null);
		Templates.TREE.makeCopy(Game.level.squares[151][46], null);
		Templates.TREE.makeCopy(Game.level.squares[151][47], null);
		Templates.TREE.makeCopy(Game.level.squares[151][48], null);
		Templates.TREE.makeCopy(Game.level.squares[151][49], null);
		Templates.TREE.makeCopy(Game.level.squares[151][52], null);
		Templates.TREE.makeCopy(Game.level.squares[151][54], null);
		Templates.TREE.makeCopy(Game.level.squares[151][55], null);
		Templates.TREE.makeCopy(Game.level.squares[151][59], null);
		Templates.TREE.makeCopy(Game.level.squares[151][79], null);
		Templates.TREE.makeCopy(Game.level.squares[151][88], null);
		Templates.TREE.makeCopy(Game.level.squares[151][9], null);
		Templates.TREE.makeCopy(Game.level.squares[152][11], null);
		Templates.TREE.makeCopy(Game.level.squares[152][17], null);
		Templates.TREE.makeCopy(Game.level.squares[152][18], null);
		Templates.TREE.makeCopy(Game.level.squares[152][29], null);
		Templates.TREE.makeCopy(Game.level.squares[152][30], null);
		Templates.TREE.makeCopy(Game.level.squares[152][31], null);
		Templates.TREE.makeCopy(Game.level.squares[152][32], null);
		Templates.TREE.makeCopy(Game.level.squares[152][36], null);
		Templates.TREE.makeCopy(Game.level.squares[152][37], null);
		Templates.TREE.makeCopy(Game.level.squares[152][40], null);
		Templates.TREE.makeCopy(Game.level.squares[152][42], null);
		Templates.TREE.makeCopy(Game.level.squares[152][43], null);
		Templates.TREE.makeCopy(Game.level.squares[152][44], null);
		Templates.TREE.makeCopy(Game.level.squares[152][45], null);
		Templates.TREE.makeCopy(Game.level.squares[152][47], null);
		Templates.TREE.makeCopy(Game.level.squares[152][48], null);
		Templates.TREE.makeCopy(Game.level.squares[152][50], null);
		Templates.TREE.makeCopy(Game.level.squares[152][56], null);
		Templates.TREE.makeCopy(Game.level.squares[152][57], null);
		Templates.TREE.makeCopy(Game.level.squares[152][6], null);
		Templates.TREE.makeCopy(Game.level.squares[152][65], null);
		Templates.TREE.makeCopy(Game.level.squares[152][68], null);
		Templates.TREE.makeCopy(Game.level.squares[152][69], null);
		Templates.TREE.makeCopy(Game.level.squares[152][7], null);
		Templates.TREE.makeCopy(Game.level.squares[152][72], null);
		Templates.TREE.makeCopy(Game.level.squares[152][77], null);
		Templates.TREE.makeCopy(Game.level.squares[152][83], null);
		Templates.TREE.makeCopy(Game.level.squares[153][20], null);
		Templates.TREE.makeCopy(Game.level.squares[153][21], null);
		Templates.TREE.makeCopy(Game.level.squares[153][32], null);
		Templates.TREE.makeCopy(Game.level.squares[153][33], null);
		Templates.TREE.makeCopy(Game.level.squares[153][40], null);
		Templates.TREE.makeCopy(Game.level.squares[153][43], null);
		Templates.TREE.makeCopy(Game.level.squares[153][44], null);
		Templates.TREE.makeCopy(Game.level.squares[153][45], null);
		Templates.TREE.makeCopy(Game.level.squares[153][46], null);
		Templates.TREE.makeCopy(Game.level.squares[153][49], null);
		Templates.TREE.makeCopy(Game.level.squares[153][56], null);
		Templates.TREE.makeCopy(Game.level.squares[153][58], null);
		Templates.TREE.makeCopy(Game.level.squares[153][70], null);
		Templates.TREE.makeCopy(Game.level.squares[153][71], null);
		Templates.TREE.makeCopy(Game.level.squares[153][73], null);
		Templates.TREE.makeCopy(Game.level.squares[153][74], null);
		Templates.TREE.makeCopy(Game.level.squares[153][80], null);
		Templates.TREE.makeCopy(Game.level.squares[153][82], null);
		Templates.TREE.makeCopy(Game.level.squares[153][84], null);
		Templates.TREE.makeCopy(Game.level.squares[153][86], null);
		Templates.TREE.makeCopy(Game.level.squares[153][88], null);
		Templates.TREE.makeCopy(Game.level.squares[153][89], null);
		Templates.TREE.makeCopy(Game.level.squares[154][11], null);
		Templates.TREE.makeCopy(Game.level.squares[154][12], null);
		Templates.TREE.makeCopy(Game.level.squares[154][16], null);
		Templates.TREE.makeCopy(Game.level.squares[154][19], null);
		Templates.TREE.makeCopy(Game.level.squares[154][23], null);
		Templates.TREE.makeCopy(Game.level.squares[154][25], null);
		Templates.TREE.makeCopy(Game.level.squares[154][26], null);
		Templates.TREE.makeCopy(Game.level.squares[154][30], null);
		Templates.TREE.makeCopy(Game.level.squares[154][32], null);
		Templates.TREE.makeCopy(Game.level.squares[154][33], null);
		Templates.TREE.makeCopy(Game.level.squares[154][36], null);
		Templates.TREE.makeCopy(Game.level.squares[154][38], null);
		Templates.TREE.makeCopy(Game.level.squares[154][39], null);
		Templates.TREE.makeCopy(Game.level.squares[154][41], null);
		Templates.TREE.makeCopy(Game.level.squares[154][42], null);
		Templates.TREE.makeCopy(Game.level.squares[154][43], null);
		Templates.TREE.makeCopy(Game.level.squares[154][49], null);
		Templates.TREE.makeCopy(Game.level.squares[154][52], null);
		Templates.TREE.makeCopy(Game.level.squares[154][53], null);
		Templates.TREE.makeCopy(Game.level.squares[154][54], null);
		Templates.TREE.makeCopy(Game.level.squares[154][57], null);
		Templates.TREE.makeCopy(Game.level.squares[154][58], null);
		Templates.TREE.makeCopy(Game.level.squares[154][59], null);
		Templates.TREE.makeCopy(Game.level.squares[154][60], null);
		Templates.TREE.makeCopy(Game.level.squares[154][62], null);
		Templates.TREE.makeCopy(Game.level.squares[154][63], null);
		Templates.TREE.makeCopy(Game.level.squares[154][65], null);
		Templates.TREE.makeCopy(Game.level.squares[154][66], null);
		Templates.TREE.makeCopy(Game.level.squares[154][67], null);
		Templates.TREE.makeCopy(Game.level.squares[154][72], null);
		Templates.TREE.makeCopy(Game.level.squares[154][80], null);
		Templates.TREE.makeCopy(Game.level.squares[154][82], null);
		Templates.TREE.makeCopy(Game.level.squares[154][85], null);
		Templates.TREE.makeCopy(Game.level.squares[155][16], null);
		Templates.TREE.makeCopy(Game.level.squares[155][22], null);
		Templates.TREE.makeCopy(Game.level.squares[155][23], null);
		Templates.TREE.makeCopy(Game.level.squares[155][26], null);
		Templates.TREE.makeCopy(Game.level.squares[155][30], null);
		Templates.TREE.makeCopy(Game.level.squares[155][42], null);
		Templates.TREE.makeCopy(Game.level.squares[155][43], null);
		Templates.TREE.makeCopy(Game.level.squares[155][44], null);
		Templates.TREE.makeCopy(Game.level.squares[155][45], null);
		Templates.TREE.makeCopy(Game.level.squares[155][47], null);
		Templates.TREE.makeCopy(Game.level.squares[155][49], null);
		Templates.TREE.makeCopy(Game.level.squares[155][50], null);
		Templates.TREE.makeCopy(Game.level.squares[155][52], null);
		Templates.TREE.makeCopy(Game.level.squares[155][54], null);
		Templates.TREE.makeCopy(Game.level.squares[155][57], null);
		Templates.TREE.makeCopy(Game.level.squares[155][61], null);
		Templates.TREE.makeCopy(Game.level.squares[155][64], null);
		Templates.TREE.makeCopy(Game.level.squares[155][66], null);
		Templates.TREE.makeCopy(Game.level.squares[155][69], null);
		Templates.TREE.makeCopy(Game.level.squares[155][71], null);
		Templates.TREE.makeCopy(Game.level.squares[155][88], null);
		Templates.TREE.makeCopy(Game.level.squares[155][9], null);
		Templates.TREE.makeCopy(Game.level.squares[155][90], null);
		Templates.TREE.makeCopy(Game.level.squares[155][92], null);
		Templates.TREE.makeCopy(Game.level.squares[156][18], null);
		Templates.TREE.makeCopy(Game.level.squares[156][19], null);
		Templates.TREE.makeCopy(Game.level.squares[156][30], null);
		Templates.TREE.makeCopy(Game.level.squares[156][34], null);
		Templates.TREE.makeCopy(Game.level.squares[156][39], null);
		Templates.TREE.makeCopy(Game.level.squares[156][45], null);
		Templates.TREE.makeCopy(Game.level.squares[156][46], null);
		Templates.TREE.makeCopy(Game.level.squares[156][49], null);
		Templates.TREE.makeCopy(Game.level.squares[156][50], null);
		Templates.TREE.makeCopy(Game.level.squares[156][51], null);
		Templates.TREE.makeCopy(Game.level.squares[156][53], null);
		Templates.TREE.makeCopy(Game.level.squares[156][54], null);
		Templates.TREE.makeCopy(Game.level.squares[156][55], null);
		Templates.TREE.makeCopy(Game.level.squares[156][57], null);
		Templates.TREE.makeCopy(Game.level.squares[156][61], null);
		Templates.TREE.makeCopy(Game.level.squares[156][63], null);
		Templates.TREE.makeCopy(Game.level.squares[156][64], null);
		Templates.TREE.makeCopy(Game.level.squares[156][65], null);
		Templates.TREE.makeCopy(Game.level.squares[156][66], null);
		Templates.TREE.makeCopy(Game.level.squares[156][67], null);
		Templates.TREE.makeCopy(Game.level.squares[156][69], null);
		Templates.TREE.makeCopy(Game.level.squares[156][73], null);
		Templates.TREE.makeCopy(Game.level.squares[157][10], null);
		Templates.TREE.makeCopy(Game.level.squares[157][11], null);
		Templates.TREE.makeCopy(Game.level.squares[157][15], null);
		Templates.TREE.makeCopy(Game.level.squares[157][18], null);
		Templates.TREE.makeCopy(Game.level.squares[157][23], null);
		Templates.TREE.makeCopy(Game.level.squares[157][30], null);
		Templates.TREE.makeCopy(Game.level.squares[157][33], null);
		Templates.TREE.makeCopy(Game.level.squares[157][35], null);
		Templates.TREE.makeCopy(Game.level.squares[157][45], null);
		Templates.TREE.makeCopy(Game.level.squares[157][48], null);
		Templates.TREE.makeCopy(Game.level.squares[157][49], null);
		Templates.TREE.makeCopy(Game.level.squares[157][51], null);
		Templates.TREE.makeCopy(Game.level.squares[157][57], null);
		Templates.TREE.makeCopy(Game.level.squares[157][58], null);
		Templates.TREE.makeCopy(Game.level.squares[157][60], null);
		Templates.TREE.makeCopy(Game.level.squares[157][61], null);
		Templates.TREE.makeCopy(Game.level.squares[157][62], null);
		Templates.TREE.makeCopy(Game.level.squares[157][67], null);
		Templates.TREE.makeCopy(Game.level.squares[157][74], null);
		Templates.TREE.makeCopy(Game.level.squares[157][76], null);
		Templates.TREE.makeCopy(Game.level.squares[157][79], null);
		Templates.TREE.makeCopy(Game.level.squares[157][80], null);
		Templates.TREE.makeCopy(Game.level.squares[157][82], null);
		Templates.TREE.makeCopy(Game.level.squares[157][86], null);
		Templates.TREE.makeCopy(Game.level.squares[157][88], null);
		Templates.TREE.makeCopy(Game.level.squares[157][96], null);
		Templates.TREE.makeCopy(Game.level.squares[158][14], null);
		Templates.TREE.makeCopy(Game.level.squares[158][20], null);
		Templates.TREE.makeCopy(Game.level.squares[158][21], null);
		Templates.TREE.makeCopy(Game.level.squares[158][28], null);
		Templates.TREE.makeCopy(Game.level.squares[158][30], null);
		Templates.TREE.makeCopy(Game.level.squares[158][34], null);
		Templates.TREE.makeCopy(Game.level.squares[158][37], null);
		Templates.TREE.makeCopy(Game.level.squares[158][38], null);
		Templates.TREE.makeCopy(Game.level.squares[158][43], null);
		Templates.TREE.makeCopy(Game.level.squares[158][44], null);
		Templates.TREE.makeCopy(Game.level.squares[158][46], null);
		Templates.TREE.makeCopy(Game.level.squares[158][47], null);
		Templates.TREE.makeCopy(Game.level.squares[158][49], null);
		Templates.TREE.makeCopy(Game.level.squares[158][50], null);
		Templates.TREE.makeCopy(Game.level.squares[158][52], null);
		Templates.TREE.makeCopy(Game.level.squares[158][53], null);
		Templates.TREE.makeCopy(Game.level.squares[158][54], null);
		Templates.TREE.makeCopy(Game.level.squares[158][59], null);
		Templates.TREE.makeCopy(Game.level.squares[158][60], null);
		Templates.TREE.makeCopy(Game.level.squares[158][62], null);
		Templates.TREE.makeCopy(Game.level.squares[158][67], null);
		Templates.TREE.makeCopy(Game.level.squares[158][70], null);
		Templates.TREE.makeCopy(Game.level.squares[158][72], null);
		Templates.TREE.makeCopy(Game.level.squares[158][73], null);
		Templates.TREE.makeCopy(Game.level.squares[158][80], null);
		Templates.TREE.makeCopy(Game.level.squares[158][86], null);
		Templates.TREE.makeCopy(Game.level.squares[158][89], null);
		Templates.TREE.makeCopy(Game.level.squares[158][9], null);
		Templates.TREE.makeCopy(Game.level.squares[158][90], null);
		Templates.TREE.makeCopy(Game.level.squares[159][14], null);
		Templates.TREE.makeCopy(Game.level.squares[159][20], null);
		Templates.TREE.makeCopy(Game.level.squares[159][25], null);
		Templates.TREE.makeCopy(Game.level.squares[159][26], null);
		Templates.TREE.makeCopy(Game.level.squares[159][31], null);
		Templates.TREE.makeCopy(Game.level.squares[159][32], null);
		Templates.TREE.makeCopy(Game.level.squares[159][34], null);
		Templates.TREE.makeCopy(Game.level.squares[159][37], null);
		Templates.TREE.makeCopy(Game.level.squares[159][38], null);
		Templates.TREE.makeCopy(Game.level.squares[159][44], null);
		Templates.TREE.makeCopy(Game.level.squares[159][45], null);
		Templates.TREE.makeCopy(Game.level.squares[159][46], null);
		Templates.TREE.makeCopy(Game.level.squares[159][48], null);
		Templates.TREE.makeCopy(Game.level.squares[159][49], null);
		Templates.TREE.makeCopy(Game.level.squares[159][50], null);
		Templates.TREE.makeCopy(Game.level.squares[159][51], null);
		Templates.TREE.makeCopy(Game.level.squares[159][55], null);
		Templates.TREE.makeCopy(Game.level.squares[159][58], null);
		Templates.TREE.makeCopy(Game.level.squares[159][60], null);
		Templates.TREE.makeCopy(Game.level.squares[159][65], null);
		Templates.TREE.makeCopy(Game.level.squares[159][67], null);
		Templates.TREE.makeCopy(Game.level.squares[159][68], null);
		Templates.TREE.makeCopy(Game.level.squares[159][70], null);
		Templates.TREE.makeCopy(Game.level.squares[159][74], null);
		Templates.TREE.makeCopy(Game.level.squares[159][81], null);
		Templates.TREE.makeCopy(Game.level.squares[159][82], null);
		Templates.TREE.makeCopy(Game.level.squares[159][86], null);
		Templates.TREE.makeCopy(Game.level.squares[159][87], null);
		Templates.TREE.makeCopy(Game.level.squares[159][92], null);
		Templates.TREE.makeCopy(Game.level.squares[160][15], null);
		Templates.TREE.makeCopy(Game.level.squares[160][18], null);
		Templates.TREE.makeCopy(Game.level.squares[160][22], null);
		Templates.TREE.makeCopy(Game.level.squares[160][32], null);
		Templates.TREE.makeCopy(Game.level.squares[160][33], null);
		Templates.TREE.makeCopy(Game.level.squares[160][34], null);
		Templates.TREE.makeCopy(Game.level.squares[160][35], null);
		Templates.TREE.makeCopy(Game.level.squares[160][36], null);
		Templates.TREE.makeCopy(Game.level.squares[160][38], null);
		Templates.TREE.makeCopy(Game.level.squares[160][41], null);
		Templates.TREE.makeCopy(Game.level.squares[160][45], null);
		Templates.TREE.makeCopy(Game.level.squares[160][48], null);
		Templates.TREE.makeCopy(Game.level.squares[160][56], null);
		Templates.TREE.makeCopy(Game.level.squares[160][57], null);
		Templates.TREE.makeCopy(Game.level.squares[160][58], null);
		Templates.TREE.makeCopy(Game.level.squares[160][61], null);
		Templates.TREE.makeCopy(Game.level.squares[160][63], null);
		Templates.TREE.makeCopy(Game.level.squares[160][64], null);
		Templates.TREE.makeCopy(Game.level.squares[160][66], null);
		Templates.TREE.makeCopy(Game.level.squares[160][67], null);
		Templates.TREE.makeCopy(Game.level.squares[160][68], null);
		Templates.TREE.makeCopy(Game.level.squares[160][69], null);
		Templates.TREE.makeCopy(Game.level.squares[160][7], null);
		Templates.TREE.makeCopy(Game.level.squares[160][71], null);
		Templates.TREE.makeCopy(Game.level.squares[160][74], null);
		Templates.TREE.makeCopy(Game.level.squares[160][78], null);
		Templates.TREE.makeCopy(Game.level.squares[160][79], null);
		Templates.TREE.makeCopy(Game.level.squares[160][80], null);
		Templates.TREE.makeCopy(Game.level.squares[160][83], null);
		Templates.TREE.makeCopy(Game.level.squares[160][91], null);
		Templates.TREE.makeCopy(Game.level.squares[161][17], null);
		Templates.TREE.makeCopy(Game.level.squares[161][21], null);
		Templates.TREE.makeCopy(Game.level.squares[161][26], null);
		Templates.TREE.makeCopy(Game.level.squares[161][29], null);
		Templates.TREE.makeCopy(Game.level.squares[161][33], null);
		Templates.TREE.makeCopy(Game.level.squares[161][37], null);
		Templates.TREE.makeCopy(Game.level.squares[161][4], null);
		Templates.TREE.makeCopy(Game.level.squares[161][44], null);
		Templates.TREE.makeCopy(Game.level.squares[161][47], null);
		Templates.TREE.makeCopy(Game.level.squares[161][51], null);
		Templates.TREE.makeCopy(Game.level.squares[161][56], null);
		Templates.TREE.makeCopy(Game.level.squares[161][60], null);
		Templates.TREE.makeCopy(Game.level.squares[161][63], null);
		Templates.TREE.makeCopy(Game.level.squares[161][67], null);
		Templates.TREE.makeCopy(Game.level.squares[161][68], null);
		Templates.TREE.makeCopy(Game.level.squares[161][69], null);
		Templates.TREE.makeCopy(Game.level.squares[161][71], null);
		Templates.TREE.makeCopy(Game.level.squares[161][79], null);
		Templates.TREE.makeCopy(Game.level.squares[161][82], null);
		Templates.TREE.makeCopy(Game.level.squares[162][21], null);
		Templates.TREE.makeCopy(Game.level.squares[162][25], null);
		Templates.TREE.makeCopy(Game.level.squares[162][26], null);
		Templates.TREE.makeCopy(Game.level.squares[162][31], null);
		Templates.TREE.makeCopy(Game.level.squares[162][34], null);
		Templates.TREE.makeCopy(Game.level.squares[162][42], null);
		Templates.TREE.makeCopy(Game.level.squares[162][43], null);
		Templates.TREE.makeCopy(Game.level.squares[162][51], null);
		Templates.TREE.makeCopy(Game.level.squares[162][54], null);
		Templates.TREE.makeCopy(Game.level.squares[162][56], null);
		Templates.TREE.makeCopy(Game.level.squares[162][59], null);
		Templates.TREE.makeCopy(Game.level.squares[162][60], null);
		Templates.TREE.makeCopy(Game.level.squares[162][61], null);
		Templates.TREE.makeCopy(Game.level.squares[162][74], null);
		Templates.TREE.makeCopy(Game.level.squares[162][77], null);
		Templates.TREE.makeCopy(Game.level.squares[162][80], null);
		Templates.TREE.makeCopy(Game.level.squares[162][88], null);
		Templates.TREE.makeCopy(Game.level.squares[163][10], null);
		Templates.TREE.makeCopy(Game.level.squares[163][14], null);
		Templates.TREE.makeCopy(Game.level.squares[163][17], null);
		Templates.TREE.makeCopy(Game.level.squares[163][23], null);
		Templates.TREE.makeCopy(Game.level.squares[163][29], null);
		Templates.TREE.makeCopy(Game.level.squares[163][30], null);
		Templates.TREE.makeCopy(Game.level.squares[163][31], null);
		Templates.TREE.makeCopy(Game.level.squares[163][33], null);
		Templates.TREE.makeCopy(Game.level.squares[163][39], null);
		Templates.TREE.makeCopy(Game.level.squares[163][41], null);
		Templates.TREE.makeCopy(Game.level.squares[163][47], null);
		Templates.TREE.makeCopy(Game.level.squares[163][48], null);
		Templates.TREE.makeCopy(Game.level.squares[163][50], null);
		Templates.TREE.makeCopy(Game.level.squares[163][54], null);
		Templates.TREE.makeCopy(Game.level.squares[163][55], null);
		Templates.TREE.makeCopy(Game.level.squares[163][56], null);
		Templates.TREE.makeCopy(Game.level.squares[163][64], null);
		Templates.TREE.makeCopy(Game.level.squares[163][72], null);
		Templates.TREE.makeCopy(Game.level.squares[163][75], null);
		Templates.TREE.makeCopy(Game.level.squares[163][80], null);
		Templates.TREE.makeCopy(Game.level.squares[163][81], null);
		Templates.TREE.makeCopy(Game.level.squares[163][86], null);
		Templates.TREE.makeCopy(Game.level.squares[163][94], null);
		Templates.TREE.makeCopy(Game.level.squares[163][96], null);
		Templates.TREE.makeCopy(Game.level.squares[164][12], null);
		Templates.TREE.makeCopy(Game.level.squares[164][13], null);
		Templates.TREE.makeCopy(Game.level.squares[164][18], null);
		Templates.TREE.makeCopy(Game.level.squares[164][24], null);
		Templates.TREE.makeCopy(Game.level.squares[164][36], null);
		Templates.TREE.makeCopy(Game.level.squares[164][37], null);
		Templates.TREE.makeCopy(Game.level.squares[164][4], null);
		Templates.TREE.makeCopy(Game.level.squares[164][42], null);
		Templates.TREE.makeCopy(Game.level.squares[164][43], null);
		Templates.TREE.makeCopy(Game.level.squares[164][45], null);
		Templates.TREE.makeCopy(Game.level.squares[164][47], null);
		Templates.TREE.makeCopy(Game.level.squares[164][49], null);
		Templates.TREE.makeCopy(Game.level.squares[164][51], null);
		Templates.TREE.makeCopy(Game.level.squares[164][53], null);
		Templates.TREE.makeCopy(Game.level.squares[164][57], null);
		Templates.TREE.makeCopy(Game.level.squares[164][59], null);
		Templates.TREE.makeCopy(Game.level.squares[164][62], null);
		Templates.TREE.makeCopy(Game.level.squares[164][63], null);
		Templates.TREE.makeCopy(Game.level.squares[164][65], null);
		Templates.TREE.makeCopy(Game.level.squares[164][72], null);
		Templates.TREE.makeCopy(Game.level.squares[164][74], null);
		Templates.TREE.makeCopy(Game.level.squares[164][75], null);
		Templates.TREE.makeCopy(Game.level.squares[164][76], null);
		Templates.TREE.makeCopy(Game.level.squares[164][81], null);
		Templates.TREE.makeCopy(Game.level.squares[164][84], null);
		Templates.TREE.makeCopy(Game.level.squares[164][86], null);
		Templates.TREE.makeCopy(Game.level.squares[165][11], null);
		Templates.TREE.makeCopy(Game.level.squares[165][18], null);
		Templates.TREE.makeCopy(Game.level.squares[165][24], null);
		Templates.TREE.makeCopy(Game.level.squares[165][32], null);
		Templates.TREE.makeCopy(Game.level.squares[165][39], null);
		Templates.TREE.makeCopy(Game.level.squares[165][42], null);
		Templates.TREE.makeCopy(Game.level.squares[165][45], null);
		Templates.TREE.makeCopy(Game.level.squares[165][46], null);
		Templates.TREE.makeCopy(Game.level.squares[165][48], null);
		Templates.TREE.makeCopy(Game.level.squares[165][49], null);
		Templates.TREE.makeCopy(Game.level.squares[165][50], null);
		Templates.TREE.makeCopy(Game.level.squares[165][52], null);
		Templates.TREE.makeCopy(Game.level.squares[165][53], null);
		Templates.TREE.makeCopy(Game.level.squares[165][55], null);
		Templates.TREE.makeCopy(Game.level.squares[165][59], null);
		Templates.TREE.makeCopy(Game.level.squares[165][6], null);
		Templates.TREE.makeCopy(Game.level.squares[165][60], null);
		Templates.TREE.makeCopy(Game.level.squares[165][63], null);
		Templates.TREE.makeCopy(Game.level.squares[165][66], null);
		Templates.TREE.makeCopy(Game.level.squares[165][71], null);
		Templates.TREE.makeCopy(Game.level.squares[165][73], null);
		Templates.TREE.makeCopy(Game.level.squares[165][80], null);
		Templates.TREE.makeCopy(Game.level.squares[165][83], null);
		Templates.TREE.makeCopy(Game.level.squares[166][13], null);
		Templates.TREE.makeCopy(Game.level.squares[166][14], null);
		Templates.TREE.makeCopy(Game.level.squares[166][24], null);
		Templates.TREE.makeCopy(Game.level.squares[166][28], null);
		Templates.TREE.makeCopy(Game.level.squares[166][30], null);
		Templates.TREE.makeCopy(Game.level.squares[166][35], null);
		Templates.TREE.makeCopy(Game.level.squares[166][36], null);
		Templates.TREE.makeCopy(Game.level.squares[166][37], null);
		Templates.TREE.makeCopy(Game.level.squares[166][4], null);
		Templates.TREE.makeCopy(Game.level.squares[166][41], null);
		Templates.TREE.makeCopy(Game.level.squares[166][42], null);
		Templates.TREE.makeCopy(Game.level.squares[166][43], null);
		Templates.TREE.makeCopy(Game.level.squares[166][45], null);
		Templates.TREE.makeCopy(Game.level.squares[166][46], null);
		Templates.TREE.makeCopy(Game.level.squares[166][48], null);
		Templates.TREE.makeCopy(Game.level.squares[166][50], null);
		Templates.TREE.makeCopy(Game.level.squares[166][57], null);
		Templates.TREE.makeCopy(Game.level.squares[166][61], null);
		Templates.TREE.makeCopy(Game.level.squares[166][64], null);
		Templates.TREE.makeCopy(Game.level.squares[166][65], null);
		Templates.TREE.makeCopy(Game.level.squares[166][71], null);
		Templates.TREE.makeCopy(Game.level.squares[166][75], null);
		Templates.TREE.makeCopy(Game.level.squares[166][76], null);
		Templates.TREE.makeCopy(Game.level.squares[166][78], null);
		Templates.TREE.makeCopy(Game.level.squares[166][79], null);
		Templates.TREE.makeCopy(Game.level.squares[166][83], null);
		Templates.TREE.makeCopy(Game.level.squares[166][97], null);
		Templates.TREE.makeCopy(Game.level.squares[167][13], null);
		Templates.TREE.makeCopy(Game.level.squares[167][21], null);
		Templates.TREE.makeCopy(Game.level.squares[167][24], null);
		Templates.TREE.makeCopy(Game.level.squares[167][30], null);
		Templates.TREE.makeCopy(Game.level.squares[167][31], null);
		Templates.TREE.makeCopy(Game.level.squares[167][33], null);
		Templates.TREE.makeCopy(Game.level.squares[167][36], null);
		Templates.TREE.makeCopy(Game.level.squares[167][39], null);
		Templates.TREE.makeCopy(Game.level.squares[167][40], null);
		Templates.TREE.makeCopy(Game.level.squares[167][43], null);
		Templates.TREE.makeCopy(Game.level.squares[167][44], null);
		Templates.TREE.makeCopy(Game.level.squares[167][45], null);
		Templates.TREE.makeCopy(Game.level.squares[167][46], null);
		Templates.TREE.makeCopy(Game.level.squares[167][47], null);
		Templates.TREE.makeCopy(Game.level.squares[167][48], null);
		Templates.TREE.makeCopy(Game.level.squares[167][49], null);
		Templates.TREE.makeCopy(Game.level.squares[167][50], null);
		Templates.TREE.makeCopy(Game.level.squares[167][52], null);
		Templates.TREE.makeCopy(Game.level.squares[167][55], null);
		Templates.TREE.makeCopy(Game.level.squares[167][63], null);
		Templates.TREE.makeCopy(Game.level.squares[167][64], null);
		Templates.TREE.makeCopy(Game.level.squares[167][65], null);
		Templates.TREE.makeCopy(Game.level.squares[167][66], null);
		Templates.TREE.makeCopy(Game.level.squares[167][73], null);
		Templates.TREE.makeCopy(Game.level.squares[168][14], null);
		Templates.TREE.makeCopy(Game.level.squares[168][18], null);
		Templates.TREE.makeCopy(Game.level.squares[168][23], null);
		Templates.TREE.makeCopy(Game.level.squares[168][28], null);
		Templates.TREE.makeCopy(Game.level.squares[168][35], null);
		Templates.TREE.makeCopy(Game.level.squares[168][37], null);
		Templates.TREE.makeCopy(Game.level.squares[168][40], null);
		Templates.TREE.makeCopy(Game.level.squares[168][45], null);
		Templates.TREE.makeCopy(Game.level.squares[168][46], null);
		Templates.TREE.makeCopy(Game.level.squares[168][49], null);
		Templates.TREE.makeCopy(Game.level.squares[168][50], null);
		Templates.TREE.makeCopy(Game.level.squares[168][53], null);
		Templates.TREE.makeCopy(Game.level.squares[168][58], null);
		Templates.TREE.makeCopy(Game.level.squares[168][6], null);
		Templates.TREE.makeCopy(Game.level.squares[168][60], null);
		Templates.TREE.makeCopy(Game.level.squares[168][61], null);
		Templates.TREE.makeCopy(Game.level.squares[168][62], null);
		Templates.TREE.makeCopy(Game.level.squares[168][64], null);
		Templates.TREE.makeCopy(Game.level.squares[168][86], null);
		Templates.TREE.makeCopy(Game.level.squares[168][87], null);
		Templates.TREE.makeCopy(Game.level.squares[169][17], null);
		Templates.TREE.makeCopy(Game.level.squares[169][21], null);
		Templates.TREE.makeCopy(Game.level.squares[169][24], null);
		Templates.TREE.makeCopy(Game.level.squares[169][31], null);
		Templates.TREE.makeCopy(Game.level.squares[169][33], null);
		Templates.TREE.makeCopy(Game.level.squares[169][34], null);
		Templates.TREE.makeCopy(Game.level.squares[169][42], null);
		Templates.TREE.makeCopy(Game.level.squares[169][46], null);
		Templates.TREE.makeCopy(Game.level.squares[169][5], null);
		Templates.TREE.makeCopy(Game.level.squares[169][51], null);
		Templates.TREE.makeCopy(Game.level.squares[169][52], null);
		Templates.TREE.makeCopy(Game.level.squares[169][55], null);
		Templates.TREE.makeCopy(Game.level.squares[169][61], null);
		Templates.TREE.makeCopy(Game.level.squares[169][62], null);
		Templates.TREE.makeCopy(Game.level.squares[169][64], null);
		Templates.TREE.makeCopy(Game.level.squares[169][65], null);
		Templates.TREE.makeCopy(Game.level.squares[169][66], null);
		Templates.TREE.makeCopy(Game.level.squares[169][72], null);
		Templates.TREE.makeCopy(Game.level.squares[169][81], null);
		Templates.TREE.makeCopy(Game.level.squares[169][84], null);
		Templates.TREE.makeCopy(Game.level.squares[169][90], null);
		Templates.TREE.makeCopy(Game.level.squares[170][15], null);
		Templates.TREE.makeCopy(Game.level.squares[170][17], null);
		Templates.TREE.makeCopy(Game.level.squares[170][29], null);
		Templates.TREE.makeCopy(Game.level.squares[170][32], null);
		Templates.TREE.makeCopy(Game.level.squares[170][33], null);
		Templates.TREE.makeCopy(Game.level.squares[170][37], null);
		Templates.TREE.makeCopy(Game.level.squares[170][39], null);
		Templates.TREE.makeCopy(Game.level.squares[170][41], null);
		Templates.TREE.makeCopy(Game.level.squares[170][42], null);
		Templates.TREE.makeCopy(Game.level.squares[170][49], null);
		Templates.TREE.makeCopy(Game.level.squares[170][50], null);
		Templates.TREE.makeCopy(Game.level.squares[170][51], null);
		Templates.TREE.makeCopy(Game.level.squares[170][52], null);
		Templates.TREE.makeCopy(Game.level.squares[170][53], null);
		Templates.TREE.makeCopy(Game.level.squares[170][54], null);
		Templates.TREE.makeCopy(Game.level.squares[170][58], null);
		Templates.TREE.makeCopy(Game.level.squares[170][63], null);
		Templates.TREE.makeCopy(Game.level.squares[170][64], null);
		Templates.TREE.makeCopy(Game.level.squares[170][66], null);
		Templates.TREE.makeCopy(Game.level.squares[170][75], null);
		Templates.TREE.makeCopy(Game.level.squares[170][81], null);
		Templates.TREE.makeCopy(Game.level.squares[170][83], null);
		Templates.TREE.makeCopy(Game.level.squares[171][14], null);
		Templates.TREE.makeCopy(Game.level.squares[171][32], null);
		Templates.TREE.makeCopy(Game.level.squares[171][41], null);
		Templates.TREE.makeCopy(Game.level.squares[171][45], null);
		Templates.TREE.makeCopy(Game.level.squares[171][46], null);
		Templates.TREE.makeCopy(Game.level.squares[171][47], null);
		Templates.TREE.makeCopy(Game.level.squares[171][49], null);
		Templates.TREE.makeCopy(Game.level.squares[171][50], null);
		Templates.TREE.makeCopy(Game.level.squares[171][54], null);
		Templates.TREE.makeCopy(Game.level.squares[171][60], null);
		Templates.TREE.makeCopy(Game.level.squares[171][64], null);
		Templates.TREE.makeCopy(Game.level.squares[171][66], null);
		Templates.TREE.makeCopy(Game.level.squares[171][8], null);
		Templates.TREE.makeCopy(Game.level.squares[171][91], null);
		Templates.TREE.makeCopy(Game.level.squares[172][22], null);
		Templates.TREE.makeCopy(Game.level.squares[172][28], null);
		Templates.TREE.makeCopy(Game.level.squares[172][36], null);
		Templates.TREE.makeCopy(Game.level.squares[172][37], null);
		Templates.TREE.makeCopy(Game.level.squares[172][39], null);
		Templates.TREE.makeCopy(Game.level.squares[172][44], null);
		Templates.TREE.makeCopy(Game.level.squares[172][46], null);
		Templates.TREE.makeCopy(Game.level.squares[172][49], null);
		Templates.TREE.makeCopy(Game.level.squares[172][54], null);
		Templates.TREE.makeCopy(Game.level.squares[172][55], null);
		Templates.TREE.makeCopy(Game.level.squares[172][56], null);
		Templates.TREE.makeCopy(Game.level.squares[172][62], null);
		Templates.TREE.makeCopy(Game.level.squares[172][64], null);
		Templates.TREE.makeCopy(Game.level.squares[172][67], null);
		Templates.TREE.makeCopy(Game.level.squares[172][71], null);
		Templates.TREE.makeCopy(Game.level.squares[172][73], null);
		Templates.TREE.makeCopy(Game.level.squares[172][75], null);
		Templates.TREE.makeCopy(Game.level.squares[172][82], null);
		Templates.TREE.makeCopy(Game.level.squares[172][87], null);
		Templates.TREE.makeCopy(Game.level.squares[172][9], null);
		Templates.TREE.makeCopy(Game.level.squares[173][21], null);
		Templates.TREE.makeCopy(Game.level.squares[173][25], null);
		Templates.TREE.makeCopy(Game.level.squares[173][29], null);
		Templates.TREE.makeCopy(Game.level.squares[173][37], null);
		Templates.TREE.makeCopy(Game.level.squares[173][40], null);
		Templates.TREE.makeCopy(Game.level.squares[173][45], null);
		Templates.TREE.makeCopy(Game.level.squares[173][50], null);
		Templates.TREE.makeCopy(Game.level.squares[173][51], null);
		Templates.TREE.makeCopy(Game.level.squares[173][53], null);
		Templates.TREE.makeCopy(Game.level.squares[173][54], null);
		Templates.TREE.makeCopy(Game.level.squares[173][55], null);
		Templates.TREE.makeCopy(Game.level.squares[173][59], null);
		Templates.TREE.makeCopy(Game.level.squares[173][61], null);
		Templates.TREE.makeCopy(Game.level.squares[173][72], null);
		Templates.TREE.makeCopy(Game.level.squares[173][79], null);
		Templates.TREE.makeCopy(Game.level.squares[173][81], null);
		Templates.TREE.makeCopy(Game.level.squares[173][85], null);
		Templates.TREE.makeCopy(Game.level.squares[174][14], null);
		Templates.TREE.makeCopy(Game.level.squares[174][22], null);
		Templates.TREE.makeCopy(Game.level.squares[174][24], null);
		Templates.TREE.makeCopy(Game.level.squares[174][26], null);
		Templates.TREE.makeCopy(Game.level.squares[174][31], null);
		Templates.TREE.makeCopy(Game.level.squares[174][32], null);
		Templates.TREE.makeCopy(Game.level.squares[174][37], null);
		Templates.TREE.makeCopy(Game.level.squares[174][38], null);
		Templates.TREE.makeCopy(Game.level.squares[174][40], null);
		Templates.TREE.makeCopy(Game.level.squares[174][42], null);
		Templates.TREE.makeCopy(Game.level.squares[174][45], null);
		Templates.TREE.makeCopy(Game.level.squares[174][46], null);
		Templates.TREE.makeCopy(Game.level.squares[174][47], null);
		Templates.TREE.makeCopy(Game.level.squares[174][48], null);
		Templates.TREE.makeCopy(Game.level.squares[174][49], null);
		Templates.TREE.makeCopy(Game.level.squares[174][50], null);
		Templates.TREE.makeCopy(Game.level.squares[174][51], null);
		Templates.TREE.makeCopy(Game.level.squares[174][53], null);
		Templates.TREE.makeCopy(Game.level.squares[174][55], null);
		Templates.TREE.makeCopy(Game.level.squares[174][57], null);
		Templates.TREE.makeCopy(Game.level.squares[174][58], null);
		Templates.TREE.makeCopy(Game.level.squares[174][62], null);
		Templates.TREE.makeCopy(Game.level.squares[174][64], null);
		Templates.TREE.makeCopy(Game.level.squares[174][66], null);
		Templates.TREE.makeCopy(Game.level.squares[174][70], null);
		Templates.TREE.makeCopy(Game.level.squares[174][72], null);
		Templates.TREE.makeCopy(Game.level.squares[174][74], null);
		Templates.TREE.makeCopy(Game.level.squares[174][75], null);
		Templates.TREE.makeCopy(Game.level.squares[174][76], null);
		Templates.TREE.makeCopy(Game.level.squares[174][81], null);
		Templates.TREE.makeCopy(Game.level.squares[174][91], null);
		Templates.TREE.makeCopy(Game.level.squares[175][33], null);
		Templates.TREE.makeCopy(Game.level.squares[175][34], null);
		Templates.TREE.makeCopy(Game.level.squares[175][41], null);
		Templates.TREE.makeCopy(Game.level.squares[175][43], null);
		Templates.TREE.makeCopy(Game.level.squares[175][44], null);
		Templates.TREE.makeCopy(Game.level.squares[175][45], null);
		Templates.TREE.makeCopy(Game.level.squares[175][53], null);
		Templates.TREE.makeCopy(Game.level.squares[175][54], null);
		Templates.TREE.makeCopy(Game.level.squares[175][59], null);
		Templates.TREE.makeCopy(Game.level.squares[175][62], null);
		Templates.TREE.makeCopy(Game.level.squares[175][64], null);
		Templates.TREE.makeCopy(Game.level.squares[175][67], null);
		Templates.TREE.makeCopy(Game.level.squares[175][68], null);
		Templates.TREE.makeCopy(Game.level.squares[175][73], null);
		Templates.TREE.makeCopy(Game.level.squares[176][30], null);
		Templates.TREE.makeCopy(Game.level.squares[176][32], null);
		Templates.TREE.makeCopy(Game.level.squares[176][38], null);
		Templates.TREE.makeCopy(Game.level.squares[176][43], null);
		Templates.TREE.makeCopy(Game.level.squares[176][45], null);
		Templates.TREE.makeCopy(Game.level.squares[176][55], null);
		Templates.TREE.makeCopy(Game.level.squares[176][57], null);
		Templates.TREE.makeCopy(Game.level.squares[176][61], null);
		Templates.TREE.makeCopy(Game.level.squares[176][62], null);
		Templates.TREE.makeCopy(Game.level.squares[176][67], null);
		Templates.TREE.makeCopy(Game.level.squares[176][68], null);
		Templates.TREE.makeCopy(Game.level.squares[176][70], null);
		Templates.TREE.makeCopy(Game.level.squares[176][72], null);
		Templates.TREE.makeCopy(Game.level.squares[176][82], null);
		Templates.TREE.makeCopy(Game.level.squares[176][86], null);
		Templates.TREE.makeCopy(Game.level.squares[176][91], null);
		Templates.TREE.makeCopy(Game.level.squares[177][10], null);
		Templates.TREE.makeCopy(Game.level.squares[177][24], null);
		Templates.TREE.makeCopy(Game.level.squares[177][27], null);
		Templates.TREE.makeCopy(Game.level.squares[177][34], null);
		Templates.TREE.makeCopy(Game.level.squares[177][36], null);
		Templates.TREE.makeCopy(Game.level.squares[177][38], null);
		Templates.TREE.makeCopy(Game.level.squares[177][42], null);
		Templates.TREE.makeCopy(Game.level.squares[177][43], null);
		Templates.TREE.makeCopy(Game.level.squares[177][44], null);
		Templates.TREE.makeCopy(Game.level.squares[177][53], null);
		Templates.TREE.makeCopy(Game.level.squares[177][58], null);
		Templates.TREE.makeCopy(Game.level.squares[177][64], null);
		Templates.TREE.makeCopy(Game.level.squares[177][66], null);
		Templates.TREE.makeCopy(Game.level.squares[177][67], null);
		Templates.TREE.makeCopy(Game.level.squares[177][68], null);
		Templates.TREE.makeCopy(Game.level.squares[177][73], null);
		Templates.TREE.makeCopy(Game.level.squares[177][74], null);
		Templates.TREE.makeCopy(Game.level.squares[177][81], null);
		Templates.TREE.makeCopy(Game.level.squares[177][84], null);
		Templates.TREE.makeCopy(Game.level.squares[177][86], null);
		Templates.TREE.makeCopy(Game.level.squares[177][9], null);
		Templates.TREE.makeCopy(Game.level.squares[178][14], null);
		Templates.TREE.makeCopy(Game.level.squares[178][15], null);
		Templates.TREE.makeCopy(Game.level.squares[178][21], null);
		Templates.TREE.makeCopy(Game.level.squares[178][29], null);
		Templates.TREE.makeCopy(Game.level.squares[178][31], null);
		Templates.TREE.makeCopy(Game.level.squares[178][37], null);
		Templates.TREE.makeCopy(Game.level.squares[178][39], null);
		Templates.TREE.makeCopy(Game.level.squares[178][41], null);
		Templates.TREE.makeCopy(Game.level.squares[178][43], null);
		Templates.TREE.makeCopy(Game.level.squares[178][44], null);
		Templates.TREE.makeCopy(Game.level.squares[178][58], null);
		Templates.TREE.makeCopy(Game.level.squares[178][64], null);
		Templates.TREE.makeCopy(Game.level.squares[178][65], null);
		Templates.TREE.makeCopy(Game.level.squares[178][70], null);
		Templates.TREE.makeCopy(Game.level.squares[178][74], null);
		Templates.TREE.makeCopy(Game.level.squares[178][78], null);
		Templates.TREE.makeCopy(Game.level.squares[178][8], null);
		Templates.TREE.makeCopy(Game.level.squares[178][85], null);
		Templates.TREE.makeCopy(Game.level.squares[178][86], null);
		Templates.TREE.makeCopy(Game.level.squares[178][9], null);
		Templates.TREE.makeCopy(Game.level.squares[178][97], null);
		Templates.TREE.makeCopy(Game.level.squares[179][10], null);
		Templates.TREE.makeCopy(Game.level.squares[179][20], null);
		Templates.TREE.makeCopy(Game.level.squares[179][29], null);
		Templates.TREE.makeCopy(Game.level.squares[179][36], null);
		Templates.TREE.makeCopy(Game.level.squares[179][38], null);
		Templates.TREE.makeCopy(Game.level.squares[179][42], null);
		Templates.TREE.makeCopy(Game.level.squares[179][43], null);
		Templates.TREE.makeCopy(Game.level.squares[179][54], null);
		Templates.TREE.makeCopy(Game.level.squares[179][55], null);
		Templates.TREE.makeCopy(Game.level.squares[179][57], null);
		Templates.TREE.makeCopy(Game.level.squares[179][59], null);
		Templates.TREE.makeCopy(Game.level.squares[179][60], null);
		Templates.TREE.makeCopy(Game.level.squares[179][61], null);
		Templates.TREE.makeCopy(Game.level.squares[179][62], null);
		Templates.TREE.makeCopy(Game.level.squares[179][63], null);
		Templates.TREE.makeCopy(Game.level.squares[179][64], null);
		Templates.TREE.makeCopy(Game.level.squares[179][67], null);
		Templates.TREE.makeCopy(Game.level.squares[179][72], null);
		Templates.TREE.makeCopy(Game.level.squares[179][73], null);
		Templates.TREE.makeCopy(Game.level.squares[179][77], null);
		Templates.TREE.makeCopy(Game.level.squares[179][78], null);
		Templates.TREE.makeCopy(Game.level.squares[179][92], null);
		Templates.TREE.makeCopy(Game.level.squares[179][95], null);
		Templates.TREE.makeCopy(Game.level.squares[179][96], null);
		Templates.TREE.makeCopy(Game.level.squares[180][21], null);
		Templates.TREE.makeCopy(Game.level.squares[180][23], null);
		Templates.TREE.makeCopy(Game.level.squares[180][30], null);
		Templates.TREE.makeCopy(Game.level.squares[180][37], null);
		Templates.TREE.makeCopy(Game.level.squares[180][38], null);
		Templates.TREE.makeCopy(Game.level.squares[180][41], null);
		Templates.TREE.makeCopy(Game.level.squares[180][44], null);
		Templates.TREE.makeCopy(Game.level.squares[180][47], null);
		Templates.TREE.makeCopy(Game.level.squares[180][56], null);
		Templates.TREE.makeCopy(Game.level.squares[180][58], null);
		Templates.TREE.makeCopy(Game.level.squares[180][65], null);
		Templates.TREE.makeCopy(Game.level.squares[180][72], null);
		Templates.TREE.makeCopy(Game.level.squares[180][73], null);
		Templates.TREE.makeCopy(Game.level.squares[180][96], null);
		Templates.TREE.makeCopy(Game.level.squares[181][14], null);
		Templates.TREE.makeCopy(Game.level.squares[181][18], null);
		Templates.TREE.makeCopy(Game.level.squares[181][21], null);
		Templates.TREE.makeCopy(Game.level.squares[181][30], null);
		Templates.TREE.makeCopy(Game.level.squares[181][33], null);
		Templates.TREE.makeCopy(Game.level.squares[181][40], null);
		Templates.TREE.makeCopy(Game.level.squares[181][49], null);
		Templates.TREE.makeCopy(Game.level.squares[181][50], null);
		Templates.TREE.makeCopy(Game.level.squares[181][51], null);
		Templates.TREE.makeCopy(Game.level.squares[181][54], null);
		Templates.TREE.makeCopy(Game.level.squares[181][55], null);
		Templates.TREE.makeCopy(Game.level.squares[181][58], null);
		Templates.TREE.makeCopy(Game.level.squares[181][67], null);
		Templates.TREE.makeCopy(Game.level.squares[181][70], null);
		Templates.TREE.makeCopy(Game.level.squares[181][89], null);
		Templates.TREE.makeCopy(Game.level.squares[182][17], null);
		Templates.TREE.makeCopy(Game.level.squares[182][29], null);
		Templates.TREE.makeCopy(Game.level.squares[182][33], null);
		Templates.TREE.makeCopy(Game.level.squares[182][36], null);
		Templates.TREE.makeCopy(Game.level.squares[182][37], null);
		Templates.TREE.makeCopy(Game.level.squares[182][46], null);
		Templates.TREE.makeCopy(Game.level.squares[182][48], null);
		Templates.TREE.makeCopy(Game.level.squares[182][49], null);
		Templates.TREE.makeCopy(Game.level.squares[182][52], null);
		Templates.TREE.makeCopy(Game.level.squares[182][58], null);
		Templates.TREE.makeCopy(Game.level.squares[182][63], null);
		Templates.TREE.makeCopy(Game.level.squares[182][68], null);
		Templates.TREE.makeCopy(Game.level.squares[182][71], null);
		Templates.TREE.makeCopy(Game.level.squares[182][90], null);
		Templates.TREE.makeCopy(Game.level.squares[182][95], null);
		Templates.TREE.makeCopy(Game.level.squares[183][11], null);
		Templates.TREE.makeCopy(Game.level.squares[183][13], null);
		Templates.TREE.makeCopy(Game.level.squares[183][15], null);
		Templates.TREE.makeCopy(Game.level.squares[183][28], null);
		Templates.TREE.makeCopy(Game.level.squares[183][3], null);
		Templates.TREE.makeCopy(Game.level.squares[183][32], null);
		Templates.TREE.makeCopy(Game.level.squares[183][39], null);
		Templates.TREE.makeCopy(Game.level.squares[183][43], null);
		Templates.TREE.makeCopy(Game.level.squares[183][46], null);
		Templates.TREE.makeCopy(Game.level.squares[183][54], null);
		Templates.TREE.makeCopy(Game.level.squares[183][61], null);
		Templates.TREE.makeCopy(Game.level.squares[183][68], null);
		Templates.TREE.makeCopy(Game.level.squares[183][69], null);
		Templates.TREE.makeCopy(Game.level.squares[183][72], null);
		Templates.TREE.makeCopy(Game.level.squares[183][85], null);
		Templates.TREE.makeCopy(Game.level.squares[184][24], null);
		Templates.TREE.makeCopy(Game.level.squares[184][28], null);
		Templates.TREE.makeCopy(Game.level.squares[184][36], null);
		Templates.TREE.makeCopy(Game.level.squares[184][39], null);
		Templates.TREE.makeCopy(Game.level.squares[184][41], null);
		Templates.TREE.makeCopy(Game.level.squares[184][42], null);
		Templates.TREE.makeCopy(Game.level.squares[184][50], null);
		Templates.TREE.makeCopy(Game.level.squares[184][51], null);
		Templates.TREE.makeCopy(Game.level.squares[184][52], null);
		Templates.TREE.makeCopy(Game.level.squares[184][54], null);
		Templates.TREE.makeCopy(Game.level.squares[184][55], null);
		Templates.TREE.makeCopy(Game.level.squares[184][64], null);
		Templates.TREE.makeCopy(Game.level.squares[184][65], null);
		Templates.TREE.makeCopy(Game.level.squares[184][66], null);
		Templates.TREE.makeCopy(Game.level.squares[184][82], null);
		Templates.TREE.makeCopy(Game.level.squares[184][86], null);
		Templates.TREE.makeCopy(Game.level.squares[184][92], null);
		Templates.TREE.makeCopy(Game.level.squares[185][19], null);
		Templates.TREE.makeCopy(Game.level.squares[185][24], null);
		Templates.TREE.makeCopy(Game.level.squares[185][34], null);
		Templates.TREE.makeCopy(Game.level.squares[185][37], null);
		Templates.TREE.makeCopy(Game.level.squares[185][38], null);
		Templates.TREE.makeCopy(Game.level.squares[185][42], null);
		Templates.TREE.makeCopy(Game.level.squares[185][43], null);
		Templates.TREE.makeCopy(Game.level.squares[185][51], null);
		Templates.TREE.makeCopy(Game.level.squares[185][53], null);
		Templates.TREE.makeCopy(Game.level.squares[185][54], null);
		Templates.TREE.makeCopy(Game.level.squares[185][6], null);
		Templates.TREE.makeCopy(Game.level.squares[185][68], null);
		Templates.TREE.makeCopy(Game.level.squares[185][70], null);
		Templates.TREE.makeCopy(Game.level.squares[185][76], null);
		Templates.TREE.makeCopy(Game.level.squares[185][93], null);
		Templates.TREE.makeCopy(Game.level.squares[186][22], null);
		Templates.TREE.makeCopy(Game.level.squares[186][32], null);
		Templates.TREE.makeCopy(Game.level.squares[186][39], null);
		Templates.TREE.makeCopy(Game.level.squares[186][40], null);
		Templates.TREE.makeCopy(Game.level.squares[186][42], null);
		Templates.TREE.makeCopy(Game.level.squares[186][45], null);
		Templates.TREE.makeCopy(Game.level.squares[186][49], null);
		Templates.TREE.makeCopy(Game.level.squares[186][50], null);
		Templates.TREE.makeCopy(Game.level.squares[186][52], null);
		Templates.TREE.makeCopy(Game.level.squares[186][59], null);
		Templates.TREE.makeCopy(Game.level.squares[186][60], null);
		Templates.TREE.makeCopy(Game.level.squares[186][65], null);
		Templates.TREE.makeCopy(Game.level.squares[186][68], null);
		Templates.TREE.makeCopy(Game.level.squares[186][83], null);
		Templates.TREE.makeCopy(Game.level.squares[187][19], null);
		Templates.TREE.makeCopy(Game.level.squares[187][24], null);
		Templates.TREE.makeCopy(Game.level.squares[187][32], null);
		Templates.TREE.makeCopy(Game.level.squares[187][36], null);
		Templates.TREE.makeCopy(Game.level.squares[187][38], null);
		Templates.TREE.makeCopy(Game.level.squares[187][39], null);
		Templates.TREE.makeCopy(Game.level.squares[187][44], null);
		Templates.TREE.makeCopy(Game.level.squares[187][46], null);
		Templates.TREE.makeCopy(Game.level.squares[187][49], null);
		Templates.TREE.makeCopy(Game.level.squares[187][52], null);
		Templates.TREE.makeCopy(Game.level.squares[187][57], null);
		Templates.TREE.makeCopy(Game.level.squares[187][6], null);
		Templates.TREE.makeCopy(Game.level.squares[187][60], null);
		Templates.TREE.makeCopy(Game.level.squares[187][67], null);
		Templates.TREE.makeCopy(Game.level.squares[187][70], null);
		Templates.TREE.makeCopy(Game.level.squares[187][86], null);
		Templates.TREE.makeCopy(Game.level.squares[187][89], null);
		Templates.TREE.makeCopy(Game.level.squares[188][26], null);
		Templates.TREE.makeCopy(Game.level.squares[188][27], null);
		Templates.TREE.makeCopy(Game.level.squares[188][30], null);
		Templates.TREE.makeCopy(Game.level.squares[188][33], null);
		Templates.TREE.makeCopy(Game.level.squares[188][39], null);
		Templates.TREE.makeCopy(Game.level.squares[188][43], null);
		Templates.TREE.makeCopy(Game.level.squares[188][57], null);
		Templates.TREE.makeCopy(Game.level.squares[188][59], null);
		Templates.TREE.makeCopy(Game.level.squares[188][61], null);
		Templates.TREE.makeCopy(Game.level.squares[188][74], null);
		Templates.TREE.makeCopy(Game.level.squares[188][85], null);
		Templates.TREE.makeCopy(Game.level.squares[188][86], null);
		Templates.TREE.makeCopy(Game.level.squares[189][27], null);
		Templates.TREE.makeCopy(Game.level.squares[189][32], null);
		Templates.TREE.makeCopy(Game.level.squares[189][34], null);
		Templates.TREE.makeCopy(Game.level.squares[189][41], null);
		Templates.TREE.makeCopy(Game.level.squares[189][43], null);
		Templates.TREE.makeCopy(Game.level.squares[189][47], null);
		Templates.TREE.makeCopy(Game.level.squares[189][48], null);
		Templates.TREE.makeCopy(Game.level.squares[189][51], null);
		Templates.TREE.makeCopy(Game.level.squares[189][60], null);
		Templates.TREE.makeCopy(Game.level.squares[189][62], null);
		Templates.TREE.makeCopy(Game.level.squares[189][63], null);
		Templates.TREE.makeCopy(Game.level.squares[189][67], null);
		Templates.TREE.makeCopy(Game.level.squares[189][70], null);
		Templates.TREE.makeCopy(Game.level.squares[189][74], null);
		Templates.TREE.makeCopy(Game.level.squares[189][79], null);
		Templates.TREE.makeCopy(Game.level.squares[189][83], null);
		Templates.TREE.makeCopy(Game.level.squares[189][86], null);
		Templates.TREE.makeCopy(Game.level.squares[190][14], null);
		Templates.TREE.makeCopy(Game.level.squares[190][38], null);
		Templates.TREE.makeCopy(Game.level.squares[190][41], null);
		Templates.TREE.makeCopy(Game.level.squares[190][44], null);
		Templates.TREE.makeCopy(Game.level.squares[190][46], null);
		Templates.TREE.makeCopy(Game.level.squares[190][49], null);
		Templates.TREE.makeCopy(Game.level.squares[190][51], null);
		Templates.TREE.makeCopy(Game.level.squares[190][53], null);
		Templates.TREE.makeCopy(Game.level.squares[190][58], null);
		Templates.TREE.makeCopy(Game.level.squares[190][60], null);
		Templates.TREE.makeCopy(Game.level.squares[190][66], null);
		Templates.TREE.makeCopy(Game.level.squares[190][74], null);
		Templates.TREE.makeCopy(Game.level.squares[190][89], null);
		Templates.TREE.makeCopy(Game.level.squares[191][31], null);
		Templates.TREE.makeCopy(Game.level.squares[191][32], null);
		Templates.TREE.makeCopy(Game.level.squares[191][37], null);
		Templates.TREE.makeCopy(Game.level.squares[191][45], null);
		Templates.TREE.makeCopy(Game.level.squares[191][54], null);
		Templates.TREE.makeCopy(Game.level.squares[191][57], null);
		Templates.TREE.makeCopy(Game.level.squares[191][61], null);
		Templates.TREE.makeCopy(Game.level.squares[191][63], null);
		Templates.TREE.makeCopy(Game.level.squares[191][66], null);
		Templates.TREE.makeCopy(Game.level.squares[191][68], null);
		Templates.TREE.makeCopy(Game.level.squares[191][69], null);
		Templates.TREE.makeCopy(Game.level.squares[191][87], null);
		Templates.TREE.makeCopy(Game.level.squares[192][12], null);
		Templates.TREE.makeCopy(Game.level.squares[192][36], null);
		Templates.TREE.makeCopy(Game.level.squares[192][40], null);
		Templates.TREE.makeCopy(Game.level.squares[192][41], null);
		Templates.TREE.makeCopy(Game.level.squares[192][50], null);
		Templates.TREE.makeCopy(Game.level.squares[192][54], null);
		Templates.TREE.makeCopy(Game.level.squares[192][66], null);
		Templates.TREE.makeCopy(Game.level.squares[192][94], null);
		Templates.TREE.makeCopy(Game.level.squares[193][27], null);
		Templates.TREE.makeCopy(Game.level.squares[193][36], null);
		Templates.TREE.makeCopy(Game.level.squares[193][46], null);
		Templates.TREE.makeCopy(Game.level.squares[193][48], null);
		Templates.TREE.makeCopy(Game.level.squares[193][49], null);
		Templates.TREE.makeCopy(Game.level.squares[193][58], null);
		Templates.TREE.makeCopy(Game.level.squares[193][65], null);
		Templates.TREE.makeCopy(Game.level.squares[193][74], null);
		Templates.TREE.makeCopy(Game.level.squares[194][30], null);
		Templates.TREE.makeCopy(Game.level.squares[194][39], null);
		Templates.TREE.makeCopy(Game.level.squares[194][41], null);
		Templates.TREE.makeCopy(Game.level.squares[194][43], null);
		Templates.TREE.makeCopy(Game.level.squares[194][46], null);
		Templates.TREE.makeCopy(Game.level.squares[194][47], null);
		Templates.TREE.makeCopy(Game.level.squares[194][49], null);
		Templates.TREE.makeCopy(Game.level.squares[194][57], null);
		Templates.TREE.makeCopy(Game.level.squares[194][65], null);
		Templates.TREE.makeCopy(Game.level.squares[194][83], null);
		Templates.TREE.makeCopy(Game.level.squares[195][22], null);
		Templates.TREE.makeCopy(Game.level.squares[195][29], null);
		Templates.TREE.makeCopy(Game.level.squares[195][30], null);
		Templates.TREE.makeCopy(Game.level.squares[195][34], null);
		Templates.TREE.makeCopy(Game.level.squares[195][35], null);
		Templates.TREE.makeCopy(Game.level.squares[195][39], null);
		Templates.TREE.makeCopy(Game.level.squares[195][44], null);
		Templates.TREE.makeCopy(Game.level.squares[195][49], null);
		Templates.TREE.makeCopy(Game.level.squares[195][55], null);
		Templates.TREE.makeCopy(Game.level.squares[196][14], null);
		Templates.TREE.makeCopy(Game.level.squares[196][17], null);
		Templates.TREE.makeCopy(Game.level.squares[196][25], null);
		Templates.TREE.makeCopy(Game.level.squares[196][27], null);
		Templates.TREE.makeCopy(Game.level.squares[196][34], null);
		Templates.TREE.makeCopy(Game.level.squares[196][36], null);
		Templates.TREE.makeCopy(Game.level.squares[196][37], null);
		Templates.TREE.makeCopy(Game.level.squares[196][40], null);
		Templates.TREE.makeCopy(Game.level.squares[196][52], null);
		Templates.TREE.makeCopy(Game.level.squares[196][53], null);
		Templates.TREE.makeCopy(Game.level.squares[196][54], null);
		Templates.TREE.makeCopy(Game.level.squares[196][62], null);
		Templates.TREE.makeCopy(Game.level.squares[196][71], null);
		Templates.TREE.makeCopy(Game.level.squares[197][13], null);
		Templates.TREE.makeCopy(Game.level.squares[197][35], null);
		Templates.TREE.makeCopy(Game.level.squares[197][37], null);
		Templates.TREE.makeCopy(Game.level.squares[197][38], null);
		Templates.TREE.makeCopy(Game.level.squares[197][54], null);
		Templates.TREE.makeCopy(Game.level.squares[197][58], null);
		Templates.TREE.makeCopy(Game.level.squares[197][59], null);
		Templates.TREE.makeCopy(Game.level.squares[198][27], null);
		Templates.TREE.makeCopy(Game.level.squares[198][31], null);
		Templates.TREE.makeCopy(Game.level.squares[198][32], null);
		Templates.TREE.makeCopy(Game.level.squares[198][36], null);
		Templates.TREE.makeCopy(Game.level.squares[198][41], null);
		Templates.TREE.makeCopy(Game.level.squares[198][43], null);
		Templates.TREE.makeCopy(Game.level.squares[198][45], null);
		Templates.TREE.makeCopy(Game.level.squares[198][50], null);
		Templates.TREE.makeCopy(Game.level.squares[198][54], null);
		Templates.TREE.makeCopy(Game.level.squares[199][22], null);
		Templates.TREE.makeCopy(Game.level.squares[199][40], null);
		Templates.TREE.makeCopy(Game.level.squares[199][43], null);
		Templates.TREE.makeCopy(Game.level.squares[199][46], null);
		Templates.TREE.makeCopy(Game.level.squares[199][61], null);
		Templates.TREE.makeCopy(Game.level.squares[199][62], null);
		Templates.TREE.makeCopy(Game.level.squares[200][12], null);
		Templates.TREE.makeCopy(Game.level.squares[200][15], null);
		Templates.TREE.makeCopy(Game.level.squares[200][30], null);
		Templates.TREE.makeCopy(Game.level.squares[200][34], null);
		Templates.TREE.makeCopy(Game.level.squares[200][39], null);
		Templates.TREE.makeCopy(Game.level.squares[200][44], null);
		Templates.TREE.makeCopy(Game.level.squares[200][48], null);
		Templates.TREE.makeCopy(Game.level.squares[200][68], null);
		Templates.TREE.makeCopy(Game.level.squares[200][73], null);
		Templates.TREE.makeCopy(Game.level.squares[201][14], null);
		Templates.TREE.makeCopy(Game.level.squares[201][20], null);
		Templates.TREE.makeCopy(Game.level.squares[201][21], null);
		Templates.TREE.makeCopy(Game.level.squares[201][32], null);
		Templates.TREE.makeCopy(Game.level.squares[201][38], null);
		Templates.TREE.makeCopy(Game.level.squares[201][51], null);
		Templates.TREE.makeCopy(Game.level.squares[201][61], null);
		Templates.TREE.makeCopy(Game.level.squares[201][66], null);
		Templates.TREE.makeCopy(Game.level.squares[201][95], null);
		Templates.TREE.makeCopy(Game.level.squares[202][34], null);
		Templates.TREE.makeCopy(Game.level.squares[202][71], null);
		Templates.TREE.makeCopy(Game.level.squares[202][89], null);
		Templates.TREE.makeCopy(Game.level.squares[202][95], null);
		Templates.TREE.makeCopy(Game.level.squares[203][42], null);
		Templates.TREE.makeCopy(Game.level.squares[203][43], null);
		Templates.TREE.makeCopy(Game.level.squares[203][46], null);
		Templates.TREE.makeCopy(Game.level.squares[203][53], null);
		Templates.TREE.makeCopy(Game.level.squares[203][76], null);
		Templates.TREE.makeCopy(Game.level.squares[203][80], null);
		Templates.TREE.makeCopy(Game.level.squares[204][57], null);
		Templates.TREE.makeCopy(Game.level.squares[204][58], null);
		Templates.TREE.makeCopy(Game.level.squares[204][71], null);
		Templates.TREE.makeCopy(Game.level.squares[205][34], null);
		Templates.TREE.makeCopy(Game.level.squares[205][45], null);
		Templates.TREE.makeCopy(Game.level.squares[205][71], null);
		Templates.TREE.makeCopy(Game.level.squares[206][11], null);
		Templates.TREE.makeCopy(Game.level.squares[206][41], null);
		Templates.TREE.makeCopy(Game.level.squares[206][47], null);
		Templates.TREE.makeCopy(Game.level.squares[206][69], null);
		Templates.TREE.makeCopy(Game.level.squares[207][35], null);
		Templates.TREE.makeCopy(Game.level.squares[208][75], null);

	}

	@Override
	public void update() {
		// Set flags

		// The wolves are dead
		if (wolvesDead == false) {
			wolvesDead = true;
			for (int i = 0; i < wolfPack.size(); i++) {
				if (wolfPack.getMember(i).remainingHealth > 0) {
					wolvesDead = false;
					break;
				}
			}
		}

		// Player has attacked the wolves
		if (playerAttackedWolves == false && wolfPack.hasAttackers()) {
			if (wolfPack.getAttackers().contains(Game.level.player)) {
				playerAttackedWolves = true;
			}
		}

		// The hunters are dead
		if (huntersDead == false) {
			huntersDead = true;
			for (int i = 0; i < hunterPack.size(); i++) {
				if (hunterPack.getMember(i).remainingHealth > 0) {
					huntersDead = false;
					break;
				}
			}
		}

		// Player has attacked the hunters
		if (playerAttackedHunters == false && hunterPack.hasAttackers()) {
			if (hunterPack.getAttackers().contains(Game.level.player)) {
				playerAttackedHunters = true;
			}
		}

		// Hunters and wolves have fought
		if (huntersAndWolvesFought == false && hunterPack.hasAttackers()) {
			for (int j = 0; j < wolfPack.size(); j++) {
				if (hunterPack.getAttackers().contains(wolfPack.getMember(j))) {
					huntersAndWolvesFought = true;
					break;
				}
			}
		}

	}

	@Override
	public boolean update(Actor actor) {
		update();
		if (hunterPack.contains(actor)) {
			return updateHunter(actor);
		} else if (actor == environmentalist) {
			return updateEnvironmentalist(actor);
		} else if (wolfPack.contains(actor)) {
			return true;
		}
		return false;
	}

	private boolean updateHunter(Actor actor) {

		if (huntersReleasedFromQuest) {
			return false;
		}

		if (!readyToGo) {
			actor.activityDescription = ACTIVITY_PLANNING_A_HUNT;
			if (actor == hunterPack.getLeader()) {
				goToHuntPlanningArea(actor);
			}
		} else if (readyToGo && !this.wolvesDead) {

			if (actor == hunterPack.getLeader()) {

				if (actor.straightLineDistanceTo(Game.level.player.squareGameObjectIsOn) > 5
						&& actor.straightLineDistanceTo(superWolf.squareGameObjectIsOn) < Game.level.player
								.straightLineDistanceTo(superWolf.squareGameObjectIsOn)) {
					// Wait for the player
					actor.activityDescription = ACTIVITY_WAITING_FOR_YOU;
				} else {
					actor.activityDescription = ACTIVITY_DESCRIPTION_HUNTING;
					if (superWolf.remainingHealth > 0) {
						if (!AIRoutineUtils.attackTarget(superWolf)) {
							AIRoutineUtils.moveTowardsTargetToAttack(superWolf);
						}
					}
				}
			}
		} else if (this.wolvesDead && this.playerAttackedWolves && !questAcceptedFromHunters) {
			// Wolves were killed by player before accepting the mission
			goToHuntPlanningArea(actor);
		} else if (this.wolvesDead && this.playerAttackedWolves && !readyToGo) {
			// Wolves were killed by player after accepting the mission, but
			// before he told the hunters he's ready to go
			goToHuntPlanningArea(actor);
		} else if (this.wolvesDead && this.playerAttackedWolves) {
			// Wolves were killed after departing for the hunt, and the player
			// helped kill them
			// Talk to them... for some reason
			if (actor == hunterPack.getLeader()) {
				if (actor.straightLineDistanceTo(Game.level.player.squareGameObjectIsOn) < 2) {
					new ActionTalk(actor, Game.level.player).perform();
				} else {
					AIRoutineUtils.moveTowardsTargetToBeAdjacent(Game.level.player);
				}
			}
		} else if (this.wolvesDead && !this.playerAttackedWolves) {
			// Wolves were killed, but player didnt help
			if (actor == hunterPack.getLeader()) {
				if (actor.squareGameObjectIsOn != huntPlanningArea) {
					actor.activityDescription = ACTIVITY_DESCRIPTION_GOING_HOME;
					AIRoutineUtils.moveTowardsTargetSquare(huntPlanningArea);
				} else {
					huntersReleasedFromQuest = true;
				}
			}
		}
		return true;

	}

	public void goToHuntPlanningArea(Actor actor) {
		AIRoutineUtils.moveTowardsTargetSquare(huntPlanningArea);
	}

	private boolean updateEnvironmentalist(Actor actor) {
		if (!questAcceptedFromHunters) {
			actor.activityDescription = ACTIVITY_SPYING;

		} else if (questAcceptedFromHunters && !talkedToEnvironmentalist) {
			actor.activityDescription = ACTIVITY_SAVING_THE_WORLD;

			if (environmentalist.squareGameObjectIsOn != squareBehindLodge) {
				// Move to weapons behind the lodge
				AIRoutineUtils.moveTowardsTargetSquare(squareBehindLodge);
			} else {
				// Pick up weapons behind the lodge
				for (GameObject weaponBehindLodge : weaponsBehindLodge) {
					if (weaponBehindLodge.squareGameObjectIsOn == squareBehindLodge) {
						AIRoutineUtils.pickupTarget(weaponBehindLodge);

						// MAYBE put loot in a chest in the lodge

					}
				}
				// If the player is near, tell them not to kill the wolf and
				// give them the weapons
				if (actor.straightLineDistanceTo(Game.level.player.squareGameObjectIsOn) < 2) {
					new ActionTalk(actor, Game.level.player).perform();
				}
			}
		}
		return true;
	}

	@Override
	public Conversation getConversation(Actor actor) {
		if (hunterPack.contains(actor)) {
			return getConversationForHunter(actor);
		} else if (actor == environmentalist) {
			return getConversationForEnvironmentalist(actor);
		}
		return null;
	}

	public Conversation getConversationForHunter(Actor actor) {
		// Talking to a hunter
		if (!questAcceptedFromHunters) {
			return conversationHuntersJoinTheHunt;
		} else if (!readyToGo) {
			return conversationHuntersReadyToGo;
		} else if (this.wolvesDead && !this.playerAttackedWolves) {
			return conversationHuntersOnlyHuntersGetLoot;
		}
		return null;
	}

	public Conversation getConversationForEnvironmentalist(Actor actor) {
		// Talking to environmentalist
		if (!questAcceptedFromHunters) {
			return conversationEnviromentalistImNotSpying;
		} else if (!talkedToEnvironmentalist) {
			return conversationEnviromentalistSaveTheWolf;
		}
		return null;
	}

	public void setUpConversationReadyToGo() {
		ConversationResponse conversationReponseEnd = new ConversationResponse("Leave", null);

		ConversationPart conversationPartTheresEquipment = new ConversationPart(
				new Object[] {
						"There's spare equipment 'round back, help yourself! Joe runs a shop to the North if you think you need anything else. Let us know when you're ready." },
				new ConversationResponse[] { conversationReponseEnd }, hunterPack.getLeader());

		ConversationPart conversationPartSuitYourself = new ConversationPart(new Object[] { "Suit yourself." },
				new ConversationResponse[] { conversationReponseEnd }, hunterPack.getLeader());

		ConversationResponse conversationResponseNoThanks = new ConversationResponse("No thanks",
				conversationPartSuitYourself);
		ConversationResponse conversationResponseYesPlease = new ConversationResponse("Yes please",
				conversationPartTheresEquipment) {
			@Override
			public void select() {
				super.select();
				// ADD QUEST TO QUEST LOG IF NO IN HARDCORE MODE
				// THIS ALSO COMES WITH A TOAST / POPUP SAYING "QUEST STARTED -
				// PACK HUNTERS"
				questAcceptedFromHunters = true;
			}
		};

		ConversationPart conversationPartWantToComeHunting = new ConversationPart(
				new Object[] { "We're just about to head out on the hunt, an extra man wouldn't go amiss." },
				new ConversationResponse[] { conversationResponseYesPlease, conversationResponseNoThanks },
				hunterPack.getLeader());

		conversationHuntersJoinTheHunt = new Conversation(conversationPartWantToComeHunting);

	}

	private void setUpConversationImNotSpying() {

		// Environmentalist could have emoticon over his head showing his
		// feelings
		// Anime style
		// try it out
		ConversationResponse conversationReponseEndAfterAccepting = new ConversationResponse("Leave", null);
		ConversationPart conversationPartImNotSpying = new ConversationPart(
				new Object[] { "What? NO! I'm not spying! You're spying!" },
				new ConversationResponse[] { conversationReponseEndAfterAccepting }, environmentalist);
		conversationEnviromentalistImNotSpying = new Conversation(conversationPartImNotSpying);
	}

	private void setUpConversationSaveTheWolf() {

		// Should Be
		// 1. Plead
		// 2. Here's your hunting equipment
		// 3. And here, this should help if you choose to do the right thing
		// (give you remove imbument or imbue with fire.

		ConversationResponse conversationReponseEndAfterAccepting = new ConversationResponse("Leave", null) {
			@Override
			public void select() {
				super.select();
				for (GameObject gameObject : weaponsBehindLodge) {
					if (environmentalist.inventory.contains(gameObject)) {
						new ActionGive(environmentalist, Game.level.player, gameObject).perform();
						talkedToEnvironmentalist = true;
					}
				}
			}

		};
		ConversationPart conversationPartSaveTheWolf = new ConversationPart(new Object[] { "Save the wolf!" },
				new ConversationResponse[] { conversationReponseEndAfterAccepting }, environmentalist);
		conversationEnviromentalistSaveTheWolf = new Conversation(conversationPartSaveTheWolf);

	}

	public void setUpConversationJoinTheHunt() {
		ConversationResponse conversationReponseEnd = new ConversationResponse("Leave", null);

		ConversationPart conversationAlrightLetsGo = new ConversationPart(
				new Object[] { "Alright! Let's go bag us a some pelts!" },
				new ConversationResponse[] { conversationReponseEnd }, hunterPack.getLeader());

		ConversationPart conversationPartWellHurryOn = new ConversationPart(new Object[] { "Well hurry on!" },
				new ConversationResponse[] { conversationReponseEnd }, hunterPack.getLeader());

		ConversationResponse conversationResponseNotYet = new ConversationResponse("Not yet",
				conversationPartWellHurryOn);
		ConversationResponse conversationResponseReady = new ConversationResponse("Ready!", conversationAlrightLetsGo) {
			@Override
			public void select() {
				super.select();
				// Update quest log
				// Set enviromentalist to come watch
				// Hunters on the way
				readyToGo = true;
			}
		};

		ConversationPart conversationPartReadyToGo = new ConversationPart(new Object[] { "Ready to go, pal?" },
				new ConversationResponse[] { conversationResponseReady, conversationResponseNotYet },
				hunterPack.getLeader());

		conversationHuntersReadyToGo = new Conversation(conversationPartReadyToGo);

	}

	private void setUpConversationYouDidntHelp() {
		// Really like the "Now fuck off!" bit.
		ConversationResponse conversationReponseEnd = new ConversationResponse("Leave", null);
		ConversationPart conversationPartOnlyHuntersGetLoot = new ConversationPart(
				new Object[] { "Only hunters get loot. Now fuck off!" },
				new ConversationResponse[] { conversationReponseEnd }, hunterPack.getLeader());
		conversationHuntersOnlyHuntersGetLoot = new Conversation(conversationPartOnlyHuntersGetLoot);
	}

}
