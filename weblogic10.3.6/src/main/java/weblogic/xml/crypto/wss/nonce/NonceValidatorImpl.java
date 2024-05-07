package weblogic.xml.crypto.wss.nonce;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.TimestampHandler;
import weblogic.xml.crypto.wss.api.NonceValidator;
import weblogic.xml.crypto.wss.api.Timestamp;

/** @deprecated */
public class NonceValidatorImpl implements NonceValidator {
   private static final boolean verbose = Verbose.isVerbose(NonceValidatorImpl.class);
   static final boolean DEBUG = false;
   protected static long expirationTime = 0L;
   protected TimestampHandler timeoutHandler = null;
   protected static Set nonceSet;
   private static NonceEntry head;
   private static NonceEntry tail;

   public NonceValidatorImpl() {
   }

   public NonceValidatorImpl(int var1) {
      this.timeoutHandler = new SimpleTimeoutHandler(var1);
      this.init(var1);
   }

   public void init(String var1, TimestampHandler var2) {
      if (null == var2) {
         this.timeoutHandler = new SimpleTimeoutHandler();
      } else {
         this.timeoutHandler = var2;
      }

      this.init(this.timeoutHandler.getMessageAge());
   }

   private void init(int var1) {
      expirationTime = (long)var1 * 1000L;
      if (nonceSet == null) {
         nonceSet = new HashSet(1000);
         head = null;
         tail = null;
      }

   }

   public void setExpirationTime(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("expirationSeconds must be greater than 0");
      } else {
         expirationTime = (long)(var1 * 1000);
         this.timeoutHandler.setMessageAge(var1);
         LogUtils.logWss("Set expriration value =" + expirationTime);
      }
   }

   public void checkNonceAndTime(String var1, Calendar var2) throws SOAPFaultException {
      this.timeoutHandler.validate(var2);
      this.checkDuplicate(var1, System.currentTimeMillis());
   }

   public void checkDuplicateNonce(String var1) throws SOAPFaultException {
      if (null == var1) {
         throw new IllegalArgumentException("Null nonce ");
      } else {
         this.checkDuplicate(var1, System.currentTimeMillis());
      }
   }

   protected void checkDuplicate(String var1, long var2) throws SOAPFaultException {
      updateNonceEntry(var1, true, var2);
   }

   protected static boolean expired(long var0, long var2) {
      return var0 + expirationTime < var2;
   }

   protected static synchronized void updateNonceEntry(String var0, boolean var1, long var2) throws SOAPFaultException {
      while(head != null && head.isExpired(var2)) {
         nonceSet.remove(head.nonce);
         head = head.next;
      }

      NonceEntry var4 = new NonceEntry(var0, var2);
      if (head == null) {
         nonceSet.add(var0);
         head = var4;
         tail = var4;
      } else if (var1 && nonceSet.contains(var0)) {
         throw new SOAPFaultException(NONCE_FAULTCODE, "A duplicated nonce is found! " + var0, (String)null, (Detail)null);
      } else {
         tail.next = var4;
         tail = var4;
         nonceSet.add(var0);
      }
   }

   protected static class SimpleTimeoutHandler implements TimestampHandler {
      private int defaultMessageAge;

      private SimpleTimeoutHandler() {
         this.defaultMessageAge = 60;
      }

      private SimpleTimeoutHandler(int var1) {
         this.defaultMessageAge = 60;
         this.defaultMessageAge = var1;
      }

      public void validate(Timestamp var1, short var2) {
         throw new UnsupportedOperationException("Timestamp is not supported");
      }

      public void validate(Calendar var1) throws SOAPFaultException {
         if (null == var1) {
            throw new IllegalArgumentException("Null created time parameter");
         } else {
            long var2 = var1.getTimeInMillis();
            long var4 = System.currentTimeMillis();
            if (var2 > (long)(this.defaultMessageAge * 1000) + var4) {
               throw new SOAPFaultException(EXPIRED_FAULTCODE, "Got wrong created time = " + var1.toString() + "(" + var2 + ") current time = " + var4, (String)null, (Detail)null);
            } else if (NonceValidatorImpl.expired(var2, var4)) {
               throw new SOAPFaultException(EXPIRED_FAULTCODE, "The transaction has timed-out, created time = " + var1.toString() + "(" + var2 + ") current time = " + var4, (String)null, (Detail)null);
            }
         }
      }

      public int getMessageAge() {
         return this.defaultMessageAge;
      }

      public void setMessageAge(int var1) {
         this.defaultMessageAge = var1;
      }

      // $FF: synthetic method
      SimpleTimeoutHandler(int var1, Object var2) {
         this(var1);
      }

      // $FF: synthetic method
      SimpleTimeoutHandler(Object var1) {
         this();
      }
   }

   protected static class NonceEntry {
      private NonceEntry next = null;
      private String nonce;
      private long insertTime;

      NonceEntry(String var1, long var2) {
         this.nonce = var1;
         this.insertTime = var2;
      }

      private boolean isExpired(long var1) {
         return NonceValidatorImpl.expired(this.insertTime, var1);
      }
   }
}
