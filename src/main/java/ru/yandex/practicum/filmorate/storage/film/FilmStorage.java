package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();
    List<Film> findPopularFilms(int count);
    Optional<Film> findFilmById(int id);
    Film create(Film film);
    void delete(Film film);
    Film update(Film film);

}
