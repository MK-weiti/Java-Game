# Java game

## By Mateusz Kordowski

**Instalation instruction:**

1. download file from master branch.
2. unpack
3. double click on it (or open terminal and run via java)
4. if it is not working check in properties if file is executable

### Or

1. go to the JGame (where is gradlew file)
2. in shell run ./gradlew desktop:dist (or just gradlew desktop:dist on windows)
3. the result is in JGame -> desktop -> build -> libs -> (chose game version)
4. run file: java -jar (file name)

**Code instalation instruction:**

1. download
2. unpack
3. in your IDE import as a gradle project (use gradle version between 4.7 and 4.10.2).

**In game instruction:**

 - To toggle to the menu press Esc. 
 - To enable the debug mode go to ~/.prefs/com.gdx.jgame.Configuration 
 - When in a debug mode type command in console example: 
	`screate-enemy texture=honeybee.png scale=0.05 position=500 250 groupName=test_group`
	`debug-lines jbox=true`
	`debug-lines jbox=false hud=true menu=true`
	(for now no other commands are suported)
