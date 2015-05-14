/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
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

    private final Path path;

    public AccessPathScript(String script) {
        path = new AccessPath(script);
    }

    @Override
    public Object execute(Context ctx) {
        Element srcElement = null;
        Element startSrcElement = ScriptUtils.getStartElement(ctx);
        Path startfullPath = startSrcElement.getFormat().getFullPath();
        if (path.toString().startsWith(startfullPath.toString())) {
            srcElement = startSrcElement;
        } else {
            Element rootSrcElement = ctx.getRootSourceElement();
            Path rootFullPath = rootSrcElement.getFormat().getFullPath();
            if (path.toString().startsWith(rootFullPath.toString()))
                srcElement = rootSrcElement;
            else
                throw new RuntimeException(String.format("Wrong path '%s'", path));
        }
        Path currentPath = path.removeLeft(srcElement.getLevel() + 1);
        Element find = srcElement.getChildByPath(currentPath);
        if (find.getFormat().getType() != Type.NONE && find.getValue() != null)
            return find.getValue().get();

        return null;
    }

    @Override
    public String getScriptText() {
        return path.toString();
    }
}
