var ScriptUtils = com.lumens.processor.script.ScriptUtils;
var SimpleDateFormat = java.text.SimpleDateFormat;
var Calendar = java.util.Calendar;
var System = java.lang.System;

/*Get the value by the current path*/
function getElementValue(ctx, path) {
    return ScriptUtils.getElement(ctx, path);
}

function logInfo(message) {
    // TODO need log4j here
    System.out.println(message);
}

/*Get the current date*/
function now() {
    var cal = Calendar.getInstance();
    return cal.getTime();
}

/*Format a date to string*/
function dateToString(date, format) {
    var dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
}

/*Format a string to data*/
function stringToDate(string, format) {
    var dateFormat = new SimpleDateFormat(format);
    return dateFormat.parse(string);
}