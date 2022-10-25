/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.github.cardinal.alpha.spring.example.reactive.repository;

import io.github.cardinal.alpha.spring.example.reactive.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer>{
    
}
