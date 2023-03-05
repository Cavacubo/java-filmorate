package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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
        log.info("Current number of films: {}", filmService.findAllFilms().size());
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") int filmId) {
        return filmService.findFilmById(filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> findMostPopularFilms(
            @RequestParam(value = "count", required = false, defaultValue = "10") int count
    ) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Film: {} added", film.getName());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug("Film: {} updated", film.getName());
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.debug("Like from user with id: {} to film: {} added",
                userId, filmService.findFilmById(filmId).getName());
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        log.debug("Like from user with id: {} to film: {} deleted",
                userId, filmService.findFilmById(filmId).getName());
        filmService.deleteLike(filmId, userId);
    }
}
