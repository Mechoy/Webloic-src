package weblogic.wsee.security.configuration;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.configuration.WebserviceTimestampMBean;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.TimestampHandler;
import weblogic.xml.crypto.wss.api.Timestamp;

public class TimestampConfiguration implements TimestampHandler, BeanUpdateListener {
   public static final String DEBUG_PROPERTY = "weblogic.xml.crypto.wss.debug";
   public static final boolean DEBUG = Boolean.getBoolean("weblogic.xml.crypto.wss.debug");
   public static final String VERBOSE_PROPERTY = "weblogic.xml.crypto.wss.verbose";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.xml.crypto.wss.verbose");
   public static final String ASSUME_SYNCHRONIZED_CLOCKS = "weblogic.wsee.security.clock.synchronized";
   public static final String CLOCK_PRECISION = "weblogic.wsee.security.clock.precision";
   private boolean useClockSkew;
   public static final String CLOCK_SKEW = "weblogic.wsee.security.clock.skew";
   public static final String LAX_CLOCK_PRECISION = "weblogic.wsee.security.clock.precision.lax";
   public static final String MAX_PROCESSING_DELAY = "weblogic.wsee.security.delay.max";
   public static final String VALIDITY_PERIOD = "weblogic.wsee.security.validity";
   public static final int DEFAULT_VALIDITY = 60;
   public static final long MILLI_PRECISION = 1L;
   public static final long SECOND_PRECISION = 1000L;
   public static final long MINUTE_PRECISION = 60000L;
   public static final long HOUR_PRECISION = 3600000L;
   public static final long DAY_PRECISION = 86400000L;
   public static final long WEEK_PRECISION = 604800000L;
   public static final long MONTH_PRECISION = 2592000000L;
   public static final long YEAR_PRECISION = 31536000000L;
   public static final long DEFAULT_CLOCK_PRECISION = 60000L;
   public static final long DEFAULT_CLOCK_SKEW = 60000L;
   public static final int NO_MAX_PROCESSING_DELAY = -1;
   private boolean clockSynchronized;
   private long clockPrecision;
   private long clockSkew;
   private boolean laxPrecision;
   private long maxProcessingDelay;
   private int validityPeriod;

   public TimestampConfiguration() {
      this((WebserviceTimestampMBean)null);
   }

   public TimestampConfiguration(WebserviceTimestampMBean var1) {
      this.useClockSkew = true;
      this.clockSynchronized = true;
      this.clockPrecision = 60000L;
      this.clockSkew = 60000L;
      this.laxPrecision = false;
      this.maxProcessingDelay = -1L;
      this.validityPeriod = 60;
      this.init(var1);
      if (var1 != null) {
         var1.addBeanUpdateListener(this);
      }

   }

   public void checkExpiration(Calendar var1, Calendar var2) {
      GregorianCalendar var3 = new GregorianCalendar();
      this.checkExpiration(var3, var1, var2, -1);
   }

   public void checkExpiration(Calendar var1, Calendar var2, int var3) {
      GregorianCalendar var4 = new GregorianCalendar();
      this.checkExpiration(var4, var1, var2, var3);
   }

   public void checkExpiration(Calendar var1, Calendar var2, Calendar var3) {
      this.checkExpiration(var1, var2, var3, -1);
   }

   public void checkExpiration(Calendar var1, Calendar var2, Calendar var3, int var4) {
      long var5 = var1.getTimeInMillis();
      if (var2 != null) {
         long var7 = var2.getTimeInMillis();
         long var9;
         if (var3 != null) {
            var9 = var3.getTimeInMillis();
         } else {
            var9 = 0L;
         }

         LogUtils.debugWss("*************TIMESTAMP VALIDATION: ");
         LogUtils.debugWss("created = " + var2.getTime());
         LogUtils.debugWss("now = " + var1.getTime());
         if (var3 != null) {
            LogUtils.debugWss("expiry = " + var3.getTime());
         } else {
            LogUtils.debugWss("expiry not set");
         }

         LogUtils.debugWss("longCreated = " + var7);
         LogUtils.debugWss("longNow = " + var5);
         LogUtils.debugWss("clockSkew used = " + this.getClockSkew());
         LogUtils.debugWss("clockSkew = " + this.clockSkew);
         LogUtils.debugWss("clockPrecision = " + this.clockPrecision);
         LogUtils.debugWss("useClockSkew = " + this.useClockSkew);
         LogUtils.debugWss("clockSynchronized = " + this.clockSynchronized);
         LogUtils.debugWss("maxProcessingDelay = " + this.maxProcessingDelay);
         LogUtils.logWss("TIMESTAMP VALIDATION: ");
         LogUtils.logWss("created = " + var2.getTime());
         LogUtils.logWss("now = " + var1.getTime());
         if (var3 != null) {
            LogUtils.logWss("expiry = " + var3.getTime());
         } else {
            LogUtils.logWss("expiry not set");
         }

         LogUtils.logWss("clockSkew used = " + this.getClockSkew());
         if (!this.clockSynchronized) {
            if (var3 != null) {
               throw new SOAPFaultException(EXPIRED_FAULTCODE, "Message includes expiry but clocks are not synchronized", (String)null, (Detail)null);
            }
         } else {
            if (this.isFreshnessEnforced()) {
               long var11 = var7 + this.getClockSkew() + this.maxProcessingDelay;
               if (var11 < var5) {
                  throw new SOAPFaultException(EXPIRED_FAULTCODE, "Message is too old", (String)null, (Detail)null);
               }
            }

            if (var5 + this.getClockSkew() < var7) {
               throw new SOAPFaultException(EXPIRED_FAULTCODE, "Message Created time past the current time even accounting for set clock skew", (String)null, (Detail)null);
            }

            if (var4 == -9999) {
               LogUtils.logWss("Bypass timeage check due to message age set to = " + var4);
               return;
            }

            if (var3 != null && var9 + this.getClockSkew() < var5) {
               throw new SOAPFaultException(EXPIRED_FAULTCODE, "Message Expires time has passed", (String)null, (Detail)null);
            }

            if (var4 >= 0 && var7 + (long)(var4 * 1000) + this.getClockSkew() < var5) {
               throw new SOAPFaultException(EXPIRED_FAULTCODE, "Message older than allowed MessageAge", (String)null, (Detail)null);
            }
         }

      }
   }

   private void init(WebserviceTimestampMBean var1) {
      String var2;
      if (DEBUG && var1 != null) {
         var2 = var1.getName();
         LogUtils.debugWss("TimestampConfiguration.init");
         LogUtils.debugWss("name = " + var2);
         if (var1.getParent() != null) {
            LogUtils.debugWss("wtm.parent.name = " + var1.getParent().getName());
         }
      }

      var2 = System.getProperty("weblogic.wsee.security.clock.synchronized");
      if (var2 != null) {
         this.clockSynchronized = Boolean.parseBoolean(var2);
      } else if (var1 != null) {
         this.clockSynchronized = var1.isClockSynchronized();
      }

      boolean var3 = true;
      boolean var4 = true;
      var2 = System.getProperty("weblogic.wsee.security.clock.precision");
      if (var2 != null) {
         this.clockPrecision = Long.parseLong(var2);
      } else if (var1 != null) {
         this.clockPrecision = var1.getClockPrecision();
         if (!var1.isSet("ClockPrecision")) {
            var3 = false;
         }
      } else {
         var3 = false;
      }

      var2 = System.getProperty("weblogic.wsee.security.clock.skew");
      if (var2 != null) {
         this.clockSkew = Long.parseLong(var2);
      } else if (var1 != null) {
         this.clockSkew = var1.getClockSkew();
         if (!var1.isSet("ClockSkew")) {
            var4 = false;
         }
      } else {
         var4 = false;
      }

      if (var1 != null) {
         LogUtils.debugWss("isSet.clockSkew = " + var1.isSet("ClockSkew"));
         LogUtils.debugWss("isSet.clockPrecision = " + var1.isSet("ClockPrecision"));
      }

      LogUtils.debugWss("skewSet = " + var4);
      LogUtils.debugWss("precisionSet = " + var3);
      this.useClockSkew = var4 || !var3;
      var2 = System.getProperty("weblogic.wsee.security.clock.precision.lax");
      if (var2 != null) {
         this.laxPrecision = Boolean.parseBoolean(var2);
      } else if (var1 != null) {
         this.laxPrecision = var1.isLaxPrecision();
      }

      var2 = System.getProperty("weblogic.wsee.security.delay.max");
      if (var2 != null) {
         this.maxProcessingDelay = Long.parseLong(var2);
      } else if (var1 != null) {
         this.maxProcessingDelay = var1.getMaxProcessingDelay();
      }

      var2 = System.getProperty("weblogic.wsee.security.validity");
      if (var2 != null) {
         this.validityPeriod = Integer.parseInt(var2);
      } else if (var1 != null) {
         this.validityPeriod = var1.getValidityPeriod();
      }

   }

   public void setMessageAge(int var1) {
      this.validityPeriod = var1;
   }

   public int getMessageAge() {
      return this.validityPeriod;
   }

   private boolean isFreshnessEnforced() {
      return this.maxProcessingDelay != -1L;
   }

   private long getClockSkew() {
      return this.useClockSkew ? this.clockSkew : this.clockPrecision;
   }

   public void validate(Calendar var1) throws SOAPFaultException {
      this.checkExpiration(var1, (Calendar)null, this.validityPeriod);
   }

   public void validate(Timestamp var1, short var2) {
      this.checkExpiration(var1.getCreated(), var1.getExpires(), var2);
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      LogUtils.debugWss("In activateUpdate of Timestamp.");
      DescriptorBean var2 = var1.getProposedBean();
      LogUtils.debugWss("bean = " + var2);
      if (var2 instanceof WebserviceTimestampMBean) {
         this.init((WebserviceTimestampMBean)var2);
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }
}
