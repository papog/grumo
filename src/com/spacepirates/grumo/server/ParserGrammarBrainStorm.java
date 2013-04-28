package com.spacepirates.grumo.server;


public class ParserGrammarBrainStorm {
	
	public static class GrammarNode{
		
	public GrammarNode(){
		
	}
	
	public String toRegexp(){
		return "";
	}
	}
	
	public void test(){
		// Date ":" Double
		String sample = "Oct 23 20:38:20 hostname dhclient[32676]: bound to 192.168.0.101 -- renewal in 4350 seconds.\n"
	             +"Oct 23 20:38:20 hostname NetworkManager[1866]: <info> (wlan0): DHCPv4 state changed preinit -> reboot\n"
	             +"Oct 23 20:38:20 hostname NetworkManager[1866]: <info> Activation (wlan0) Stage 4 of 5 (IP4 Configure Get) scheduled...\n"
	             +"Oct 23 20:38:20 hostname NetworkManager[1866]: <info> Activation (wlan0) Stage 4 of 5 (IP4 Configure Get) started...\n";
		
		
		//Group(Word.seq(Number).seq(Number).seq(Lex(":")).seq(Number).seq(Lex":").seq(Number)).toDate("MMM DD HH:MM:SS").as("date").seq(Word.as("hostname")).seq(Word) +  "[" ;
        // 
		//"(\s*\w+\s*\d+\s+\d+:\d+:\d+)\s+(\w+)"
		//group 1 => date , format "MMM DD HH:MM:SS"
	    //group 2 => 
		//Parser<DataPoint> bracketPID = Ignore("[") Number("pid") Ignore("[")
		//Parser<DataPoint> p = new Sequence().add(new Date("timestamp")).add(new Word("hostname").add());
		// Date("MMM DD HH:MM:SS","date") Word("A-Za-z", "hostname") Word("A-Za-z",process_name) Ignore(":")
		// {"date": "Oct 23 20:38:20", "hostname" : "hostname", "process_name": "NetworkManager", "pid" : "1866" , "text" : "...."
		
		// Parametric parsers, generalization
		// Classic parsers with common parameters, recently configured parsers
		// Run the parsers in 'search mode' on the sample string, color sections that match and color parsers in the parser list.
		// the user can choose matching parsers and 
		// the user can write new parsers that combine parsers (sequence, alternative, optional, repetition etc.)
		// the user can drag a parser to a combining parser to add it to it
		// the user can click on a matched zone to see the corresponding parsers
		// the user can select a parser to see what matches.
		// parsers that match the most surface to the top of the list, specific parsers should be higher than less specific ones
		// longer matches get a higher score.
		// Algo for scoring:
		// each parser that matches a character alone gets 1 point
		// if 2 parsers match a character, sum the lengths of the matches containing the character and provide a percentage of the point to each
		// showing matches can be done graphically:
		// * change the color of the text: no waste of space but hard to show more than one matc	h
		// * change the background color behind the text and draw it with bands for each match.
		// * draw match lines below or above the text.
		
		
	}
}
