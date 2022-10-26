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
package io.cardinal.alpha.spring.example.mvc.service;

import io.cardinal.alpha.spring.example.mvc.entity.File;
import io.cardinal.alpha.spring.example.mvc.entity.Log;
import io.cardinal.alpha.spring.example.mvc.entity.type.Updatable;
import io.cardinal.alpha.spring.example.mvc.entityDown.FileDownload;
import io.cardinal.alpha.spring.example.mvc.entityUp.LogUpload;
import io.cardinal.alpha.spring.example.mvc.entityUp.FileUpload;
import io.cardinal.alpha.spring.example.mvc.mapping.type.RestEntityMapper;
import io.cardinal.alpha.spring.example.mvc.service.type.CrudService;
import io.cardinal.alpha.spring.generic.bind.GenericComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
    public DownT get(ID id){
        return mapper.mapDownload(repository.findById(id).get());
    }
    
    @Override
    public Page<DownT> list(Pageable paginate){
        return repository.findAll(paginate)
                        .map(e -> mapper.mapDownload(e));
    }
    
    @Override
    public void create(UpT data){
        repository.save(mapper.mapUpload(data));
    }
    
    @Override
    public DownT update(ID oldEntityId, UpT update){
        T oldEntity = repository.findById(oldEntityId).get(),
                entityUpdate = mapper.mapUpload(update);
        if(entityUpdate.getId() != null)
            entityUpdate.setId(null);
        mapper.updateEntity(entityUpdate, oldEntity);
        return mapper.mapDownload(
                repository.save(oldEntity));
    }
    
    @Override
    public void delete(ID id){
        repository.deleteById(id);
    }
    
}
