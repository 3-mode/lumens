/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface TextConstants {
    public static final String SCHEMA_PATH = "SchemaPath";
    public static final String SCHEMA_ENCODING = "SchemaEncoding";

    // properities adding from wizard
    public static final String ENCODING = "Encoding";
    public static final String PATH = "Path";
    public static final String LINEDELIMITER = "LineDelimiter";
    public static final String FILEDELIMITER = "FiledDelimiter";
    public static final String ESCAPE_CHAR = "EscapeChar";
    public static final String QUOTE_CHAR = "EscapeChar";
    public static final String FILE_EXTENSION = "FileExtension";
    public static final String FILE_FILTER = "FileFilter";

    // connector options
    public static final String OPTION_MAXLINE = "MaxLine";
    public static final String OPTION_FORMAT_ASTITLE = "FormatAsTitle";
    public static final String OPTION_FIRST_LINE_ASTITLE = "FirstLineAsTitle";
    public static final String OPTION_IGNORE_READLINE_ERROR = "IgnoreReadlineError";
    public static final String OPTION_IGNORE_EMPTYLINE = "IgnoreEmptyLine";
    public static final String OPTION_TRIM_SPACE = "TrimSpace";

    // connector operation    
    public static final String OPERATION = "Operation";
    public static final String OPERATION_READ = "Read";
    public static final String OPERATION_APPEND = "Append";
    public static final String OPERATION_OVERWRITE = "OverWrite";

    // end use seen options
    public static final String FORMAT_PARAMS = "TextParams";
    public static final String FORMAT_MESSAGE = "TextMessage";
    public static final String FORMAT_FIELD = "field";
    public static final String FORMAT_NAME = "name";
    public static final String FORMAT_KEY = "key";
    public static final String FORMAT_TYPE = "type";
    public static final String FORMAT_NULLABLE = "nullable";
    public static final String FORMAT_PATTERN = "pattern";
    public static final String FORMAT_LENGTH = "length";
    public static final String FORMAT_COMMENT = "comment";
    public static final String FORMAT_PRECISION = "precision";
    public static final String FORMAT_FORMAT = "format";
}
