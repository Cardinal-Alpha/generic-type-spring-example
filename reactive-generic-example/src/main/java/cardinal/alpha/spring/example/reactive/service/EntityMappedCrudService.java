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
package cardinal.alpha.spring.example.reactive.service;

import cardinal.alpha.spring.example.reactive.entity.File;
import cardinal.alpha.spring.example.reactive.entity.GeoTag;
import cardinal.alpha.spring.example.reactive.entity.Log;
import cardinal.alpha.spring.example.reactive.entity.type.Updatable;
import cardinal.alpha.spring.example.reactive.entityDown.FileDownload;
import cardinal.alpha.spring.example.reactive.entityDown.GeoTagDownload;
import cardinal.alpha.spring.example.reactive.entityUp.LogUpload;
import cardinal.alpha.spring.example.reactive.entityUp.FileUpload;
import cardinal.alpha.spring.example.reactive.entityUp.GeoTagUpload;
import cardinal.alpha.spring.example.reactive.mapping.type.RestMapper;
import cardinal.alpha.spring.example.reactive.service.type.CrudService;
import cardinal.alpha.spring.generic.bind.GenericComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@GenericComponent(typeParameters = {File.class, Integer.class, FileUpload.class, FileDownload.class})
@GenericComponent(typeParameters = {Log.class, Integer.class, LogUpload.class, Log.class})
@GenericComponent(typeParameters = {GeoTag.class, Integer.class, GeoTagUpload.class, GeoTagDownload.class})
public class EntityMappedCrudService<T extends Updatable<ID>, ID, UpT, DownT> implements CrudService<UpT, DownT, ID>{
    
    @Autowired
    private BasicEntityCrudService<T, ID> svc;
    
    @Autowired
    private RestMapper<T, UpT, DownT> mapper;
    
    @Override
    public Mono<DownT> get(Mono<ID> id){
        return svc.get(id)
                    .map(mapper::mapDownload);
    }
    
    @Override
    public Mono<Page<DownT>> list(Mono<Pageable> paginate){
        return svc.list(paginate)
                    .map(p -> p.map(mapper::mapDownload)
                    );
    }
    
    @Override
    public Mono<DownT> create(Mono<UpT> entity){
        return svc.create(entity.map(mapper::mapUpload)
                            .map(e->{
                                if(e.getId() != null)
                                    e.setId(null);
                                return e;
                            }))
                    .map(mapper::mapDownload);
    }
    
    @Override
    public Mono<DownT> update(Mono<ID> oldEntityId, Mono<UpT> entityUpdate){
        return svc.update(oldEntityId, entityUpdate.map(mapper::mapUpload))
                    .map(mapper::mapDownload);
    }
    
    @Override
    public Mono<ID>delete(Mono<ID> id){
        return svc.delete(id);
    }
    
}
