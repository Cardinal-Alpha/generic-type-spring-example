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
import cardinal.alpha.spring.example.reactive.entity.GeoTag;
import cardinal.alpha.spring.example.reactive.entity.Location;
import cardinal.alpha.spring.example.reactive.entityDown.FileDownload;
import cardinal.alpha.spring.example.reactive.entityDown.GeoTagDownload;
import cardinal.alpha.spring.example.reactive.entityUp.FileUpload;
import cardinal.alpha.spring.example.reactive.entityUp.GeoTagUpload;
import cardinal.alpha.spring.example.reactive.mapping.type.BaseUploadableEntityMapper;
import cardinal.alpha.spring.example.reactive.mapping.type.RestMapper;
import cardinal.alpha.spring.example.reactive.mapping.type.UpdateMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@Mapper(componentModel = "spring")
public abstract class GeoTagMapper extends BaseUploadableEntityMapper implements UpdateMapping<GeoTag>,
                                                                                    RestMapper<GeoTag, GeoTagUpload, GeoTagDownload>{
    
    @Autowired
    private UpdateMapping<Location> locationUpdater;
    
    @Autowired
    private UpdateMapping<File> fileUpdater;
    
    @Autowired
    private RestMapper<File, FileUpload, FileDownload> fileMapper;
    
    protected void locationTransfer(Location src, @MappingTarget Location dest){
        locationUpdater.updateEntity(src, dest);
    }
    
    protected void fileTransfer(File src, @MappingTarget File dest){
        fileUpdater.updateEntity(src, dest);
    }

    @Override
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
                    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "file", target = "file")
    @Mapping(source = "location", target = "location")
    public abstract void updateEntity(GeoTag updateData, @MappingTarget GeoTag oldEntity);
    
    protected FileDownload mapDownload(File src){
        return fileMapper.mapDownload(src);
    }
    
}
