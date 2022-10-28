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
package io.github.cardinal.alpha.spring.example.reactive.service;

import io.github.cardinal.alpha.spring.example.reactive.entity.Profile;
import io.github.cardinal.alpha.spring.example.reactive.entity.Book;
import io.github.cardinal.alpha.spring.example.reactive.entity.type.Updatable;
import io.github.cardinal.alpha.spring.example.reactive.mapping.type.UpdateMapping;
import io.github.cardinal.alpha.spring.example.reactive.service.type.CrudService;
import io.github.cardinal.alpha.spring.generic.bind.GenericComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@GenericComponent(typeParameters = {Book.class, Integer.class})
@GenericComponent(typeParameters = {Profile.class, Integer.class})
public class BasicEntityCrudService<T extends Updatable<ID>, ID> implements CrudService<T, T, ID>{
    
    @Autowired
    private JpaRepository<T, ID> repository;
    
    @Autowired
    private UpdateMapping<T> updater;
    
    @Override
    public Mono<T> get(Mono<ID> id){
        return id.map(i-> repository.findById(i).get());
    }
    
    @Override
    public Mono<Page<T>> list(Mono<Pageable> paginate){
        return paginate.map(p-> repository.findAll(p));
    }
    
    @Override
    public Mono<T> create(Mono<T> entity){
        return entity.map(e->{
                    if(e.getId() != null)
                        e.setId(null);
                    return e;
                })
                .map(repository::save);
    }
    
    @Override
    public Mono<T> update(Mono<ID> oldEntityId, Mono<T> update){
        return oldEntityId.zipWith(update, (i, u)-> new UpdateHolder<>(i, u))
                .map(u->{
                    T oldEntity = repository.findById(u.getId()).get(),
                        newData = u.getUpdate();
                    if(newData.getId() != null)
                        newData.setId(null);
                    updater.updateEntity(newData, oldEntity);
                    return oldEntity;
                })
                .map(repository::save);
    }
    
    @Override
    public Mono<ID>delete(Mono<ID> id){
        return id.map(i->{
                    repository.deleteById(i);
                    return i;
                });
    }
    
}
