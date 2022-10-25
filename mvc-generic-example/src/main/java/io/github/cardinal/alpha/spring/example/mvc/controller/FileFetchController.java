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
package io.github.cardinal.alpha.spring.example.mvc.controller;

import io.github.cardinal.alpha.spring.example.mvc.entity.File;
import io.github.cardinal.alpha.spring.example.mvc.exception.StreamException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import io.github.cardinal.alpha.spring.example.mvc.repository.FileRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@Controller
@RequestMapping("${app.filePrefix}")
public class FileFetchController {
    
    @Autowired
    private FileRepository repository;
    
    @GetMapping("/{id}")
    public ResponseEntity<StreamingResponseBody> showFile(@PathVariable Integer id){
        final File file = repository.findById(id).get();
        return ResponseEntity.ok()
                            .contentType(MediaType.valueOf(file.getMime()))
                            .body(outStream->{
                                try{
                                    IOUtils.copy(file.getContent().getBinaryStream(), outStream);
                                }catch(Throwable ex){
                                    throw new StreamException("Something wrong when stream file to output", ex);
                                }
                            });
    }
    
    @GetMapping("/{id}/download")
    public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable Integer id){
        final File file = repository.findById(id).get();
        return ResponseEntity.ok()
                            .contentType(MediaType.valueOf(file.getMime()))
                            .header("Content-Disposition", String.join(";", 
                                                            "attachment", 
                                                            String.format("filename=\"%s\"", file.getName()) )
                            )
                            .body(outStream->{
                                try{
                                    IOUtils.copy(file.getContent().getBinaryStream(), outStream);
                                }catch(Throwable ex){
                                    throw new StreamException("Something wrong when stream file to output", ex);
                                }
                            });
    }
    
}
