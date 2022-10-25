/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.github.cardinal.alpha.spring.example.mvc.mapping.type;

/**
 *
 * @author Cardinal Alpha <renaldi96.aldi@gmail.com>
 */
public interface RestMapper<EntityT, UploadT, DownloadT> {
    
    EntityT mapUpload(UploadT data);
    DownloadT mapDownload(EntityT data);
    
}
