package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public List<Film> findAll() {
        log.info("Current number of films: {}", films.size());
        return List.copyOf(films.values());
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
            throw new NotFoundException("id does not exist: " + film.getId());
        }
        validate(film);
        films.put(film.getId(), film);
        log.debug("Film: {} updated", film.getName());
        return film;
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
