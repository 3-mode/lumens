/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.io;
import java.io.File;  
import java.io.FilenameFilter;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LocalFileNameFilter implements FilenameFilter{
    private String extension;
    
    public LocalFileNameFilter(String ext){
        extension = ext;
    }
    
    public boolean accept(File dir, String name){         
         String[] arrExt = name.split("\\.");
         return arrExt[1].equalsIgnoreCase(extension);         
     }
}
