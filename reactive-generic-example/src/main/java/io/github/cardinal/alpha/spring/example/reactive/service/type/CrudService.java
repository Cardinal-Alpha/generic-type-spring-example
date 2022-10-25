/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.github.cardinal.alpha.spring.example.reactive.service.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
public interface CrudService<UpT, DownT, ID> {
    
    @Data
    @AllArgsConstructor
    public static class UpdateHolder<ID, UpT>{
        private ID id;
        private UpT update;
    }
    
    @Data
    @AllArgsConstructor
    public static class Update<T>{
        private T oldData, newData;
    }
    
    Mono<DownT> get(Mono<ID> id);
    
    Mono<Page<DownT>> list(Mono<Pageable> paginate);
    
    Mono<DownT> create(Mono<UpT> data);
    
    Mono<DownT> update(Mono<ID> oldEntityId, Mono<UpT> update);
    
    Mono<ID> delete(Mono<ID> id);
    
}
