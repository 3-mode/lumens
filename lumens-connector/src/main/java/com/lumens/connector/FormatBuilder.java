/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.connector;

import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface FormatBuilder
{
    public Map<String, Format> getFormatList(Direction direction);

    public Format getFormat(Format format, String path, Direction direction);
}