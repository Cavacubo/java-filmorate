package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Current number of films: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);
        film.setId(currentId);
        currentId++;
        films.put(film.getId(), film);
        log.debug("Film: {} added", film.getName());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("id does not exist: " + film.getId());
        }
        validate(film);
        films.put(film.getId(), film);
        log.debug("Film: {} updated", film.getName());
        return film;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("name: " + film.getName() + " is incorrect");
        }
        if (film.getDescription().length() > 201) {
            throw new ValidationException("max length of description is 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Please check film's parameters");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Please check film's parameters");
        }
    }
}
