package weblogic.diagnostics.instrumentation;

public interface InstrumentationConstants {
   String WEBLOGIC_DIAGNOSTIC_INSTRUMENTATION_PROPERTY = "weblogic.diagnostics.instrumentation";
   String SUFFIX = ".class";
   int BUFSIZE = 8192;
   String INSTRUMENTATION_LIBRARY_NAME = "diagnostic-instrumentation.jar";
   String DYE_INJECTION_MONITOR_NAME = "DyeInjection";
   String DIAGNOSTIC_MONITOR_PACKAGE = "com.bea.aspects.j2ee.";
   String GET_MONITOR_INSTANCE_METHOD_NAME = "aspectOf";
   String WLDF_DYE_COOKIE_NAME = "weblogic.diagnostics.dye";
   long DEFAULT_EVENT_ARCHIVAL_INTERVAL = 5000L;
   long MINIMUM_EVENT_ARCHIVAL_INTERVAL = 1000L;
   String IMAGE_SOURCE_NAME = "InstrumentationImageSource";
}
