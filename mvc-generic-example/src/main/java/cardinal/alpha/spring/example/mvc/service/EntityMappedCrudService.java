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

import cardinal.alpha.spring.example.mvc.entity.File;
import cardinal.alpha.spring.example.mvc.entity.Log;
import cardinal.alpha.spring.example.mvc.entity.type.Updatable;
import cardinal.alpha.spring.example.mvc.entityDown.FileDownload;
import cardinal.alpha.spring.example.mvc.entityUp.LogUpload;
import cardinal.alpha.spring.example.mvc.entityUp.FileUpload;
import cardinal.alpha.spring.example.mvc.mapping.type.RestMapper;
import cardinal.alpha.spring.example.mvc.service.type.CrudService;
import cardinal.alpha.spring.generic.bind.GenericComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@GenericComponent(typeParameters = {File.class, Integer.class, FileUpload.class, FileDownload.class})
@GenericComponent(typeParameters = {Log.class, Integer.class, LogUpload.class, Log.class})
public class EntityMappedCrudService<T extends Updatable<ID>, ID, UpT, DownT> implements CrudService<UpT, DownT, ID>{
    
    @Autowired
    protected BasicEntityCrudService<T, ID> basicSvc;
    
    @Autowired
    protected RestMapper<T, UpT, DownT> mapper;

    @Override
    public DownT get(ID id) {
        return mapper.mapDownload(basicSvc.get(id));
    }

    @Override
    public Page<DownT> list(Pageable paginate) {
        return basicSvc.list(paginate)
                    .map(e -> mapper.mapDownload(e));
    }

    @Override
    public void create(UpT data) {
        basicSvc.create(mapper.mapUpload(data));
    }

    @Override
    public DownT update(ID oldEntityId, UpT update) {
        return mapper.mapDownload( 
                basicSvc.update(
                        oldEntityId,
                        mapper.mapUpload(update) )
        );
    }

    @Override
    public void delete(ID id) {
        basicSvc.delete(id);
    }
    
}
