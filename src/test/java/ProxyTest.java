import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyTest {

    private RealSongService createServer() {
        return new RealSongService(List.of(
                new Song(1, "Midnight Drive", "Alex Rivers", "Night Lights", 210),
                new Song(2, "Ocean Waves", "Maya Stone", "Blue Horizon", 195),
                new Song(3, "City Lights", "Alex Rivers", "Night Lights", 230)
        ));
    }

    @Test
    public void testSongMetadata() {
        Song song = new Song(1, "Midnight Drive",
                "Alex Rivers", "Night Lights", 210);

        assertEquals(1, song.getId());
        assertEquals("Midnight Drive", song.getTitle());
        assertEquals("Alex Rivers", song.getArtist());
        assertEquals("Night Lights", song.getAlbum());
        assertEquals(210, song.getDuration());
        assertTrue(song.toString().contains("Midnight Drive"));
    }

    @Test
    public void testRealServerSearchById() {
        RealSongService server = createServer();

        Song song = server.searchById(1);

        assertNotNull(song);
        assertEquals("Midnight Drive", song.getTitle());
        assertEquals(1, server.getRequestCount());
    }

    @Test
    public void testRealServerSearchByTitle() {
        RealSongService server = createServer();

        List<Song> results = server.searchByTitle("Ocean Waves");

        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getId());
        assertEquals(1, server.getRequestCount());
    }

    @Test
    public void testRealServerSearchByAlbum() {
        RealSongService server = createServer();

        List<Song> results = server.searchByAlbum("Night Lights");

        assertEquals(2, results.size());
        assertEquals(1, server.getRequestCount());
    }

    @Test
    public void testProxyCachesIdSearch() {
        RealSongService server = createServer();
        SongService proxy = new SongServiceProxy(server);

        Song first = proxy.searchById(1);
        Song second = proxy.searchById(1);

        assertSame(first, second);
        assertEquals(1, server.getRequestCount());
    }

    @Test
    public void testProxyCachesTitleSearch() {
        RealSongService server = createServer();
        SongService proxy = new SongServiceProxy(server);

        List<Song> first = proxy.searchByTitle("Ocean Waves");
        List<Song> second = proxy.searchByTitle("Ocean Waves");

        assertEquals(first, second);
        assertEquals(1, server.getRequestCount());
    }

    @Test
    public void testProxyCachesAlbumSearch() {
        RealSongService server = createServer();
        SongService proxy = new SongServiceProxy(server);

        List<Song> first = proxy.searchByAlbum("Night Lights");
        List<Song> second = proxy.searchByAlbum("Night Lights");

        assertEquals(first, second);
        assertEquals(1, server.getRequestCount());
    }

    @Test
    public void testDifferentSearchesContactServer() {
        RealSongService server = createServer();
        SongService proxy = new SongServiceProxy(server);

        proxy.searchById(1);
        proxy.searchByTitle("Ocean Waves");
        proxy.searchByAlbum("Night Lights");

        assertEquals(3, server.getRequestCount());
    }

    @Test
    public void testMissingSongIsCached() {
        RealSongService server = createServer();
        SongService proxy = new SongServiceProxy(server);

        assertNull(proxy.searchById(99));
        assertNull(proxy.searchById(99));

        assertEquals(1, server.getRequestCount());
    }

    @Test
    public void testTitleSearchIgnoresCase() {
        RealSongService server = createServer();
        SongService proxy = new SongServiceProxy(server);

        assertEquals(1, proxy.searchByTitle("ocean waves").size());
        assertEquals(1, proxy.searchByTitle("OCEAN WAVES").size());
        assertEquals(1, server.getRequestCount());
    }

    @Test
    public void testProxyDemo() {
        assertDoesNotThrow(() -> ProxyDemo.main(new String[0]));
    }
}