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
    private String regex;
    
    public LocalFileNameFilter(String filter){
        regex = filter.replaceAll("\\*", ".*");
    }
    
    public boolean accept(File dir, String name){     
         File fileOrDir = new File(dir + File.separator + name);
         if (!fileOrDir.isFile())
             return false;
         
         return name.matches(regex);        
     }
}
