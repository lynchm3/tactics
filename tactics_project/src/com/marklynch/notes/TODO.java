package com.marklynch.notes;

public class TODO {

	// GameObject.canShareSquare

	// GameObject.canBePlacedInInventory

	// GameObject.canHoldObjects

	// Still using squareObjectIsOn... but also have attribute
	// InventoryThisItemBelongsTo...

	// put canShareSquare in to path finding

	// Put EDITOR STATE somewhere on screen

	// Add object button in PopupSelectObject?, can copy from
	// ObjectsSettingsWindow

	// Editor.gameObjectClicked shoule be rplaced with squareClicked
	// Level/Game.gameObjectClicked shoule be rplaced with squareClicked
	// In userInputEditor and TWICE in UserInputLevel, look for inventory.size()

	// ADD_ACTOR and ADD_OBJECT need to check shareSquare

	// Should be able to place item on square that has unsharable object, make
	// the object u place sharable, this also applies to moving objects...

	// Show a toast "NO SPACE FOR ACTOR" if you try to add a new actor to a
	// square that has no share sapce

	// The update() method in the windows and popup seem unneccesary, when is
	// update called?

	// Issue, what if an object is sharing a square and u turn off
	// canShareSquare?? (in
	// editor)

	// IDEA: the ability to move objects
	// There's a raid on the way and u can move shit around to protect urself
	// Also... the guys coming for the raid have always existed, so u could have
	// killed them before if u met them in the game

	// OBJECT HOLDER
	// Set it up in the test data
	// Set up every time u create object
	// every time u move object
	// every time u put and object in an inventory

	// concept of "passable" or "can be traversed" for objects...

	// contextual option to "eat" people ur attacking, does damage to them,
	// heals u a bit

	// when u click on stacked game objects u'll just get the first
	// one,Game.squareMouseIsOver.inventory.gameObjects.get(0), time to
	// implement pop up windows

	// Context menu should show some details and the options. Options should
	// include "View full deatils"

	// Right click opens context menu, left click does default (talk to allies,
	// attack hostiles...), the deafult action shows under the mouse when u
	// hover (maybe in the form of icons rather than text?) (maybe thats an
	// option in the options menu - icon, text, icon + text or nothing)

	// Put this in marketing blurb - The contextual options on click will
	// encourage experimentation

	// make inventory

	// drawInventory boolean decides whether the inventory is drawn or not
	// For a chest it won't be drawn, for an empty square it will
	// remove weapons collection that objects have

	// check the make copy methods, i think they might b shit

	// Maybe change objects to "loose objects"
	// And have an inventory for each character that u can view

	// Just have ADD OBJECT, which asks Weapon, GameObject or Explosive Object

	// Select owner by clicking on person.
	// When you click on the person you get asked whether to set them or the
	// faction as the owner... or dont have ownership on a factin lvl... i
	// dunno...

	// Need object, square, and actor templates

	// Need concept of vision for player character and enemy characters

	// Vision should definitely be an attribute that you can upgrade

	// Ability - show NPC vision (you can right click on an NPC and select see
	// vision) Then you can see whether they can see you or not. Good for
	// sneaking, stealing shit.

	// InterfaceSelectionWindow and ClassSelection Window can be merged or
	// subclassed in to eachother

	// location and owner interfaces
	// Object and SQuare implement location
	// Actor and Faction implement owner

	// When adding a new object select whtehter it'll be onject class or
	// exploder class

	// Add a title to the class selection window and instance selection window

	// Slider for editor to only list items within X distance, or those on
	// screen.
	// So for actors for example, you can slid all the way to the right to list
	// all actors, or all the way to the left and it will only list those on
	// screen in the settings menu on the left

	// Junk objects automatically give you money, like in Dishonoured

	// TODO write a cat that will run away from characters that get too close,
	// and will run off the screen after being disturbed too many times :D
	// Little details like this make a good game great

	// I need a style.
	// Side pane for game time :)
	// Lets say 300px and on the left at the moment

	// SpeechParts selector in speechpartscriptevent too

	// When adding an actor to the speechPart, make sure u add in his GUID too
	// :D

	// WHen removing actor from speechpart remember to delete their guid

	// End level event doesn't have text section in attributes pane

	// Add Actor butto in SpeechPart is adding and actor to both speech parts

	// Text for the speech isnt showing up in the attributes window, no idea why

	// DOoooooooooooooooooo new class for holding Text instead of Object[]
	// Will work muuuuuch better
	// TEXT(Objects... objects)

	// public void postLoad() {
	// for (ScriptEvent scriptEvent : scriptEvents) {
	// scriptEvent.postLoad();
	// }
	// for (ScriptTrigger scriptTrigger : scriptTriggers) {
	// scriptTrigger.postLoad();
	// }
	//
	// HERE
	// SPEECHEVENTS.POSTLOAD()
	//
	// }

	// when changing the arraylist in speech event need to alter the guid too.
	// OR have preSave() method!?!

	// Speechpart needs total postLoad()

	// preSave() method... probably... maybe... defo...

	// Snow that falls and lands on the ground (shader? probably not...)

	// Edit in editor for strings in script events

	// negative values for ints and floats

	// Copy button for actors

	// The script trigger that triggers when a script event is completed might
	// not work at the moment in the editor coz we're making copies :P

	// Triggers - obj attacked, object health < x, object
	// !destroyed

	// multiple triggers per event

	// Changing object drawable using postload + imagePath

	// Put together a level, something to work with from the get-go

	// After a load - images, colors, weapons wont line up... weapons is the big
	// one coz its multiple selection...

	// Cant change the rgba of colours coz of the attributes bit assuming
	// everything is an
	// int :D

	// change to the imagetexture wont save :D it doesnt update the imagepath,
	// just the texture

	// highlight decorations when selected in the list

	// Put in some opengl 2d particle effects/ water effects/ special effects, i
	// dunno, give it a google

	// could use the save/load functionality to make a undo/redo process in the
	// editor :D

	// colors need to be handled better in the save/load process

	// color delete button... hmmmmmmm.............. keep minimum to one
	// color... if faction has color... put them at color(0) after the delete

	// decorations tab (will need boolean), anchor the decorations to a square i
	// think...

	// esc button usage, particularly when ur going full screen

	// weapons attribute :/
	// could limit the user to 5 weapons.........
	// so the attributes would be weapon0 to weapon4

	// image attribute.......

	// missing actor attributes...
	// title
	// actorLevel
	// travelDistance
	// equipped weapon

	// missing object attributes
	// weapons

	// next i need to handle Lists and booleans generically (TOTES DOABLE)
	// for lists show them the options 0 to X, they tap one of them, BOOM
	// booleans can be toggles

	// Tabs on left of screen - faction, actors, object, scriptevent, script
	// trigger, weapon,
	// level, squares, relationships, decoration

	// I think the attributes window should be beside the settins windwo

	// circle around selected character looks kinda cool... could have circle
	// around selected and trianlge around target... or something...

	// Info dialog thing on right of screen

	// Ghost object / Ghost actor should appear over the square when ur in add
	// mode and... hovering over a square

	// check unnessesary exceptions, like the one in WindowButton

	// I want whatever ur hovering over to light up, this no light up shite it
	// BULLSHIT
	// Could just be an outline of the square... i dunno

	// Under the mouse it should say MOVE,SELECT,ATTACK,HEAL like a contextual A
	// button. Mayeb the hover details should come out of the mouse?!
	// Interesting...

	// look up how to switch between textures and quads in opengl, coz the way
	// im doing it feels like BS

	// buttons do not resize nicely

	// buttons do not take the click from the level

	// vsync in settings Display.setVSyncEnabled(true);

	// FUCK PIVOT
	// board size (text box)
	// add/remove/edit faction (text box, check box, button, dropdown)
	// add/remove/edit character
	// add/remove/edit object
	// add/remove/edit script event
	// add/remove/edit script trigger

	// So GUI items - theres swing, and a bunch of libs that provide gui
	// elements for lwjgl...
	// http://wiki.lwjgl.org/wiki/Game_Engines_and_Libraries_Using_LWJGL#GUI_Libraries
	// i dunnooooo.....

	// I think I need a level editor...
	// Based on Classes...
	// How hard could it be???
	// Look at the map maker for TT... Might reveal some info on the mechanics
	// they use...
	// I want to be able to try out a level im playing immediately (see aoe2,
	// the TT lvl editor is bollox and must have seriously hindered development)

	// unit tests + monkey...

	// random generator button for the level editor... might help people (me...)
	// with creativity

	// Why does script trigger get level?

	// Need to maintain an array of active events

	// multiple triggers for an event :D

	// I've put in the ability to have multiple scriptevents at a time, but...
	// need to sort out some sort of precedence, particularly for clicks
	// (maybe...)

	// Refactor day

	// Pretty art day

	// Try build a full level...
	// Needs dialog at the start
	// Needs a goal
	// Needs dialog at the end
	// What size is a FE level? Awakenings chapter 9 is 20x20, chapter 5 is
	// 16x21, prologue chapter is 14x15,
	// http://fireemblem.wikia.com/wiki/List_of_chapters_in_Fire_Emblem:_Awakening

	// bug - pow doesnt show up coz of the hover preview

	// http://www.reddit.com/r/gamedev

	// Public domain map to build my world around, this link may be public
	// domain stuff... http://www.usgs.gov/pubprod/

	// write equipBestWeapon method - create fights at that distance and kill
	// dem bitches... interesting problem coz u know the enemy is gonna select
	// the best weapon to counter you... need to take that in to a/c in the sort
	// method... maybe... it's quite a confusing problem. BUT... it's an AI
	// problem, not a game mechanics problem, so it's fine to be complicated.
	// ALSO... need to check this for all sorting of best fights. If weapon will
	// kill someone or cant be countered... its the best :D
	// HERE IT IS FOR ALL RANGES (so for not all ranges it sshould be easier...)
	// do it from the enemy's point of view......
	// so all attacks from all ranges......
	// work out how they'll counter......
	// then compile their selections the (0) after sorting...
	// switch to our guy, using those fights... pick the best...

	// ANALYZE TT VS FE (there might be a youtube video or an aticle about it.
	// See where one beats the other.)

	// The way they hold the selected weapon is really cool, build that in, an
	// anchor point for the weapons (for where the persons hand(s) are), and
	// maybe arms with anchor points... and maybe some elbows w/ anchor point on
	// those arms... animate them the way that u do for 3d items like that....
	// (move the camera, not the arm...)

	// do a withinBounds() method for the buttons

	// animate the buttons comin out of the playa

	// When you hover over a square you can move to show where you can reach and
	// with what weapon... ot something... i dunno, maybe just highlight the
	// squares

	// Push

	// In the fight sorter, should take enemy damage post attack in to account,
	// they keep attacking that guy w/ 100 health :D

	// pushing (the skips, enemies, allies)

	// third (green) team with relationships towards others

	// try make a 16 bit character for the game board out of a profile image,
	// all pixels and shit and awesome, i could use like any body... once it
	// shrunk down enough it wont matter...

	// Character made out of a creepy doll face!

	// do profile image of character w/ gas mask in photoshop

	// weapon in the dumpster

	// attributes along bottom of character's square

	// More AI (see the enum list at the top of Faction.java)

	// For decoration - a can, a bottle, rubbish bag

	// The VS looks really cool on its own, maybe move the bars out so that u
	// can see the VS, then make the bars drain in the same direction

	// advantage/disadvantage (goes on top of weapon, tick VS X), special
	// effects(sleep, confuse, poison etc.)

	// Look up what type of text is most legible

	// I think the things should empty from a different direction, dont like the
	// middle. BUT... the text is getting hard to read :D

	// fade out the other baddies when looking at preview, or hide them
	// completely... or... i dunno... but its overlapping w/ weapon images on
	// other enemies squares which is shite.

	// use different colour than white for highlights, its confusing

	// blocked symbol if they have no weapon to attack with, or maybe leave it
	// as nothing?

	// make a single item for all these reused colors, WAY too many calls to new
	// color
	// dialog has a weird drawing thing...
	// SOmething clever where the health bar pulls back to reveal a skull or
	// something :P
	// also... need textures rather than flat color probably
	// Need to find a way to make the preview clearer, at the moment its just
	// messy and confusing. Maybe lines. Or health bars only as big as a weapon
	// symbol... Also, health bars need a bg (same as the normal health bars
	// probably...

	// see if theres a way to force the white lines to draw even when they're
	// tiny, maybe if i draw lines rather than quads?

	// make the white lines translucent? that might look better

	// Make a util to fill an area with a texture

	// seperate the attack + counter attack by 250ms

	// Look up "the mechanics of fire emblem"

	// feck it in to Android and c what happens ;)

	// enemy move grid all/single/toggle multiple

	// button to show the battle preview info for all enemies

	// undo button for moves

	// Special preview for when neither of them have weapons :D, when
	// fights.size() == 0

	// Look up strategy guides for similar games, will help with AI writing,
	// will help clarify some of the subtleties in these games

	// DONE

	// go through the code and add comments b4 i foget what the fuck i was doing

	// merge actor and game object?

	// see if u can find a font that will work over 60

	// should i take some time out and make it look prettier? might make me
	// appreciate it more. Like take a saturday and do graphical tweaks :) YUS

	// the bars look a lot better on the right, that could just be because of
	// the player model

	// Shit written on walls/ground like stealth bastard. Text could take up a
	// whole row of a grid. One word per row. Cool style. "YOUR TURN",
	// "YOU WIN", "HERO VANQUISHED", "YOU DIED", Dark souls - (BONFIRE LIT, YOU
	// DIED, YOU DEFEATED), i particularly like bonfire lit, coz it seemed so
	// retarded. Bloodborne - "Prey Slaughtered" VERY COOL

	// Work on user manual, make it COOL

	// Work on design rules

	// match enemy weapons with yours for what they'll counter w/, or draw
	// arrows from one to the other :D MESSYI

	// SORT THE NOTES IN TO CATEGORIES

	// Underneath of bar goes from white to black as they're dying, also colored
	// part of bar goes down

	// match enemy weapons with yours for what they'll counter w/, or draw
	// arrows from one to the other :D MESSYI

	// DONE

	// Dialogs stay open if open after ending turn

	// attacking enemies
	// currently when i click on an enemy it just selects them
	// in TT you attack enemies by selecting the sword then the square to
	// attack, this is a good system, flexible but slow
	// also... in TT it doesn't show u where u can attack given ur start pos
	// in FE you move beside them and attack

	// ranges -
	// currently weapon range works as any range up to it's range value
	// So if range is 3, the weapon can attack 1,2 or 3 blocks away
	// Does it make sense u can use a bow at short range? kinda does, and
	// doesn't...
	// It's close enough to get hit b a sword so......
	// Also, i think all the box dudes should have weak daggers by default, and
	// be shit at using them :D

	// WRITE A RULE BOOK, this is cool and appreciated by retro fans and easy to
	// do
	// ALSO... it must be completely pointless coz if u need instructions ur app
	// is broken

	// More enemies on screen for better testing

	// Colorize the log to match enemy/ally

	// When u highlight a square to move to, also highlight where u can attack
	// if you move there... LOTS of highlighting.
	// Maybe like. X on the squares u can attack
	// OOOOORRRRR show the icon of the weapon(s) that can attack there ;)
	// YES, start putting in all these useful icons....

	// AI to take over on their turn
	// DONE - Phase 1. End their turn
	// DONE - Phase 2. Move their dudes randomly
	// DONE - Phase 3. Put timers in so that u can see them move
	// Move towards nearest enemy
	// Phase 4. Turn level plan
	// Phase 5. Battle level plan

	// There's got to be a better way to calculate distances for EVERY square
	// from the selected guy (mark selected guys as zero, all others as
	// integer.max
	// got around in a circle and set all free squares to 1 (or 0 + travel cost)
	// go around all the 1s and set all free squares that dont have a number to
	// (1 + travelcost)
	// continue until u run out of squares w/ numbers to go around
	// this sounds a lot better :'D
	// blllllllergh
	// so if travel distance is 5 and attack range is 2 1-5 is reachable, 1-7 is
	// attackable (unless we have a min range!!!

	// Don't highlight AI square when u mouse over it...

	// object should give indication it contains something,
	// a little twinkle or something
	// so that user doesnt have to click everything

	// show path for attack squares too
	// cant necessarily attack squares u can walk if ur min range isnt 1

	// undo button

	// how about using neural networking to teach different types of strategies?
	// offensive, defensive...
	// would have to be a random setup every time
	// visibles are - empty squares, enemies squares, allies squares,
	// impasssable squares

	// tkae time to move.....

	// make the dialog stay on screen by moving to other corners

	// put photoshop on ur phone
	// Square.actor, Square.inanimateObject
	// move to 128 px per aquare
	// scrolling ;) that's a good one!
	// convert to cm/inches for mouse dragging calculation!?
	// calc reachable and attackable squares is being called twice in update...

	// route drawer(the route is sorta worked out when u finding which squares
	// are available to to) (when we end on a square could have
	// routeTo.add(this.squaresInPath.clone)
	// //WAIT... need to add ROUTETO even if square is not at the end of a route
	// :D
	// For now I could just highlight squares in the route, draw the lines and
	// arrows later
	// route drawn needs to show how many steps it takes... probably... i
	// think..... or how many u'll have left????
	// dialogs
	// factions
	// attacking

	// Weapons and weapon reach
	// On the square for weapon reach draw
	// How it will compare to the enemey

	// SWORD^ VS AXEv
	// 90% VS 50%
	// 11x2 VS 12x1

	// BOW VS 0
	// 80% VS 0%
	// 20 VS 0

	// Color blue/red/yellow depending on how u'll do

	// Go to some museuoms and take ohots of shit to use

	// Read some devblogs and indepth articles (RPS should point me in the right
	// direction) (see if i can filter by blogs/diaries/stories)

	// Take a photo of ur own face u dork!!

	// Make all the over the top details configurable
	// The weapons icons beside the characters
	// The wepaons icons on where you can attack
	// maybe ur weapon icons are always on the left and the enemies are always
	// on the right, w/ a VS or something... i dunno... VS what they'd retaliate
	// with, i'm sure i'll work it out CONCEPT GO

	// In log text, make it personal to the player / target / weapons that were
	// used, so that the actors / weapons / styles will grow on the player
	// Also... if they do something clever, tell them (ITS SUPER EFFECTIVE!!!!!)
	// (Robots don't like lightning strikes, ShitBot 3000 has been overcharged)
	// or speach bubbles rather than log text? or speachbubbles and log the
	// speach like aoe2)
}
