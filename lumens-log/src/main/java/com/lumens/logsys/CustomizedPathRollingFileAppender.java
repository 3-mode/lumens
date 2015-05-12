package com.lumens.logsys;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;

/**
 * Porting the RollingFile appender to customized appender in order to use the customized log path
 */
@Plugin(name = "CustomizedPathRollingFile", category = "Core", elementType = "appender", printObject = true)
public final class CustomizedPathRollingFileAppender extends AbstractOutputStreamAppender<RollingFileManager> {

    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final long serialVersionUID = 1L;
    private final String fileName;
    private final String filePattern;
    private Object advertisement;
    private final Advertiser advertiser;

    private CustomizedPathRollingFileAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter,
                                       final RollingFileManager manager, final String fileName, final String filePattern,
                                       final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
        if (advertiser != null) {
            final Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            advertisement = advertiser.advertise(configuration);
        }
        this.fileName = fileName;
        this.filePattern = filePattern;
        this.advertiser = advertiser;
    }

    @Override
    public void stop() {
        super.stop();
        if (advertiser != null) {
            advertiser.unadvertise(advertisement);
        }
    }

    /**
     * Write the log entry rolling over the file when required.
     *
     * @param event The LogEvent.
     */
    @Override
    public void append(final LogEvent event) {
        getManager().checkRollover(event);
        super.append(event);
    }

    /**
     * Returns the File name for the Appender.
     *
     * @return The file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the file pattern used when rolling over.
     *
     * @return The file pattern.
     */
    public String getFilePattern() {
        return filePattern;
    }

    /**
     * Create a RollingFileAppender.
     *
     * @param fileName The name of the file that is actively written to. (required).
     * @param filePattern The pattern of the file name to use on rollover. (required).
     * @param append If true, events are appended to the file. If false, the file is overwritten
     * when opened. Defaults to "true"
     * @param name The name of the Appender (required).
     * @param bufferedIO When true, I/O will be buffered. Defaults to "true".
     * @param bufferSizeStr buffer size for buffered IO (default is 8192).
     * @param immediateFlush When true, events are immediately flushed. Defaults to "true".
     * @param policy The triggering policy. (required).
     * @param strategy The rollover strategy. Defaults to DefaultRolloverStrategy.
     * @param layout The layout to use (defaults to the default PatternLayout).
     * @param filter The Filter or null.
     * @param ignore If {@code "true"} (default) exceptions encountered when appending events are
     * logged; otherwise they are propagated to the caller.
     * @param advertise "true" if the appender configuration should be advertised, "false"
     * otherwise.
     * @param advertiseURI The advertised URI which can be used to retrieve the file contents.
     * @param config The Configuration.
     * @return A RollingFileAppender.
     */
    @PluginFactory
    public static CustomizedPathRollingFileAppender createAppender(
    @PluginAttribute("fileName") final String fileName,
    @PluginAttribute("filePattern") final String filePattern,
    @PluginAttribute("append") final String append,
    @PluginAttribute("name") final String name,
    @PluginAttribute("bufferedIO") final String bufferedIO,
    @PluginAttribute("bufferSize") final String bufferSizeStr,
    @PluginAttribute("immediateFlush") final String immediateFlush,
    @PluginElement("Policy") final TriggeringPolicy policy,
    @PluginElement("Strategy") RolloverStrategy strategy,
    @PluginElement("Layout") Layout<? extends Serializable> layout,
    @PluginElement("Filter") final Filter filter,
    @PluginAttribute("ignoreExceptions") final String ignore,
    @PluginAttribute("advertise") final String advertise,
    @PluginAttribute("advertiseURI") final String advertiseURI,
    @PluginConfiguration final Configuration config) {
        String newFileName = fileName;
        String newFilePattern = filePattern;
        String base = System.getProperty("lumens.base");
        if (base != null && !base.isEmpty()) {
            if (!base.endsWith(File.separator))
                base = base + File.separator;
            newFileName = base + fileName;
            newFilePattern = base + filePattern;
            System.out.println("new file name is '" + newFileName + "', '" + newFilePattern + "'");
        }

        final boolean isAppend = Booleans.parseBoolean(append, true);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final boolean isBuffered = Booleans.parseBoolean(bufferedIO, true);
        final boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        final int bufferSize = Integers.parseInt(bufferSizeStr, DEFAULT_BUFFER_SIZE);
        if (!isBuffered && bufferSize > 0) {
            LOGGER.warn("The bufferSize is set to {} but bufferedIO is not true: {}", bufferSize, bufferedIO);
        }
        if (name == null) {
            LOGGER.error("No name provided for FileAppender");
            return null;
        }

        if (fileName == null) {
            LOGGER.error("No filename was provided for FileAppender with name " + name);
            return null;
        }

        if (filePattern == null) {
            LOGGER.error("No filename pattern provided for FileAppender with name " + name);
            return null;
        }

        if (policy == null) {
            LOGGER.error("A TriggeringPolicy must be provided");
            return null;
        }

        if (strategy == null) {
            strategy = DefaultRolloverStrategy.createStrategy(null, null, null,
                                                              String.valueOf(Deflater.DEFAULT_COMPRESSION), config);
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        final RollingFileManager manager = RollingFileManager.getFileManager(newFileName, newFilePattern, isAppend,
                                                                             isBuffered, policy, strategy, advertiseURI, layout, bufferSize);
        if (manager == null) {
            return null;
        }

        return new CustomizedPathRollingFileAppender(name, layout, filter, manager, newFileName, newFilePattern,
                                              ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
    }
}
