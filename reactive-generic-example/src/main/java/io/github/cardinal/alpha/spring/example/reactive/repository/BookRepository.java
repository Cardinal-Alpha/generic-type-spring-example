/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.github.cardinal.alpha.spring.example.reactive.repository;

import io.github.cardinal.alpha.spring.example.reactive.entity.Book;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
    
}
