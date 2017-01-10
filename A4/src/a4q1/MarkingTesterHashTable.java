package a4q1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.LinkLoopException;

import a4q1.MyHashTable.HashEntry;

public class MarkingTesterHashTable {
	private static ArrayList<Song> initSongList() {
		ArrayList<Song> songs = new ArrayList<Song>();
		songs.add(new Song("Paranoid Android", "Radiohead", 1997));
		songs.add(new Song("Machine Gun", "Slowdive", 1993));
		songs.add(new Song("A Change Is Gonna Come", "Sam Cooke", 1964));
		songs.add(new Song("Blue Line Swinger", "Yo La Tengo", 1995));
		songs.add(new Song("Ashes To Ashes", "David Bowie", 1980));
		songs.add(new Song("15 Step", "Radiohead", 2008));
		songs.add(new Song("Reckoner", "Radiohead", 2008));
		songs.add(new Song("Speed Law", "Mos Def", 1999));
		songs.add(new Song("93 'Til Infinity", "Souls of Mischief", 1993));
		songs.add(new Song("Season of the Shark", "Yo La Tengo", 2003));
		songs.add(new Song("Cet Air-La", "France Gall", 1966));
		songs.add(new Song("Space Oddity", "David Bowie", 1969));
		songs.add(new Song("Il Nous Faut Regarder", "Jacques Brel", 1955));
		songs.add(new Song("Happy Holidays", "Jim O'Rourke", 1999));
		songs.add(new Song("Le Premier Bonheur du Jour", "Os Mutantes", 1968));
		songs.add(new Song("Stretch Out And Wait", "The Smiths", 1987));
		songs.add(new Song("Scream", "Black Flag", 1984));
		songs.add(new Song("Europe, After the Rain", "Max Richter", 2002));
		songs.add(new Song("Why Are You Looking Grave?", "Mew", 2005));
		songs.add(new Song("Fallen Angel", "King Crimson", 1974));
		songs.add(new Song("Milk and Honey", "Nick Drake", 2007));
		songs.add(new Song("One Less Bell To Answer", "Burt Bacharach", 2003));
		songs.add(new Song("A Letter To The New York Post", "Public Enemy", 1991));
		songs.add(new Song("Murder Mystery", "Edan", 2005));
		songs.add(new Song("Heaven's Blade", "Coil", 2005));
		songs.add(new Song("Daddy's Gonna Tell You No Lie", "Sun Ra", 2005));
		songs.add(new Song("Burning", "Fugazi", 1989));
		songs.add(new Song("La goualante de pauvre jean", "Edith Piaf", 2007));
		songs.add(new Song("Traveling Riverside Blues", "Led Zeppelin", 1982));
		songs.add(new Song("Sequent C'", "Tangerine Dream", 1974));
		songs.add(new Song("Mothers Of The Disappeared", "U2", 1987));
		songs.add(new Song("Down to the Well", "Pixies", 1990));
		songs.add(new Song("Seras-tu là?", "Michel Berger", 1994));
		songs.add(new Song("Another Brick In The Wall (Part I)", "Pink Floyd", 1979));
		songs.add(new Song("She Lives On A Mountain", "Gorky's Zygotic Mynci", 1999));
		songs.add(new Song("Moody Dipper", "My Education", 2006));
		return songs;
	}
	public static void main(String[] args) {
		ArrayList<Song> songs = initSongList();
		MyHashTable<String,Song> songTable;
		int numBuckets = 7;
		StringBuilder report = new StringBuilder();
		int finalGrade = 0;
		int partGrade;
		songTable = new MyHashTable<String,Song>(numBuckets);
		for (int i = 0; i < 5; i++) {
			songTable.put(songs.get(i).getTitle(), songs.get(i));
		}
		//table should not have rehashed
		report.append("Testing Put():\n");
		partGrade = 10;
		int count = 0;
		for(LinkedList<MyHashTable<String, Song>.HashEntry> l : songTable.buckets){
			for(HashEntry e : l){
				count++;
			}
		}
		if(count != 5){
			report.append("\tPut not working\n");
			partGrade -= 10;
		}else if(count != songTable.size()){
			report.append("\tNot updating entryCount\n");
			partGrade -= 3;
		}else{
			Song s = songTable.put(songs.get(0).getTitle(), new Song("Paranoid Android", "HamRadioHead", 2016));
			if(s == null){
				report.append("\tDid not return old value when key existed already\n");
				partGrade -= 5;
			}
			for(LinkedList<MyHashTable<String, Song>.HashEntry> l : songTable.buckets){
				for(HashEntry e : l){
					if(((Song)e.getValue()).getArtist().equals("Radiohead")){
						report.append("\tDid not replace value with same key, added a second\n");
						partGrade -= 5;
					}
				}
			}
		}
		if(songTable.getNumBuckets() != 7){
			if(songTable.buckets.size() == 7){
				report.append("\tNot setting numBuckets\n");
				partGrade -= 3;
			}
		}
		if(songTable.buckets.size() > 7){
			report.append("\tRehashed to soon, not checking load factor correctly\n");
			partGrade -= 5;
		}else{
			songTable.put(songs.get(5).getTitle(), songs.get(5));///should rehash when 6th song is added
			if(songTable.buckets.size() != 14){
				report.append("\tNot rehashing at correct time\n");
				partGrade -= 5;
			}
		}
		if(partGrade < 0) partGrade = 0;
		finalGrade += partGrade;
		report.append("Put() method: " + partGrade + "/10\n");

		partGrade = 10;
		report.append("Checking get():\n");
		Song s = songTable.get("Machine Gun");
		if(s == null || s.getArtist() != "Slowdive" || s.getYear() != 1993){
			report.append("\tFailed to get song\n");
			partGrade -= 5;
		}
		s = songTable.get("A Change Is Gonna Come");
		if(s == null || s.getArtist() != "Sam Cooke" || s.getYear() != 1964){
			report.append("\tFailed to get song\n");
			partGrade -= 5;
		}
		count = 0;
		for(LinkedList<MyHashTable<String, Song>.HashEntry> l : songTable.buckets){
			for(HashEntry e : l){
				count++;
			}
		}
		s = songTable.get("Fake Song");
		if(s != null){
			report.append("\tReturned non-existing song\n");
			partGrade -= 5;
		}
		if(partGrade < 0) partGrade = 0;
		finalGrade += partGrade;
		report.append("get() method: " + partGrade + "/10\n");

		partGrade = 10;
		report.append("Testing Remove():\n");
		s = songTable.remove("Machine Gun");
		if(s == null || s.getArtist() != "Slowdive" || s.getYear() != 1993){
			report.append("\tFailed to remove song\n");
			partGrade -= 10;
		}else{
			for(LinkedList<MyHashTable<String, Song>.HashEntry> l : songTable.buckets){
				for(HashEntry e : l){
					if(((Song)e.getValue()).getArtist().equals("Slowdive")){
						report.append("\tDid not remove entry from table\n");
						partGrade -= 5;
					}
				}
			}
		}
		if(partGrade < 0) partGrade = 0;
		finalGrade += partGrade;
		report.append("Remove() method: " + partGrade + "/10\n");

		partGrade = 10;
		int index = 0;
		songTable = new MyHashTable<String,Song>(numBuckets);
		for (int i = 0; i < 5; i++) {
			songTable.put(songs.get(i).getTitle(), songs.get(i));
		}
		report.append("Testing rehash():\n");
		songTable.rehash();
		if(songTable.buckets.size() != 14){
			if(songTable.buckets.size() == 7){
				report.append("\tDid not increase size\n");
				partGrade -= 10;
			}
		}else if (songTable.getNumBuckets() != 14){
			report.append("\tNot updating numBuckets\n");
			partGrade -= 3;
		}
		for(HashEntry e : songTable.buckets.get(index)){
			if(((Song)e.getValue()).getTitle().equals("Ashes To Ashes")){
				report.append("\tCopied entries to a new table, entries need to be rehashed\n");
				partGrade -= 10;
			}
		}
		if(partGrade < 0) partGrade = 0;
		finalGrade += partGrade;
		report.append("rehash() method: " + partGrade + "/10\n");

		partGrade = 10;
		report.append("Testing keys()\n");
		songTable = new MyHashTable<String,Song>(numBuckets);
		List<String> keysIn = new LinkedList<>();
		for (Song song : songs) {
			songTable.put(song.getTitle(), song);
			keysIn.add(song.getTitle());
		}
		List<String> keysOut = songTable.keys();
		for(String key : keysOut){
			keysIn.remove(key);
		}
		if(!keysIn.isEmpty()){
			report.append("\tDid not return all the keys\n");
			partGrade -= 10;
		}
		if(partGrade < 0) partGrade = 0;
		finalGrade += partGrade;
		report.append("keys() method: " + partGrade + "/10\n");

		partGrade = 10;
		report.append("Testing values()\n");
		songTable = new MyHashTable<String,Song>(numBuckets);
		List<Song> songsIn = new LinkedList<>();
		for (Song song : songs) {
			songTable.put(song.getTitle(), song);
			songsIn.add(song);
		}
		List<Song> songsOut = songTable.values();
		for(Song song : songsOut){
			remove(songsIn, song);
		}
		if(!songsIn.isEmpty()){
			report.append("\tDid not return all the keys\n");
			partGrade -= 10;
		}
		if(partGrade < 0) partGrade = 0;
		finalGrade += partGrade;
		report.append("values() method: " + partGrade + "/10\n");

		partGrade = 10;
		report.append("Testing iterator():\n");
		songTable = new MyHashTable<String,Song>(numBuckets);
		songsIn = new LinkedList<>();
		for (Song song : songs) {
			songTable.put(song.getTitle(), song);
			songsIn.add(song);
		}
		MyHashTable<String, Song>.HashIterator iterator =  songTable.iterator();
		boolean mismatch = false;
		if(!iterator.hasNext()){
			report.append("\titerator is empty or not implemented\n");
			partGrade -= 10;
		}else{
			while(iterator.hasNext()){
				HashEntry entry = iterator.next();
				Song song = (Song) entry.getValue();
				remove(songsIn, song);
			}
		}
		if(!songsIn.isEmpty()){
			report.append("\tDid not return all the values\n");
			partGrade -= 10;
		}
		if(partGrade < 0) partGrade = 0;
		finalGrade += partGrade;
		report.append("iterator() method: " + partGrade + "/10\n");

		report.append("\nTotal Raw Grade: " + finalGrade + "/70");

//		System.out.println(report);
//		System.out.println("Final Grade: " + finalGrade + "/70");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Report.txt")));
			writer.write(report.toString());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private static void remove(List<Song> songsIn, Song song) {
		for(int i = 0; i < songsIn.size(); i++){
			if(songsIn.get(i).getTitle() == song.getTitle() && songsIn.get(i).getArtist() == song.getArtist() && songsIn.get(i).getYear() == song.getYear()){
				songsIn.remove(i);
			}
		}
	}

}
