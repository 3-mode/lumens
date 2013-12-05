/*
 * (C) Copyright Hewlett-Packard Development Company, L.P. All Rights Reserved.
 */
package com.lumens.processor.script;

import com.lumens.model.AccessPath;
import com.lumens.model.Element;
import com.lumens.model.Path;
import com.lumens.model.Type;
import com.lumens.processor.Context;
import com.lumens.processor.Script;

// TODO need to refine the package path, if it is correct to put the class into
// transform package
public class AccessPathScript implements Script {

    private Path path;

    public AccessPathScript(String script) {
        path = new AccessPath(script);
    }

    @Override
    public Object execute(Context ctx) {
        Element searchEntry = ctx.getAccessPathEntry();
        Path currentPath;
        if (searchEntry.getLevel() > 0)
            currentPath = path.right(path.tokenCount() - searchEntry.getLevel());
        else
            currentPath = path;
        Element find = searchEntry.getChildByPath(currentPath);
        if (find.getFormat().getType() != Type.NONE && find.getValue() != null)
            return find.getValue().get();

        return null;
    }
}
