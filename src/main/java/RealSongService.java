import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RealSongService implements SongService {

    private final List<Song> songs = new ArrayList<>();
    private int requestCount = 0;

    public RealSongService(List<Song> songs) {
        this.songs.addAll(songs);
    }

    private void simulateDelay() {
        requestCount++;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Song searchById(Integer songID) {
        simulateDelay();

        for (Song song : songs) {
            if (Objects.equals(song.getId(), songID)) {
                return song;
            }
        }

        return null;
    }

    @Override
    public List<Song> searchByTitle(String title) {
        simulateDelay();

        List<Song> results = new ArrayList<>();

        for (Song song : songs) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                results.add(song);
            }
        }

        return results;
    }

    @Override
    public List<Song> searchByAlbum(String album) {
        simulateDelay();

        List<Song> results = new ArrayList<>();

        for (Song song : songs) {
            if (song.getAlbum().equalsIgnoreCase(album)) {
                results.add(song);
            }
        }

        return results;
    }

    public int getRequestCount() {
        return requestCount;
    }
}