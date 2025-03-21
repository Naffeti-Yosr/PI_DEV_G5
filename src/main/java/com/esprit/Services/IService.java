package com.esprit.Services;

import java.util.List;

public interface IService <T> {
    void deleteById(int id);

    List<T> get();
    void add(T t);
    void update(T t);
    void delete(T t);
}
