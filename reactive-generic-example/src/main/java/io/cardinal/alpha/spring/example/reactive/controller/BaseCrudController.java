/*
 * The MIT License
 *
 * Copyright (c) 2022 Cardinal Alpha
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
package io.cardinal.alpha.spring.example.reactive.controller;

import io.cardinal.alpha.spring.example.reactive.domain.SimplePagination;
import io.cardinal.alpha.spring.example.reactive.service.type.CrudService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
public abstract class BaseCrudController<UpT, DownT, ID>{
    
    @InitBinder 
    public void initBinder(WebDataBinder binder){
        
    }
    
    @Data
    @AllArgsConstructor
    public static class PaginateHolder{
        private Integer page, size;
    }
    
    @Data
    @AllArgsConstructor
    public static class ListingPage{
        private Integer page, size;
    }
    
    @Autowired
    protected CrudService<UpT, DownT, ID> service;
    
    
    @GetMapping("/{id}")
    public Mono<DownT> getHandler(@PathVariable ID id){
        return service.get(Mono.just(id));
    }
    
    @GetMapping("")
    public Mono<SimplePagination<DownT>> listingHandler(@Nullable @RequestParam Integer page, @Nullable @RequestParam Integer size){
        return Mono.just(new PaginateHolder(page, size))
                .map(pagination->{
                        Integer p = pagination.getPage(),
                                s = pagination.getSize();
                        if(p == null)
                             p = 0;
                         else
                             p = p < 1 ? 0 : p - 1;
                         if(s == null)
                             s = 20;
                         else
                             s = s < 0 ? 20 : s;
                         return Pageable.ofSize(s).withPage(p);
                })
                .as(paginateM -> service.list(paginateM))
                .map(down -> new SimplePagination<>(down));
    }
    
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteHandler(@PathVariable ID id){
        return service.delete(Mono.just(id))
                        .map(i-> ResponseEntity.ok("DELETED"));
    }
    
}
