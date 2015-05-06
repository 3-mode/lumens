/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.master;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformEngineMaster {
    

    public static void main(String[] args) {
        try {
            Options options = new Options();
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);
        } catch (ParseException ex) {
        }
    }
}
