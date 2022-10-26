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
package io.github.cardinal.alpha.spring.example.reactive.service;

import io.github.cardinal.alpha.spring.example.reactive.entity.File;
import io.github.cardinal.alpha.spring.example.reactive.entity.Log;
import io.github.cardinal.alpha.spring.example.reactive.entity.type.Updatable;
import io.github.cardinal.alpha.spring.example.reactive.entityDown.FileDownload;
import io.github.cardinal.alpha.spring.example.reactive.entityUp.LogUpload;
import io.github.cardinal.alpha.spring.example.reactive.entityUp.FileUpload;
import io.github.cardinal.alpha.spring.example.reactive.mapping.type.RestEntityMapper;
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
@GenericComponent(typeParameters = {File.class, Integer.class, FileUpload.class, FileDownload.class})
@GenericComponent(typeParameters = {Log.class, Integer.class, LogUpload.class, Log.class})
public class EntityMappedCrudService<T extends Updatable<ID>, ID, UpT, DownT> implements CrudService<UpT, DownT, ID>{
    
    @Autowired
    private JpaRepository<T, ID> repository;
    
    @Autowired
    private RestEntityMapper<T, UpT, DownT> mapper;
    
    @Override
    public Mono<DownT> get(Mono<ID> id){
        return id.map(i-> mapper.mapDownload(repository.findById(i).get()));
    }
    
    @Override
    public Mono<Page<DownT>> list(Mono<Pageable> paginate){
        return paginate.map(p-> repository.findAll(p)
                                .map(e -> mapper.mapDownload(e)));
    }
    
    @Override
    public Mono<DownT> create(Mono<UpT> entity){
        return entity.map(mapper::mapUpload)
                    .map(e->{
                        if(e.getId() != null)
                            e.setId(null);
                        return e;
                    })
                    .map(repository::save)
                    .map(mapper::mapDownload);
    }
    
    @Override
    public Mono<DownT> update(Mono<ID> oldEntityId, Mono<UpT> entityUpdate){
        return oldEntityId.zipWith(entityUpdate, (i, u)-> new UpdateHolder<>(i, u))
                        .map(u->{
                            T oldEntity = repository.findById(u.getId()).get(),
                                update = mapper.mapUpload(u.getUpdate());
                            if(update.getId() != null)
                                update.setId(null);
                            mapper.updateEntity(update, oldEntity);
                            return oldEntity;
                        })
                        .map(repository::save)
                        .map(mapper::mapDownload);
    }
    
    @Override
    public Mono<ID>delete(Mono<ID> id){
        return id.map(i->{
                        repository.deleteById(i);
                        return i;
                    });
    }
    
}
