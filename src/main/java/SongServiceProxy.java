import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SongServiceProxy implements SongService {

    private final SongService realService;

    private final Map<Integer, Song> idCache = new HashMap<>();
    private final Map<String, List<Song>> titleCache = new HashMap<>();
    private final Map<String, List<Song>> albumCache = new HashMap<>();

    public SongServiceProxy(SongService realService) {
        this.realService = realService;
    }

    @Override
    public Song searchById(Integer songID) {
        if (idCache.containsKey(songID)) {
            System.out.println("Cache hit: Song ID " + songID);
            return idCache.get(songID);
        }

        System.out.println("Server request: Song ID " + songID);

        Song song = realService.searchById(songID);
        idCache.put(songID, song);

        return song;
    }

    @Override
    public List<Song> searchByTitle(String title) {
        String key = title.toLowerCase(Locale.ROOT);

        if (titleCache.containsKey(key)) {
            System.out.println("Cache hit: Title " + title);
            return new ArrayList<>(titleCache.get(key));
        }

        System.out.println("Server request: Title " + title);

        List<Song> results = realService.searchByTitle(title);
        titleCache.put(key, new ArrayList<>(results));

        return new ArrayList<>(results);
    }

    @Override
    public List<Song> searchByAlbum(String album) {
        String key = album.toLowerCase(Locale.ROOT);

        if (albumCache.containsKey(key)) {
            System.out.println("Cache hit: Album " + album);
            return new ArrayList<>(albumCache.get(key));
        }

        System.out.println("Server request: Album " + album);

        List<Song> results = realService.searchByAlbum(album);
        albumCache.put(key, new ArrayList<>(results));

        return new ArrayList<>(results);
    }
}