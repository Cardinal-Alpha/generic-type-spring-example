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
package io.github.cardinal.alpha.spring.example.reactive.mapping.type;

import io.github.cardinal.alpha.spring.example.reactive.exception.MappingException;
import io.github.cardinal.alpha.spring.example.reactive.mapping.type.UpdateMapping;
import java.io.InputStream;
import java.sql.Blob;
import org.hibernate.engine.jdbc.BlobProxy;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
public abstract class BaseEntityMapper<T> implements UpdateMapping<T>{

    @Override
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
                    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(T updateData, @MappingTarget T oldEntity);
    
    protected Blob extractFileContent(MultipartFile file){
        try{
            InputStream input = file.getInputStream();
            return BlobProxy.generateProxy(input, input.available());
        }catch(Throwable ex){
            throw new MappingException("Something wrong when transform file upload content to database blob", ex);
        }
    }
    
}
