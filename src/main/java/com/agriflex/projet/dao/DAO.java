package com.agriflex.projet.dao;

import java.util.List;

public interface DAO<T> {

    boolean create(T object);

    T findById(int id);

    List<T> findAll();

    boolean update(T object);

    boolean delete(int id);

    boolean exists(int id);

    long count();
}