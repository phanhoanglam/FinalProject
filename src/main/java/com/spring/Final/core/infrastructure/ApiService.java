package com.spring.Final.core.infrastructure;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class ApiService<T, R extends JpaRepository<T, Integer>> {

    @Autowired
    protected ModelMapper modelMapper;
    protected R repository;

    public List<T> findAll() {
        return this.repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public T getOne(int id) {
        return this.repository.getOne(id);
    }

    public List<T> getAllByIds(List<Integer> ids) {
        return this.repository.findAllById(ids);
    }

    protected int getPage(int pageNumber) {
        return pageNumber > 0 ? pageNumber - 1 : 0;
    }


}
