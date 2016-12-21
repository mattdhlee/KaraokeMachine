//the UI class
import java.io.InputStreamReader;
import java.util.Queue;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KaraokeMachine {
	private SongBook mSongBook;
	private BufferedReader mReader;
	private Map<String,String> mMenu;
	private Queue<SongRequest> mSongRequestQueue;
	
	public KaraokeMachine(SongBook songBook)  {
		mSongBook = songBook;
		mReader = new BufferedReader(new InputStreamReader(System.in));
		mSongRequestQueue = new ArrayDeque<>();
		mMenu = new HashMap<String,String>();
		mMenu.put("add", "Add a new song to the song book");
		mMenu.put("play", "Play next song queue");
		mMenu.put("choose", "Choose a song to sing!");
		mMenu.put("quit", "Give up. Exit the program");
	}
	
	private String promptAction() throws IOException {
		System.out.printf("There are %d songs available and %d in the queue. Your options are: %n", mSongBook.getSongCount(), mSongRequestQueue.size());
		
		for (Map.Entry<String,String> option : mMenu.entrySet()) {
			System.out.printf("%s - %s %n", option.getKey(), option.getValue());
		}
		System.out.print("What do you want to do: ");
		String choice = mReader.readLine();
		return choice.trim().toLowerCase();
	}
	public void run() {
		String choice = "";
		do {
			
			try {
				choice = promptAction();
				switch(choice) {
				case "add":
					Song song = promptSong();
					mSongBook.addSong(song);
					System.out.printf("%s added! %n%n", song);
					break;
				case "quit":
					System.out.println("Thanks for p laying!");
					break;
				case "choose":
					String singerName = promptSongForArtist();
					String artist = promptArtist();
					Song artistSong = promptSongForArtist(artist);
					SongRequest songRequest = new SongRequest(singerName, artistSong);
					if (mSongRequestQueue.contains(songRequest)) {
						System.out.printf("%n%n Whoops %s already requested %s! %n",
								singerName,        ///shouldnt it be previous singername who requested the song?
								artistSong
								);
						break;
					}
					mSongRequestQueue.add(songRequest);
					System.out.printf("You chose %s's %s. %n", artist, artistSong);
					break;
				case "play":
					playNext();
					break;
				default:
					System.out.printf("Unknown choice : '%s' . Try Again. %n%n%n",choice);
				}
			}
			catch(IOException ioe){
				System.out.println("Problem with input");
				ioe.printStackTrace();
			}
			catch(NumberFormatException nfe) {
				System.out.println("Problem with input format");
				nfe.printStackTrace();
			}
		}
		while(!choice.equals("quit"));	
	}

	private String promptSongForArtist() throws IOException {
		System.out.print("Enter the singer's name:   ");
		return mReader.readLine();
	}

	private Song promptSong() throws IOException{
		System.out.print("Enter the Arist's name:  ");
		String artist = mReader.readLine();
		System.out.print("Enter the title:  ");
		String title = mReader.readLine();
		System.out.print("Enter the video URL");
		String videoUrl = mReader.readLine();
		return new Song(artist,title,videoUrl);
	}
	
	private String promptArtist() throws IOException {
		System.out.print("Available Artists:");
		List<String> artists = new ArrayList<String>(mSongBook.getArtists());
		int index = promptIndex(artists);
		return artists.get(index);
	}
	
	private Song promptSongForArtist(String artist) throws IOException {
		List<Song> songs = mSongBook.getSongsForArtists(artist);
		List<String> songTitles = new ArrayList<>();
		for (Song song: songs) {
			songTitles.add(song.getTitle());
		}
		int index = promptIndex(songTitles);
		return songs.get(index);
	}
	
	private int promptIndex(List<String> options) throws IOException {
		int counter = 1;
		for (String option: options) {
			System.out.printf("%d.) %s %n", counter, option);
			counter ++;
			
		}
		System.out.print("Your choice:   ");
		String optionAsString = mReader.readLine();
		int choice  = Integer.parseInt(optionAsString.trim());
		
		return choice - 1; 
	}
	
	public void playNext() {
		SongRequest songRequest = mSongRequestQueue.poll();
		if (songRequest == null) {
			System.out.println("Sorry there aren o songs in the queue. Use choose from the menu to add some.");
		}
		else {
			Song song = songRequest.getSong();
			System.out.printf("%n%n%n Ready %s? Open %s to hear %s %n%n%n",
					songRequest.getSingerName(),
					song.getVideoUrl(),
					song.getTitle(),
					song.getArtist());
		}
	}
		
		
}
	
	
	
	
	

