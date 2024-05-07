package weblogic.management.mbeanservers;

import java.security.PrivilegedActionException;
import java.util.Locale;
import weblogic.management.mbeanservers.internal.JMXContextInterceptor;

public final class JMXContextUtil {
   public static Locale getLocale() throws PrivilegedActionException {
      return JMXContextInterceptor.getThreadLocale();
   }
}
