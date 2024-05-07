package weblogic.management.runtime;

/** @deprecated */
public interface ServletSessionRuntimeMBean extends RuntimeMBean {
   /** @deprecated */
   void invalidate();

   /** @deprecated */
   long getTimeLastAccessed();

   /** @deprecated */
   long getMaxInactiveInterval();

   /** @deprecated */
   String getMainAttribute();
}
