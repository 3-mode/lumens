/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.FormatPath = Class.$extend({
    __init__: function(strPath) {
        this.tokens = [];
        if (!strPath || strPath === "") {
            return;
        }
        var found = 0, index = 0, quoteFound = false;
        var length = strPath.length;
        while (index < length) {
            var c = strPath.charAt(index);
            if (c === '.' && !quoteFound) {
                if (index === 0 || index + 1 === length) {
                    throw "Error string path of format \"" + strPath + "\"";
                }
                this.tokens.push(this.removeQuote(strPath.substring(found, index)));
                found = index + 1;
            } else if (c === '\'') {
                quoteFound = !quoteFound;
            }
            ++index;
        }
        if (found < length && index === length) {
            this.tokens.push(this.removeQuote(strPath.substring(found, index)));
        }
    },
    tokenCount: function() {
        return this.tokens.length;
    },
    token: function(i) {
        return this.tokens[i];
    },
    removeQuote: function(strToken) {
        if (strToken.charAt(0) === '\'' && strToken.charAt(strToken.length - 1) === '\'') {
            return strToken.substring(1, strToken.length - 1);
        }
        return strToken;
    }
});