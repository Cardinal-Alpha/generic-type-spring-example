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
package io.github.cardinal.alpha.spring.example.mvc.controller;

import io.github.cardinal.alpha.spring.example.mvc.entityDown.FileDownload;
import io.github.cardinal.alpha.spring.example.mvc.entityUp.FileUpload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import io.github.cardinal.alpha.spring.generic.bind.GenericRestController;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com> 
 */
@GenericRestController(typeParameters = {FileUpload.class, FileDownload.class, Integer.class}, path = "/api/file")
public class MultipartCrudController<UpT, DownT, ID> extends BaseCrudController<UpT, DownT, ID>{
    
    @Override
    @PostMapping("")
    public ResponseEntity<String> createHandler(@ModelAttribute UpT data){
        service.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
    
    @Override
    @PatchMapping("/{id}")
    public DownT updateHandler(@PathVariable ID id, @ModelAttribute UpT update){
        return service.update(id, update);
    }
    
}
