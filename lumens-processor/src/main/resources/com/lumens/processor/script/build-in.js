var ScriptUtils = com.lumens.processor.script.ScriptUtils;
var SimpleDateFormat = java.text.SimpleDateFormat;
var Calendar = java.util.Calendar;
var System = java.lang.System;
var Timestamp = java.sql.Timestamp;

/*Get the value by the current path*/
function getElementValue(ctx, path) {
    return ScriptUtils.getElement(ctx, path);
}

function $LogInfo(message) {
    // TODO need log4j here
    System.out.println(message);
}

/*Get the current date*/
function $Now() {
    var cal = Calendar.getInstance();
    return cal.getTime();
}

/*Format a date to string*/
function $DateToString(date, format) {
    var dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
}

/*Format a string to data*/
function $StringToDate(string, format) {
    var dateFormat = new SimpleDateFormat(format);
    return dateFormat.parse(string);
}

function $GetCarrier(ctx, name) {
    return ScriptUtils.getCarrier(ctx, name);
}

function $SetCarrier(ctx, name, value) {
    ScriptUtils.setCarrier(ctx, name, value);
}

function $RemoveCarrier(ctx, name) {
    return ScriptUtils.removeCarrier(ctx, name);
}