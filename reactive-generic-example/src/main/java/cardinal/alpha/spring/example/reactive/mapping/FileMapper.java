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
package cardinal.alpha.spring.example.reactive.mapping;

import cardinal.alpha.spring.example.reactive.entity.File;
import cardinal.alpha.spring.example.reactive.entityDown.FileDownload;
import cardinal.alpha.spring.example.reactive.entityUp.FileUpload;
import cardinal.alpha.spring.example.reactive.exception.StreamException;
import cardinal.alpha.spring.example.reactive.mapping.type.RestEntityMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.IOUtils;
import org.hibernate.engine.jdbc.BlobProxy;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.http.codec.multipart.FilePart;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@Mapper(componentModel = "spring")
public abstract class FileMapper extends RestEntityMapper<File, FileUpload, FileDownload>{

    @Override
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
            final FilePart upload = src.getUpload();
            dest.setName(upload.filename());
            dest.setMime(Files.probeContentType(
                            Path.of(upload.filename()) )
            );
            final ByteArrayOutputStream holder = new ByteArrayOutputStream();
            upload.content()
                    .doOnEach(sig->{
                        try{
                            if(!sig.isOnComplete())
                                IOUtils.copy(sig.get().asInputStream(), holder);
                            else{
                                ByteArrayInputStream content = new ByteArrayInputStream(holder.toByteArray());
                                dest.setContent(BlobProxy.generateProxy(content, content.available()));
                            }
                        }catch(Throwable ex){
                            throw new StreamException("Something wrong when upload file", ex);
                        }
                    })
                    .subscribe();
        }catch(Throwable ex){
            throw new StreamException("Something wrong when upload file", ex);
        }
    }
    
}
