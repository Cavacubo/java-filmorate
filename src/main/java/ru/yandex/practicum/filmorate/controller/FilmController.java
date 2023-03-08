package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        List<Film> films = filmService.findAllFilms();
        log.info("Current number of films: {}", films.size());
        return films;
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") int filmId) {
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(
            @RequestParam(value = "count", required = false, defaultValue = "10") int count
    ) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);
        log.debug("Film: {} added", film.getName());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validate(film);
        log.debug("Film: {} updated", film.getName());
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        if (userId < 0) {
            throw new NotFoundException("User Id: " + userId + " should be a positive number");
        }
        log.debug("Like from user with id: {} to film: {} added",
                userId, filmService.getFilmById(filmId).getName());
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        if (userId < 0) {
            throw new NotFoundException("User Id: " + userId + " should be a positive number");
        }
        log.debug("Like from user with id: {} to film: {} deleted",
                userId, filmService.getFilmById(filmId).getName());
        filmService.deleteLike(filmId, userId);
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
