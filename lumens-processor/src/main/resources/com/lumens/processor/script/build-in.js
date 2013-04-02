var ScriptUtils = com.lumens.processor.script.ScriptUtils;
var SimpleDateFormat = java.text.SimpleDateFormat;
var Calendar = java.util.Calendar;

/*Get the value by the current path*/
function getElementValue(ctx, path) {
  return ScriptUtils.getElement(ctx, path);
}

/*Get the current date*/
function now() {
  var cal = Calendar.getInstance();
  return cal.getTime();
}

/*Format a date to string*/
function dateFormat(date, format) {
  var dateFormat = new SimpleDateFormat(format);
  return dateFormat.format(date);
}