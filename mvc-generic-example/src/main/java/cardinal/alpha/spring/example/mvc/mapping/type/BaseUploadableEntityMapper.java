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
package cardinal.alpha.spring.example.mvc.mapping.type;

import cardinal.alpha.spring.example.mvc.entity.File;
import cardinal.alpha.spring.example.mvc.exception.MappingException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.engine.jdbc.BlobProxy;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
public abstract class BaseUploadableEntityMapper{
    
    protected String generateUrl(File src){
        try {
            if(src.getContent().length() > 0)
                return String.join("/", "/file", src.getId().toString());
        } catch (SQLException ex) {
            Logger.getLogger(BaseUploadableEntityMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    protected Blob extractFileContent(MultipartFile file){
        try{
            InputStream input = file.getInputStream();
            return BlobProxy.generateProxy(input, input.available());
        }catch(Throwable ex){
            throw new MappingException("Something wrong when transform file upload content to database blob", ex);
        }
    }
    
    @Mapping(source = "originalFilename", target = "name")
    @Mapping(source = "contentType", target = "mime")
    @Mapping(source = ".", target = "content")
    public abstract File mapUploadFile(MultipartFile file);
    
}
