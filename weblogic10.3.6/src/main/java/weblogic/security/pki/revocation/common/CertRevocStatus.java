package weblogic.security.pki.revocation.common;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

public final class CertRevocStatus {
   private static final String DATE_FORMAT = "EEE d MMM yyyy HH:mm:ss.SSS Z";
   private final CertRevocCheckMethodList.SelectableMethod sourceMethod;
   private final X500Principal subjectDn;
   private final X500Principal issuerDn;
   private final BigInteger serialNumber;
   private final Date thisUpdate;
   private final Date nextUpdate;
   private final boolean revoked;
   private final Boolean nonceIgnored;
   private final Object key;
   private final Map<String, String> additionalProperties;

   CertRevocStatus(CertRevocCheckMethodList.SelectableMethod var1, X500Principal var2, X500Principal var3, BigInteger var4, Date var5, Date var6, boolean var7, Boolean var8, Map<String, String> var9) {
      Util.checkNotNull("sourceMethod", var1);
      Util.checkNotNull("issuerDn", var3);
      Util.checkNotNull("serialNumber", var4);
      Util.checkNotNull("thisUpdate", var5);
      this.sourceMethod = var1;
      this.subjectDn = var2;
      this.issuerDn = var3;
      this.serialNumber = var4;
      this.thisUpdate = var5;
      this.nextUpdate = var6;
      this.revoked = var7;
      this.nonceIgnored = var8;
      this.key = createKey(var3, var4);
      if (null == var9) {
         this.additionalProperties = null;
      } else {
         HashMap var10 = new HashMap(var9);
         this.additionalProperties = Collections.unmodifiableMap(var10);
      }

   }

   public Object getKey() {
      return this.key;
   }

   public static Object createKey(X509Certificate var0) {
      Util.checkNotNull("X509Certificate", var0);
      X500Principal var1 = var0.getIssuerX500Principal();
      BigInteger var2 = var0.getSerialNumber();
      return null != var1 && null != var2 ? createKey(var1, var2) : null;
   }

   public static Object createKey(X500Principal var0, BigInteger var1) {
      Util.checkNotNull("issuerDn", var0);
      Util.checkNotNull("serialNumber", var1);
      StringBuilder var2 = new StringBuilder(128);
      var2.append(var0.getName());
      var2.append("|");
      var2.append(var1.toString(16));
      return var2.toString();
   }

   public CertRevocCheckMethodList.SelectableMethod getSourceMethod() {
      return this.sourceMethod;
   }

   public X500Principal getSubjectDn() {
      return this.subjectDn;
   }

   public X500Principal getIssuerDn() {
      return this.issuerDn;
   }

   public BigInteger getSerialNumber() {
      return this.serialNumber;
   }

   public Date getThisUpdate() {
      return this.thisUpdate;
   }

   public Date getNextUpdate() {
      return this.nextUpdate;
   }

   public boolean isRevoked() {
      return this.revoked;
   }

   public Boolean isNonceIgnored() {
      return this.nonceIgnored;
   }

   boolean isValid(int var1, int var2, LogListener var3) {
      Util.checkTimeTolerance(var1);
      Util.checkRefreshPeriodPercent(var2);
      if (null == this.nextUpdate) {
         if (null != var3 && var3.isLoggable(Level.FINEST)) {
            var3.log(Level.FINEST, "Status not cachable for {0}: No next update indicated, continually updated.", this.getSubjectDn());
         }

         return false;
      } else if (this.thisUpdate.getTime() >= this.nextUpdate.getTime()) {
         if (null != var3 && var3.isLoggable(Level.FINEST)) {
            var3.log(Level.FINEST, "Invalid status for {0}: ThisUpdate {1} is after NextUpdate {2}.", this.getSubjectDn(), this.thisUpdate, this.nextUpdate);
         }

         return false;
      } else {
         long var4 = this.nextUpdate.getTime() - this.thisUpdate.getTime();
         long var6 = var4 * (long)var2 / 100L;
         Date var8 = new Date(this.thisUpdate.getTime() + var6);
         Date var9 = this.thisUpdate;
         if (var1 > 0) {
            var9 = new Date(this.thisUpdate.getTime() - (long)(var1 * 1000));
         }

         Date var10 = var8;
         if (var1 > 0) {
            var10 = new Date(var8.getTime() + (long)(var1 * 1000));
         }

         Date var11 = new Date();
         if (var11.before(var9)) {
            if (null != var3 && var3.isLoggable(Level.FINEST)) {
               var3.log(Level.FINEST, "Premature status for {0}: Now {1} is before tolerance-applied ThisUpdate {2}.", this.getSubjectDn(), var11, var9);
            }

            return false;
         } else if (!var11.before(var10)) {
            if (null != var3 && var3.isLoggable(Level.FINEST)) {
               var3.log(Level.FINEST, "Expired status for {0}: Now {1} is not before tolerance-applied NextUpdate {2}.", this.getSubjectDn(), var11, var10);
            }

            return false;
         } else {
            return true;
         }
      }
   }

   public String toString() {
      return this.generateString();
   }

   private String generateString() {
      String var1 = this.isRevoked() ? "REVOKED" : "NOT REVOKED";
      String var2 = this.getSubjectDn() != null ? this.getSubjectDn().getName() : null;
      String var3 = MessageFormat.format("\nStatus={0}\nSource={1}\nSubject=\"{2}\"\nIssuer=\"{3}\"\nSerialNumber={4}\nStatusValid={5,date,EEE d MMM yyyy HH:mm:ss.SSS Z}\nStatusExpires={6,date,EEE d MMM yyyy HH:mm:ss.SSS Z}", var1, this.getSourceMethod(), var2, this.getIssuerDn().getName(), this.getSerialNumber().toString(16), this.getThisUpdate(), this.getNextUpdate());
      StringBuilder var4 = new StringBuilder(256);
      var4.append(var3);
      if (null != this.nonceIgnored) {
         var4.append("\n");
         var4.append("NonceIgnored");
         var4.append("=");
         var4.append(this.nonceIgnored);
      }

      if (null != this.additionalProperties) {
         Set var5 = this.additionalProperties.entrySet();
         if (null != var5) {
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               Map.Entry var7 = (Map.Entry)var6.next();
               if (null != var7) {
                  var4.append("\n");
                  var4.append((String)var7.getKey());
                  var4.append("=");
                  var4.append((String)var7.getValue());
               }
            }
         }
      }

      var4.append("\n");
      return var4.toString();
   }

   public Map<String, String> getAdditionalProperties() {
      return this.additionalProperties;
   }

   static String format(Date var0) {
      if (null == var0) {
         return null;
      } else {
         SimpleDateFormat var1 = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss.SSS Z");
         return var1.format(var0);
      }
   }
}
