package weblogic.xml.security.specs;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import weblogic.webservice.core.handler.TimestampHandler;
import weblogic.xml.security.signature.Reference;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.utils.ValidationException;
import weblogic.xml.security.utils.XMLReader;
import weblogic.xml.security.utils.XMLSerializable;
import weblogic.xml.security.utils.XMLWriter;
import weblogic.xml.security.wsse.Security;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.security.wsu.Created;
import weblogic.xml.security.wsu.Expires;
import weblogic.xml.security.wsu.Timestamp;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public final class TimestampConfig extends XMLSerializable implements SpecConstants {
   private boolean clockSynchronized;
   private long clockPrecision;
   private boolean laxPrecision;
   private long maxProcessingDelay;
   private boolean generateTimestamp;
   private long validityPeriod;
   private boolean timestampRequired;
   public static final String ASSUME_SYNCHRONIZED_CLOCKS = "weblogic.webservice.security.clock.synchronized";
   public static final String CLOCK_PRECISION = "weblogic.webservice.security.clock.precision";
   public static final String LAX_CLOCK_PRECISION = "weblogic.webservice.security.clock.precision.lax";
   public static final String MAX_PROCESSING_DELAY = "weblogic.webservice.security.delay.max";
   public static final String GENERATE_TIMESTAMP = "weblogic.webservice.security.timestamp.include";
   public static final String VALIDITY_PERIOD = "weblogic.webservice.security.validity";
   public static final String REQUIRE_TIMESTAMP = "weblogic.webservice.security.timestamp.require";
   public static final int NO_MAX_PROCESSING_DELAY = -1;
   public static final int NO_EXPIRATION = -1;
   private static final QName FAILED_CHECK;

   public TimestampConfig(XMLInputStream var1) throws XMLStreamException {
      this();
      this.fromXMLInternal(var1);
   }

   public TimestampConfig(XMLReader var1) throws ValidationException {
      this();
      this.fromXMLInternal(var1);
   }

   public TimestampConfig() {
      boolean var1 = Boolean.getBoolean("weblogic.webservice.security.clock.synchronized");
      Integer var2 = Integer.getInteger("weblogic.webservice.security.clock.precision");
      long var3 = var2 != null ? var2.longValue() : 60000L;
      boolean var5 = Boolean.getBoolean("weblogic.webservice.security.clock.precision.lax");
      Integer var6 = Integer.getInteger("weblogic.webservice.security.delay.max");
      int var7 = var6 != null ? var6 : -1;
      Integer var8 = Integer.getInteger("weblogic.webservice.security.validity");
      int var9 = var8 != null ? var8 : -1;
      String var10 = System.getProperty("weblogic.webservice.security.timestamp.include");
      boolean var11 = var10 == null || var10.equalsIgnoreCase("true");
      String var12 = System.getProperty("weblogic.webservice.security.timestamp.require");
      boolean var13 = var12 == null || var10.equalsIgnoreCase("true");
      this.clockSynchronized = var1;
      this.clockPrecision = var3;
      this.laxPrecision = var5;
      this.maxProcessingDelay = (long)var7;
      this.generateTimestamp = var11;
      this.validityPeriod = (long)var9;
      this.timestampRequired = var13;
      this.checkConfig();
   }

   public TimestampConfig(boolean var1, long var2, boolean var4, long var5, boolean var7, long var8, boolean var10) {
      this.clockSynchronized = var1;
      this.clockPrecision = var2 > 0L ? var2 : 1L;
      this.laxPrecision = var4;
      this.maxProcessingDelay = var5;
      this.generateTimestamp = var7;
      this.validityPeriod = var8;
      this.timestampRequired = var10;
      this.checkConfig();
   }

   private void checkConfig() {
      if (!this.clockSynchronized) {
         if (this.maxProcessingDelay > -1L) {
            throw new AssertionError("Cannot specify a maximum processing delay if clocks are not synchronized");
         }

         if (this.validityPeriod > -1L && this.generateTimestamp) {
            throw new AssertionError("Cannot specify a validityPeriod if clocks are not synchronized");
         }
      }

      if (!this.laxPrecision && this.maxProcessingDelay != -1L && this.maxProcessingDelay < this.clockPrecision) {
         throw new AssertionError("Cannot enforce a maximum processing delay that is smaller than clock precision unless lax enforcement of clock precision has been enabled");
      }
   }

   public boolean isClockSynchronized() {
      return this.clockSynchronized;
   }

   public long getMaxProcessingDelay() {
      return this.maxProcessingDelay;
   }

   public long getRoundedMaxDelay() {
      return this.maxProcessingDelay / this.clockPrecision;
   }

   public long getClockPrecision() {
      return this.clockPrecision;
   }

   public boolean laxClockPrecision() {
      return this.laxPrecision;
   }

   public boolean generateTimestamp() {
      return this.generateTimestamp;
   }

   public boolean includeExpiry() {
      return this.validityPeriod != -1L;
   }

   public long getValidityPeriod() {
      return this.validityPeriod;
   }

   public boolean isFreshnessEnforced() {
      return this.maxProcessingDelay != -1L;
   }

   public void setClockSynchronized(boolean var1) {
      this.clockSynchronized = var1;
   }

   public void setClockPrecision(long var1) {
      this.clockPrecision = var1;
   }

   public long getRoundedTime(Calendar var1) {
      long var2 = var1.getTimeInMillis();
      return var2 / this.clockPrecision;
   }

   public void setLaxPrecision(boolean var1) {
      this.laxPrecision = var1;
   }

   public void setMaxProcessingDelay(long var1) {
      this.maxProcessingDelay = var1;
   }

   public void setGenerateTimestamp(boolean var1) {
      this.generateTimestamp = var1;
   }

   public void setValidityPeriod(long var1) {
      this.validityPeriod = var1;
   }

   public boolean isTimestampRequired() {
      return this.timestampRequired;
   }

   public void setTimestampRequired(boolean var1) {
      this.timestampRequired = var1;
   }

   public String toString() {
      return "weblogic.xml.security.specs.TimestampConfig{timestampRequired=" + this.timestampRequired + ", clockSynchronized=" + this.clockSynchronized + ", clockPrecision=" + this.clockPrecision + ", laxPrecision=" + this.laxPrecision + ", maxProcessingDelay=" + this.maxProcessingDelay + ", generateTimestamp=" + this.generateTimestamp + ", validityPeriod=" + this.validityPeriod + "}";
   }

   public void toXML(XMLWriter var1) {
      var1.writeStartElement((String)null, "timestamp");
      this.writeBoolean(var1, "clocks-synchronized", this.clockSynchronized);
      if (this.clockPrecision != 60000L) {
         this.writeLong(var1, "clock-precision", this.clockPrecision);
      }

      if (this.laxPrecision) {
         this.writeBoolean(var1, "enforce-precision", this.laxPrecision);
      }

      if (this.maxProcessingDelay != -1L) {
         this.writeLong(var1, "inbound-expiry", this.maxProcessingDelay);
      }

      if (!this.generateTimestamp) {
         this.writeBoolean(var1, "generate-signature-timestamp", this.generateTimestamp);
      }

      if (this.validityPeriod != -1L) {
         this.writeLong(var1, "outbound-expiry", this.validityPeriod);
      }

      if (!this.timestampRequired) {
         this.writeBoolean(var1, "require-signature-timestamp", this.timestampRequired);
      }

      var1.writeEndElement();
   }

   private void writeBoolean(XMLWriter var1, String var2, boolean var3) {
      var1.writeStartElement((String)null, var2);
      var1.writeCharacters(Boolean.toString(var3));
      var1.writeEndElement();
   }

   private void writeLong(XMLWriter var1, String var2, long var3) {
      var1.writeStartElement((String)null, var2);
      var1.writeCharacters(Long.toString(var3));
      var1.writeEndElement();
   }

   public TimestampConfig copy() {
      return new TimestampConfig(this.clockSynchronized, this.clockPrecision, this.laxPrecision, this.maxProcessingDelay, this.generateTimestamp, this.validityPeriod, this.timestampRequired);
   }

   protected void fromXMLInternal(XMLReader var1) throws ValidationException {
      HashSet var2 = new HashSet(7);
      var1.require(2, (String)null, "timestamp");

      for(int var3 = var1.next(); var3 != 4; var3 = var1.next()) {
         if (var3 == 16) {
            throw new ValidationException("Unexpected CharacterData in TimestampConfig");
         }

         String var4 = var1.getLocalName();
         if (var4.equals("clocks-synchronized")) {
            this.clockSynchronized = this.readBoolean("clocks-synchronized", var1, var2);
         } else if (var4.equals("clock-precision")) {
            this.clockPrecision = this.readLong("clock-precision", var1, var2);
         } else if (var4.equals("enforce-precision")) {
            this.laxPrecision = this.readBoolean("enforce-precision", var1, var2);
         } else if (var4.equals("inbound-expiry")) {
            this.maxProcessingDelay = this.readLong("inbound-expiry", var1, var2);
         } else if (var4.equals("generate-signature-timestamp")) {
            this.generateTimestamp = this.readBoolean("generate-signature-timestamp", var1, var2);
         } else if (var4.equals("outbound-expiry")) {
            this.validityPeriod = this.readLong("outbound-expiry", var1, var2);
         } else {
            if (!var4.equals("require-signature-timestamp")) {
               throw new ValidationException("Unexpected data in dd -- " + var4);
            }

            this.timestampRequired = this.readBoolean("require-signature-timestamp", var1, var2);
         }
      }

      var1.require(4, (String)null, "timestamp");
   }

   private long readLong(String var1, XMLReader var2, Set var3) throws ValidationException {
      return Long.parseLong(this.readString(var1, var2, var3));
   }

   private boolean readBoolean(String var1, XMLReader var2, Set var3) throws ValidationException {
      return Boolean.valueOf(this.readString(var1, var2, var3));
   }

   private String readString(String var1, XMLReader var2, Set var3) throws ValidationException {
      if (var3.contains(var1)) {
         throw new ValidationException("Timestamp configuration contains multiple " + var1);
      } else {
         var2.require(2, (String)null, var1);
         var2.next();
         var2.require(16, (String)null, (String)null);
         String var4 = var2.getText();
         var2.next();
         var2.require(4, (String)null, var1);
         var3.add(var1);
         return var4;
      }
   }

   public void checkExpiration(Calendar var1, Calendar var2, Calendar var3) {
      long var4 = this.getRoundedTime(var1);
      long var6 = this.getRoundedTime(var2);
      long var8;
      if (var3 != null) {
         var8 = this.getRoundedTime(var3);
      } else {
         var8 = 0L;
      }

      if (!this.isClockSynchronized()) {
         if (var3 != null) {
            throw new SOAPFaultException(TimestampHandler.EXPIRED_FAULTCODE, "Message includes expiry but clocks are not synchronized", (String)null, (Detail)null);
         }
      } else {
         if (this.isFreshnessEnforced()) {
            long var10 = var6 + this.getRoundedMaxDelay();
            if (var10 < var4) {
               throw new SOAPFaultException(TimestampHandler.EXPIRED_FAULTCODE, "Message is too old", (String)null, (Detail)null);
            }
         }

         if (var4 < var6) {
            throw new SOAPFaultException(TimestampHandler.EXPIRED_FAULTCODE, "Message Created time in the future", (String)null, (Detail)null);
         }

         if (var3 != null) {
            if (!this.laxClockPrecision() && var8 <= var6) {
               throw new SOAPFaultException(TimestampHandler.EXPIRED_FAULTCODE, "Message is expired: clock precision insufficient to distinguish Created time from Expires time", (String)null, (Detail)null);
            }

            if (var8 < var4) {
               throw new SOAPFaultException(TimestampHandler.EXPIRED_FAULTCODE, "Message Expires time has passed", (String)null, (Detail)null);
            }
         }
      }

   }

   public void checkTimestamps(Security var1, MessageContext var2) {
      HashMap var3 = new HashMap();
      Calendar var4 = Calendar.getInstance(WSUConstants.TZ_ZULU);
      Calendar var5 = null;
      Calendar var6 = null;
      Iterator var7 = var1.getTimestamps();

      while(var7.hasNext()) {
         Timestamp var8 = (Timestamp)var7.next();
         Created var9 = var8.getCreated();
         Calendar var10 = var9.getTime();
         var3.put("#" + var8.getId(), var8);
         var3.put("#" + var9.getId(), var10);
         Expires var11 = var8.getExpires();
         Calendar var12;
         if (var11 != null) {
            var12 = var11.getTime();
         } else {
            var12 = null;
         }

         if (var6 == null || var6.after(var10)) {
            var6 = var10;
         }

         if (var5 == null || var12 != null && var5.after(var12)) {
            var5 = var12;
         }
      }

      if (var6 != null) {
         this.checkExpiration(var4, var6, var5);
      }

      var2.setProperty("weblogic.webservice.timestamp.received", var4);
      var2.setProperty("weblogic.webservice.timestamp.created", var6);
      if (var5 != null) {
         var2.setProperty("weblogic.webservice.timestamp.expires", var5);
      }

      if (this.isTimestampRequired()) {
         var7 = var1.getSignatures();

         while(var7.hasNext()) {
            Signature var13 = (Signature)var7.next();
            boolean var14 = false;
            Iterator var15 = var13.getReferences();

            while(var15.hasNext()) {
               Reference var16 = (Reference)var15.next();
               if (var3.containsKey(var16.getURI())) {
                  var14 = true;
                  break;
               }
            }

            if (!var14) {
               throw new SOAPFaultException(FAILED_CHECK, "Signature did not cover a timestamp; cannot determine freshness", var1.getRole(), (Detail)null);
            }
         }
      }

   }

   public void addTimestamp(SecuritySpec var1, Security var2) {
      SignatureSpec var3 = var1.getSignatureSpec();
      if (var3 != null && this.generateTimestamp()) {
         if (!var3.contains(WSUConstants.WSU_URI, "Timestamp", (String)null)) {
            var3.addElement(WSUConstants.WSU_URI, "Timestamp", (String)null);
         }

         if (this.includeExpiry()) {
            var2.addTimestamp(this.getValidityPeriod());
         } else {
            var2.addTimestamp();
         }
      }

   }

   static {
      FAILED_CHECK = Utils.getQName(WSSEConstants.QNAME_FAULT_FAILEDCHECK);
   }
}
