package com.example.controller;

import com.example.domain.Views;
import com.example.domain.WsSender;
import com.example.dto.EventType;
import com.example.dto.ObjectType;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.function.BiConsumer;

public abstract class AbstractRestController<T, R extends JpaRepository<T, ?>> {
    protected R repo;
    protected final BiConsumer<EventType, T> wsSender;

    public AbstractRestController(R repo, WsSender wsSender) {
        this.repo = repo;
        this.wsSender = wsSender.getSender(ObjectType.USER, Views.FullProfile.class);
    }

    @GetMapping
    public Page<T> getAll(@PageableDefault Pageable pageable){
        return repo.findAll(pageable);
    }

    @GetMapping("{id}")
    public T getOne(@PathVariable ("id") T obj){
        return obj;
    }

    @PostMapping
    public T add(@RequestBody T obj){
        wsSender.accept(EventType.CREATE, obj);
        return repo.save(obj);
    }

    @PutMapping("{id}")
    public T update(@PathVariable ("id") T dbObj,
                    @RequestBody T obj){
        BeanUtils.copyProperties(obj, dbObj, "id");
        wsSender.accept(EventType.UPDATE, obj);
        return repo.save(obj);
    }

    @DeleteMapping("{id}")
    public void delete (@PathVariable ("id") T obj){
        wsSender.accept(EventType.REMOVE, obj);
        repo.delete(obj);
    }
}
