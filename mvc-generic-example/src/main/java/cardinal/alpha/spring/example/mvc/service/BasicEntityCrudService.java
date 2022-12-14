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
package cardinal.alpha.spring.example.mvc.service;

import cardinal.alpha.spring.example.mvc.entity.*;
import cardinal.alpha.spring.example.mvc.entity.type.Updatable;
import cardinal.alpha.spring.example.mvc.mapping.type.UpdateMapping;
import cardinal.alpha.spring.example.mvc.service.type.CrudService;
import cardinal.alpha.spring.generic.bind.GenericComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@GenericComponent(typeParameters = {Book.class, Integer.class})
@GenericComponent(typeParameters = {Profile.class, Integer.class})
@GenericComponent(typeParameters = {File.class, Integer.class})
@GenericComponent(typeParameters = {Log.class, Integer.class})
@GenericComponent(typeParameters = {GeoTag.class, Integer.class})
public class BasicEntityCrudService<T extends Updatable<ID>, ID> implements CrudService<T, T, ID>{
    
    @Autowired
    private JpaRepository<T, ID> repository;
    
    @Autowired
    private UpdateMapping<T> updater;
    
    
    @Override
    public T get(ID id){
        return repository.findById(id).get();
    }
    
    @Override
    public Page<T> list(Pageable paginate){
        return repository.findAll(paginate);
    }
    
    @Override
    public void create(T entity){
        if(entity.getId() != null)
            entity.setId(null);
        repository.save(entity);
    }
    
    @Override
    public T update(ID oldEntityId, T entityUpdate){
        T oldEntity = repository.findById(oldEntityId).get();
        if(entityUpdate.getId() != null)
            entityUpdate.setId(null);
        updater.updateEntity(entityUpdate, oldEntity);
        return repository.save(oldEntity);
    }
    
    @Override
    public void delete(ID id){
        repository.deleteById(id);
    }
    
}
