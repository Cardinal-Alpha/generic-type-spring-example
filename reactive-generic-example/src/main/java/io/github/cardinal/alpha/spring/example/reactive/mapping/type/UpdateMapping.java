/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.github.cardinal.alpha.spring.example.reactive.mapping.type;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
public interface UpdateMapping<T> {
    
    void updateEntity(T updateData, T oldEntity);
    
}
