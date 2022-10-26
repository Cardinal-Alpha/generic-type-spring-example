/*
 * The MIT License
 *
 * Copyright (c) 2022 Cardinal Alpha <renaldi96.aldi@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.cardinal.alpha.spring.example.mvc.controller;

import io.github.cardinal.alpha.spring.example.mvc.domain.SimplePagination;
import io.github.cardinal.alpha.spring.example.mvc.service.type.CrudService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
public abstract class BaseCrudController<UpT, DownT, ID>{
    
    @Data
    @AllArgsConstructor
    public static class ListingPage{
        private Integer page, size;
    }
    
    @Autowired
    protected CrudService<UpT, DownT, ID> service;
    
    
    @GetMapping("/{id}")
    public DownT getHandler(@PathVariable ID id){
        return service.get(id);
    }
    
    @GetMapping("")
    public SimplePagination<DownT> listingHandler(@Nullable @RequestParam Integer page, @Nullable @RequestParam Integer size){
        if(page == null)
            page = 0;
        else
            page = page < 1 ? 0 : page - 1;
        if(size == null)
            size = 20;
        else
            size = size < 0 ? 20 : size;
        return new SimplePagination<>(service.list(Pageable.ofSize(size).withPage(page)));
    }
    
    public abstract ResponseEntity<String> createHandler(UpT data);
    
    public abstract DownT updateHandler(ID id, UpT update);
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHandler(@PathVariable ID id){
        service.delete(id);
        return ResponseEntity.ok("OK");
    }
    
}
