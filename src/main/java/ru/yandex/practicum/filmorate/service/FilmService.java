package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    public Film findFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film createFilm(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    public void deleteFilm(Film film) {
        filmStorage.delete(film);
    }

    public void addLike(int filmId, int userId) {
        Film filmToLike = filmStorage.getFilmById(filmId);
        filmToLike.addLike(userId);
        filmStorage.update(filmToLike);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.deleteLike(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new BadRequestException("name: " + film.getName() + " is incorrect");
        }
        if (film.getDescription().length() > 201) {
            throw new BadRequestException("max length of description is 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new BadRequestException("Please check film's parameters");
        }
        if (film.getDuration() <= 0) {
            throw new BadRequestException("Please check film's parameters");
        }
    }
}
