package singleton;

import model.Movie;
import model.Seat;
import model.Show;
import enums.SeatStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Stores and searches movie records.
 */
public class MovieCatalog {

    private static volatile MovieCatalog instance;

    private final List<Movie> movies;
    private final Map<Integer, Show> shows; // Store for admin show management
    private final DBConnection dbConnection;
    private int nextShowId = 100;

    private MovieCatalog() {
        this.movies = new ArrayList<>();
        this.shows = new HashMap<>();
        this.dbConnection = DBConnection.getInstance();
        loadMoviesFromDb();
        loadShowsFromDb();
        if (movies.isEmpty()) {
            seedMovies();
        }
    }

    public static MovieCatalog getInstance() {
        if (instance == null) {
            synchronized (MovieCatalog.class) {
                if (instance == null) {
                    instance = new MovieCatalog();
                }
            }
        }
        return instance;
    }

    public List<Movie> getAllActiveMovies() {
        List<Movie> active = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.isActive()) {
                active.add(movie);
            }
        }
        return active;
    }

    public List<Movie> searchMovies(String titleKeyword, String genre, String language, double minRating) {
        String normalizedTitle = normalize(titleKeyword);
        String normalizedGenre = normalize(genre);
        String normalizedLanguage = normalize(language);

        List<Movie> results = new ArrayList<>();
        for (Movie movie : movies) {
            if (!movie.isActive()) {
                continue;
            }

            boolean titleMatch = normalizedTitle.isEmpty() || normalize(movie.getTitle()).contains(normalizedTitle);
            boolean genreMatch = normalizedGenre.isEmpty() || normalize(movie.getGenre()).equals(normalizedGenre);
            boolean languageMatch = normalizedLanguage.isEmpty() || normalize(movie.getLanguage()).equals(normalizedLanguage);
            boolean ratingMatch = movie.getRating() >= minRating;

            if (titleMatch && genreMatch && languageMatch && ratingMatch) {
                results.add(movie);
            }
        }
        return results;
    }

    // ===== Admin CRUD operations =====
    public Movie getMovieById(int movieId) {
        for (Movie m : movies) {
            if (m.getMovieId() == movieId) {
                return m;
            }
        }
        return null;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
        saveMovie(movie);
    }

    public void updateMovie(Movie movie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getMovieId() == movie.getMovieId()) {
                movies.set(i, movie);
                saveMovie(movie);
                return;
            }
        }
    }

    public void deleteMovie(int movieId) {
        movies.removeIf(m -> m.getMovieId() == movieId);
        String sql = "DELETE FROM movies WHERE movie_id = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete movie " + movieId, e);
        }
    }

    public Show getShowById(int showId) {
        return shows.get(showId);
    }

    public void addShow(Show show) {
        if (show.getShowId() == 0) {
            show.setShowId(nextShowId++);
        }
        if (show.getSeats().isEmpty() && show.getTotalSeats() > 0) {
            generateSeatsForShow(show);
        }
        shows.put(show.getShowId(), show);
        saveShow(show);
        saveSeats(show);
    }

    public void updateShow(Show show) {
        shows.put(show.getShowId(), show);
        saveShow(show);
        saveSeats(show);
    }

    public void deleteShow(int showId) {
        shows.remove(showId);
        try (PreparedStatement ps1 = dbConnection.getConnection().prepareStatement("DELETE FROM booking_seats WHERE show_id = ?");
             PreparedStatement ps2 = dbConnection.getConnection().prepareStatement("DELETE FROM seats WHERE show_id = ?");
             PreparedStatement ps3 = dbConnection.getConnection().prepareStatement("DELETE FROM shows WHERE show_id = ?")) {
            ps1.setInt(1, showId);
            ps1.executeUpdate();
            ps2.setInt(1, showId);
            ps2.executeUpdate();
            ps3.setInt(1, showId);
            ps3.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete show " + showId, e);
        }
    }

    public List<Show> getAllShows() {
        return new ArrayList<>(shows.values());
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    private void seedMovies() {
        addMovie(new Movie(1, "Interstellar", "Sci-Fi", "English", 8.9, true));
        addMovie(new Movie(2, "Inception", "Sci-Fi", "English", 8.8, true));
        addMovie(new Movie(3, "Kantara", "Action", "Kannada", 8.2, true));
        addMovie(new Movie(4, "Dangal", "Drama", "Hindi", 8.3, true));
        addMovie(new Movie(5, "The Lion King", "Animation", "English", 8.5, false));
        addMovie(new Movie(6, "Drishyam", "Thriller", "Malayalam", 8.6, true));
    }

    private void loadMoviesFromDb() {
        String sql = "SELECT movie_id, title, genre, language, rating, active, duration, director, cast_info, release_date, description FROM movies";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setLanguage(rs.getString("language"));
                movie.setRating(rs.getDouble("rating"));
                movie.setActive(rs.getInt("active") == 1);
                movie.setDuration(rs.getDouble("duration"));
                movie.setDirector(rs.getString("director"));
                movie.setCast(rs.getString("cast_info"));
                String releaseDate = rs.getString("release_date");
                if (releaseDate != null && !releaseDate.isBlank()) {
                    movie.setReleaseDate(LocalDate.parse(releaseDate));
                }
                movie.setDescription(rs.getString("description"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load movies", e);
        }
    }

    private void loadShowsFromDb() {
        String showSql = "SELECT show_id, movie_id, show_time, show_date, show_time_datetime, auditorium, total_seats, available_seats, base_price, language, format, is_active FROM shows";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(showSql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Show show = new Show();
                int showId = rs.getInt("show_id");
                show.setShowId(showId);
                show.setShowTime(rs.getString("show_time"));
                show.setShowDate(rs.getString("show_date"));
                String showDateTime = rs.getString("show_time_datetime");
                if (showDateTime != null && !showDateTime.isBlank()) {
                    show.setShowTime(LocalDateTime.parse(showDateTime));
                }
                show.setAuditorium(rs.getString("auditorium"));
                show.setTotalSeats(rs.getInt("total_seats"));
                show.setAvailableSeats(rs.getInt("available_seats"));
                show.setBasePrice(rs.getDouble("base_price"));
                show.setLanguage(rs.getString("language"));
                show.setFormat(rs.getString("format"));
                show.setActive(rs.getInt("is_active") == 1);

                int movieId = rs.getInt("movie_id");
                if (movieId > 0) {
                    show.setMovie(getMovieById(movieId));
                }

                loadSeatsForShow(show);
                shows.put(showId, show);
                nextShowId = Math.max(nextShowId, showId + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load shows", e);
        }
    }

    private void loadSeatsForShow(Show show) {
        String seatSql = "SELECT seat_id, seat_number, seat_type, price, status FROM seats WHERE show_id = ? ORDER BY seat_number";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(seatSql)) {
            ps.setInt(1, show.getShowId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat(
                            rs.getInt("seat_id"),
                            rs.getString("seat_number"),
                            rs.getString("seat_type"),
                            rs.getDouble("price")
                    );
                    seat.setStatus(SeatStatus.valueOf(rs.getString("status")));
                    show.addSeat(seat);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load seats for show " + show.getShowId(), e);
        }
    }

    private void saveMovie(Movie movie) {
        String sql = "INSERT OR REPLACE INTO movies(movie_id, title, genre, language, rating, active, duration, director, cast_info, release_date, description) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, movie.getMovieId());
            ps.setString(2, movie.getTitle());
            ps.setString(3, movie.getGenre());
            ps.setString(4, movie.getLanguage());
            ps.setDouble(5, movie.getRating());
            ps.setInt(6, movie.isActive() ? 1 : 0);
            ps.setDouble(7, movie.getDuration());
            ps.setString(8, movie.getDirector());
            ps.setString(9, movie.getCast());
            ps.setString(10, movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : null);
            ps.setString(11, movie.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save movie " + movie.getMovieId(), e);
        }
    }

    private void saveShow(Show show) {
        String sql = "INSERT OR REPLACE INTO shows(show_id, movie_id, show_time, show_date, show_time_datetime, auditorium, total_seats, available_seats, base_price, language, format, is_active) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, show.getShowId());
            if (show.getMovie() != null) {
                ps.setInt(2, show.getMovie().getMovieId());
            } else {
                ps.setObject(2, null);
            }
            ps.setString(3, show.getShowTime());
            ps.setString(4, show.getShowDate());
            ps.setString(5, show.getShowTimeDateTime() != null ? show.getShowTimeDateTime().toString() : null);
            ps.setString(6, show.getAuditorium());
            ps.setInt(7, show.getTotalSeats());
            ps.setInt(8, show.getAvailableSeatsCount());
            ps.setDouble(9, show.getBasePrice());
            ps.setString(10, show.getLanguage());
            ps.setString(11, show.getFormat());
            ps.setInt(12, show.isActive() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save show " + show.getShowId(), e);
        }
    }

    private void saveSeats(Show show) {
        String deleteSql = "DELETE FROM seats WHERE show_id = ?";
        String insertSql = "INSERT INTO seats(seat_id, show_id, seat_number, seat_type, price, status) VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement deletePs = dbConnection.getConnection().prepareStatement(deleteSql);
             PreparedStatement insertPs = dbConnection.getConnection().prepareStatement(insertSql)) {
            deletePs.setInt(1, show.getShowId());
            deletePs.executeUpdate();

            for (Seat seat : show.getSeats()) {
                insertPs.setInt(1, seat.getSeatId());
                insertPs.setInt(2, show.getShowId());
                insertPs.setString(3, seat.getSeatNumber());
                insertPs.setString(4, seat.getType());
                insertPs.setDouble(5, seat.getPrice());
                insertPs.setString(6, seat.getStatus().name());
                insertPs.addBatch();
            }
            insertPs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save seats for show " + show.getShowId(), e);
        }
    }

    private void generateSeatsForShow(Show show) {
        int seatCount = show.getTotalSeats();
        int seatsPerRow = 10;
        for (int index = 0; index < seatCount; index++) {
            char row = (char) ('A' + (index / seatsPerRow));
            int seatNo = (index % seatsPerRow) + 1;
            String seatNumber = row + String.valueOf(seatNo);
            show.addSeat(new Seat(index + 1, seatNumber, "REGULAR", show.getBasePrice()));
        }
        show.setAvailableSeats(show.getTotalSeats());
    }
}
