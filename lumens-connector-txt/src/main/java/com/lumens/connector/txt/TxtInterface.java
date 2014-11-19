/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.txt;

/**
 *
 * @author whiskey
 */

import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  

public interface TxtInterface {
    public void read(InputStream ins);    
    public void write(OutputStream ous);
}
