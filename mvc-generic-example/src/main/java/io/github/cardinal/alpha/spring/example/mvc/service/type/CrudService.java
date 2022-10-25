/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.github.cardinal.alpha.spring.example.mvc.service.type;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
public interface CrudService<UpT, DownT, ID> {
    
    DownT get(ID id);
    
    Page<DownT> list(Pageable paginate);
    
    void create(UpT data);
    
    DownT update(ID oldEntityId, UpT update);
    
    void delete(ID id);
    
}
