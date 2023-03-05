package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();
    Film getFilmById(int id);
    Film create(Film film);
    void delete(Film film);
    Film update(Film film);

}
