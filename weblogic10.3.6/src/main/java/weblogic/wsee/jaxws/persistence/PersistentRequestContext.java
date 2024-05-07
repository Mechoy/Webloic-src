package weblogic.wsee.jaxws.persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.jaxws.sslclient.PersistentSSLInfo;
import weblogic.wsee.jaxws.sslclient.SSLClientUtil;

public class PersistentRequestContext implements Serializable {
   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Logger.getLogger(PersistentRequestContext.class.getName());
   private static final int SSL_PRIORITY = 100;
   private static final int BASE_PRIORITY = 0;
   private static final SortedSet<NonSerializablePropertyHandler> _handlerRegistry = new TreeSet();
   private transient Map<String, Object> _props;
   private boolean _nonSerializableKeys;

   public static void registerNonSerializablePropertyHandler(NonSerializablePropertyHandler var0) {
      _handlerRegistry.add(var0);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      Object var2 = this._props;
      if (this._nonSerializableKeys) {
         var2 = new HashMap(this._props);
         Iterator var3 = _handlerRegistry.iterator();

         while(var3.hasNext()) {
            NonSerializablePropertyHandler var4 = (NonSerializablePropertyHandler)var3.next();
            var4.convertNonSerializable((Map)var2);
         }
      }

      var1.writeObject("10.3.6");
      var1.writeObject(var2);
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.readObject();
      this._props = (Map)var1.readObject();
      var1.defaultReadObject();
      if (this._nonSerializableKeys) {
         Iterator var2 = _handlerRegistry.iterator();

         while(var2.hasNext()) {
            NonSerializablePropertyHandler var3 = (NonSerializablePropertyHandler)var2.next();
            var3.unconvertNonSerializable(this._props);
         }
      }

   }

   public PersistentRequestContext() {
      this(new HashMap());
   }

   public PersistentRequestContext(Map<String, Object> var1) {
      this._props = new HashMap(var1.size());
      this._nonSerializableKeys = false;
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = var1.get(var3);
         this._props.put(var3, var4);
         if (var4 != null && !(var4 instanceof Serializable)) {
            this._nonSerializableKeys = true;
         }
      }

   }

   public void loadRequestContext(Map<String, Object> var1) {
      Iterator var2 = this._props.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = this._props.get(var3);
         var1.put(var3, var4);
      }

   }

   static {
      registerNonSerializablePropertyHandler(new SSLNonSerializablePropertyHandler());
      registerNonSerializablePropertyHandler(new BaseNonSerializablePropertyHandler());
   }

   private static class SSLNonSerializablePropertyHandler extends AbstractNonSerializablePropertyHandler {
      private SSLNonSerializablePropertyHandler() {
         super(null);
      }

      public int getPriority() {
         return 100;
      }

      public void convertNonSerializable(Map<String, Object> var1) {
         if (var1.containsKey("com.sun.xml.ws.transport.https.client.SSLSocketFactory")) {
            if (var1.containsKey("weblogic.wsee.jaxws.sslclient.PersistentSSLInfo")) {
               var1.remove("com.sun.xml.ws.transport.https.client.SSLSocketFactory");
            } else {
               PersistentSSLInfo var2 = SSLClientUtil.getPersistentSSLInfoFromSysProperties();
               var1.put("weblogic.wsee.jaxws.sslclient.PersistentSSLInfo", var2);
            }
         }

      }

      public void unconvertNonSerializable(Map<String, Object> var1) {
         if (var1.containsKey("weblogic.wsee.jaxws.sslclient.PersistentSSLInfo")) {
            PersistentSSLInfo var2 = (PersistentSSLInfo)var1.get("weblogic.wsee.jaxws.sslclient.PersistentSSLInfo");
            var1.put("com.sun.xml.ws.transport.https.client.SSLSocketFactory", SSLClientUtil.getSSLSocketFactory(var2));
         }

      }

      // $FF: synthetic method
      SSLNonSerializablePropertyHandler(Object var1) {
         this();
      }
   }

   private static class BaseNonSerializablePropertyHandler extends AbstractNonSerializablePropertyHandler {
      private BaseNonSerializablePropertyHandler() {
         super(null);
      }

      public int getPriority() {
         return 0;
      }

      public void convertNonSerializable(Map<String, Object> var1) {
         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Object var4 = var1.get(var3);
            if (var4 != null && !(var4 instanceof Serializable)) {
               var1.put(var3, new MarkerValue(var4));
               if (PersistentRequestContext.LOGGER.isLoggable(Level.FINE)) {
                  PersistentRequestContext.LOGGER.fine("Created MarkerValue for non-serializable PersistentRequestContext property '" + var3 + "' and value: " + var4);
               }
            }
         }

      }

      public void unconvertNonSerializable(Map<String, Object> var1) {
         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Object var4 = var1.get(var3);
            if (var4 instanceof MarkerValue && PersistentRequestContext.LOGGER.isLoggable(Level.FINE)) {
               PersistentRequestContext.LOGGER.fine("Found MarkerValue serialized in place of non-serializable PersistentRequestContext property '" + var3 + "' and value: " + var4);
            }
         }

      }

      // $FF: synthetic method
      BaseNonSerializablePropertyHandler(Object var1) {
         this();
      }
   }

   private abstract static class AbstractNonSerializablePropertyHandler implements NonSerializablePropertyHandler {
      private AbstractNonSerializablePropertyHandler() {
      }

      public int compareTo(Object var1) {
         if (!(var1 instanceof AbstractNonSerializablePropertyHandler)) {
            return 0;
         } else {
            AbstractNonSerializablePropertyHandler var2 = (AbstractNonSerializablePropertyHandler)var1;
            return this.getPriority() - var2.getPriority();
         }
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof AbstractNonSerializablePropertyHandler)) {
            return false;
         } else {
            AbstractNonSerializablePropertyHandler var2 = (AbstractNonSerializablePropertyHandler)var1;
            return var2.getPriority() == this.getPriority();
         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString()).append(" - priority=").append(this.getPriority());
         return var1.toString();
      }

      // $FF: synthetic method
      AbstractNonSerializablePropertyHandler(Object var1) {
         this();
      }
   }

   public interface NonSerializablePropertyHandler extends Comparable {
      int getPriority();

      void convertNonSerializable(Map<String, Object> var1);

      void unconvertNonSerializable(Map<String, Object> var1);
   }

   private static class MarkerValue implements Serializable {
      private static final long serialVersionUID = 1L;
      private String _className;
      private String _toStringValue;

      public MarkerValue(Object var1) {
         this._className = var1 != null ? var1.getClass().getName() : "null";
         this._toStringValue = var1 != null ? var1.toString() : "null";
      }

      public int hashCode() {
         return this._toStringValue.hashCode();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof MarkerValue)) {
            return false;
         } else {
            MarkerValue var2 = (MarkerValue)var1;
            return var2._toStringValue.equals(this._toStringValue);
         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         var1.append(" - ").append(this._className);
         var1.append(" - ").append(this._toStringValue);
         return var1.toString();
      }
   }
}
