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

import io.cardinal.alpha.spring.example.reactive.exception.StreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.cardinal.alpha.spring.example.reactive.repository.FileRepository;
import java.io.OutputStream;
import java.sql.Blob;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@Controller
@RequestMapping("${app.filePrefix}")
public class FileFetchController {
    
    @Autowired
    private FileRepository repository;
    
    private DefaultDataBufferFactory bufferFactory = new DefaultDataBufferFactory();
    
    private void handleStream(Blob input, OutputStream output){
        try{
            IOUtils.copy(input.getBinaryStream(), output);
        }catch(Throwable ex){
            throw new StreamException("Something wrong when stream file download", ex);
        }
    }
    
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataBuffer>> showFile(@PathVariable Integer id){
        return Mono.just(repository.findById(id).get())
                    .map(file->{
                        DataBuffer buffer = bufferFactory.allocateBuffer();
                        handleStream(file.getContent(), buffer.asOutputStream());
                        return ResponseEntity.ok()
                                    .contentType(MediaType.valueOf(file.getMime()))
                                    .body(buffer);
                    });
    }
    
    @GetMapping("/{id}/download")
    public Mono<ResponseEntity<DataBuffer>> downloadFile(@PathVariable Integer id){
        return Mono.just(repository.findById(id).get())
                    .map(file->{
                        DataBuffer buffer = bufferFactory.allocateBuffer();
                        handleStream(file.getContent(), buffer.asOutputStream());
                        return ResponseEntity.ok()
                                    .headers(head->{
                                        head.add("Content-Disposition", String.join(";", 
                                                            "attachment", 
                                                            String.format("filename=\"%s\"", file.getName()))
                                        );
                                    })
                                    .contentType(MediaType.valueOf(file.getMime()))
                                    .body(buffer);
                    });
    }
    
}
