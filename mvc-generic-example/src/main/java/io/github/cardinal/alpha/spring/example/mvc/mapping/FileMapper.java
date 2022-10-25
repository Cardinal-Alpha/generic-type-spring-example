/*
 * The MIT License
 *
 * Copyright 2022 Ryuuji.
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
package io.github.cardinal.alpha.spring.example.mvc.mapping;

import io.github.cardinal.alpha.spring.example.mvc.entity.File;
import io.github.cardinal.alpha.spring.example.mvc.entityDown.FileDownload;
import io.github.cardinal.alpha.spring.example.mvc.entityUp.FileUpload;
import io.github.cardinal.alpha.spring.example.mvc.exception.StreamException;
import io.github.cardinal.alpha.spring.example.mvc.mapping.type.RestEntityMapper;
import org.hibernate.engine.jdbc.BlobProxy;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@Mapper(componentModel = "spring")
public abstract class FileMapper extends RestEntityMapper<File, FileUpload, FileDownload>{

    @Override
    @Mapping(source = "upload.originalFilename", target = "name")
    @Mapping(source = "upload.contentType", target = "mime")
    public abstract File mapUpload(FileUpload data);
    
    @AfterMapping
    protected void postDownMap(File src, @MappingTarget FileDownload dest){
        dest.setUrl(
                String.join("/", "/file", src.getId().toString())
        );
    }
    
    @AfterMapping
    protected void postUpload(FileUpload src, @MappingTarget File dest){
        try{
            MultipartFile upload = src.getUpload();
            dest.setContent(BlobProxy.generateProxy(
                                        upload.getInputStream(),
                                        upload.getSize() )
            );
        }catch(Throwable ex){
            throw new StreamException("Something wrong when upload file", ex);
        }
    }
    
}
