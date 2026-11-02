import java.util.List;

public class ProxyDemo {

    public static void main(String[] args) {

        List<Song> songs = List.of(
                new Song(1, "Midnight Drive", "Alex Rivers", "Night Lights", 210),
                new Song(2, "Ocean Waves", "Maya Stone", "Blue Horizon", 195),
                new Song(3, "City Lights", "Alex Rivers", "Night Lights", 230),
                new Song(4, "Morning Sun", "Jordan Lee", "New Beginnings", 180),
                new Song(5, "Summer Rain", "Maya Stone", "Blue Horizon", 205),
                new Song(6, "Lost in Time", "Taylor Brooks", "Memories", 240),
                new Song(7, "Golden Hour", "Jordan Lee", "New Beginnings", 200),
                new Song(8, "Last Goodbye", "Taylor Brooks", "Memories", 225)
        );

        RealSongService server = new RealSongService(songs);
        SongService service = new SongServiceProxy(server);

        System.out.println("Searching by ID:");
        System.out.println(service.searchById(1));
        System.out.println(service.searchById(1));

        System.out.println();

        System.out.println("Searching by Title:");
        System.out.println(service.searchByTitle("Ocean Waves"));
        System.out.println(service.searchByTitle("Ocean Waves"));

        System.out.println();

        System.out.println("Searching by Album:");
        System.out.println(service.searchByAlbum("Night Lights"));
        System.out.println(service.searchByAlbum("Night Lights"));

        System.out.println();

        System.out.println("Additional Searches:");
        System.out.println(service.searchById(8));
        System.out.println(service.searchByTitle("Golden Hour"));
        System.out.println(service.searchByAlbum("Memories"));

        System.out.println();

        System.out.println("Total server requests: " + server.getRequestCount());
    }
}