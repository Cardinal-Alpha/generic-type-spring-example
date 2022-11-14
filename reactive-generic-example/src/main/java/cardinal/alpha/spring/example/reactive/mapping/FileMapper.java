package cardinal.alpha.spring.example.reactive.mapping;

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


import cardinal.alpha.spring.example.reactive.entity.File;
import cardinal.alpha.spring.example.reactive.entityDown.FileDownload;
import cardinal.alpha.spring.example.reactive.entityUp.FileUpload;
import cardinal.alpha.spring.example.reactive.mapping.type.BaseUploadableEntityMapper;
import cardinal.alpha.spring.example.reactive.mapping.type.RestMapper;
import cardinal.alpha.spring.example.reactive.mapping.type.UpdateMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@Mapper(componentModel = "spring")
public abstract class FileMapper extends BaseUploadableEntityMapper implements UpdateMapping<File>,
                                                                                    RestMapper<File, FileUpload, FileDownload>{

    @Override
    @Mapping(source = ".", target = "url")
    public abstract FileDownload mapDownload(File data);

    @Override
    public File mapUpload(FileUpload data){
        return mapUploadFilePart(data.getUpload());
    }
    
}
