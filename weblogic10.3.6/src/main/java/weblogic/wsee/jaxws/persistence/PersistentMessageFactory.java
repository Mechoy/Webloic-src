package weblogic.wsee.jaxws.persistence;

import com.sun.xml.ws.api.message.Packet;
import java.io.Serializable;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.subject.SubjectManager;
import weblogic.wsee.jaxws.framework.PropertySetUtil;

public class PersistentMessageFactory {
   private static final Logger LOGGER = Logger.getLogger(PersistentMessageFactory.class.getName());
   private static final PersistentMessageFactory _instance = new PersistentMessageFactory();
   private Map<String, PropertySetUtil.PropertySetRetriever<?>> propSetRetrievers = new ConcurrentHashMap();

   public static PersistentMessageFactory getInstance() {
      return _instance;
   }

   public PersistentMessage createMessageFromPacket(String var1, Packet var2) {
      PersistentContext var3 = this.createContextFromPacket(var1, var2);
      return new PersistentMessage(var2.getMessage(), var3);
   }

   public PersistentContext createContextFromPacket(String var1, Packet var2) {
      PacketPersistencePropertyBag var3 = (PacketPersistencePropertyBag)PacketPersistencePropertyBag.propertySetRetriever.getFromPacket(var2);
      HashSet var4 = new HashSet();
      var4.addAll(var3.getPersistablePropertyBagClassNames());
      HashMap var5 = new HashMap();
      Iterator var7;
      String var8;
      Object var9;
      if (var2.component != null) {
         StandardPersistentPropertyRegister var6 = (StandardPersistentPropertyRegister)var2.component.getSPI(StandardPersistentPropertyRegister.class);
         if (var6 != null) {
            var7 = var6.getStandardProperties().iterator();

            while(var7.hasNext()) {
               var8 = (String)var7.next();
               var9 = this.getPropertyFromPacket(var2, var8);
               var5.put(var8, (Serializable)var9);
            }

            var4.addAll(var6.getStandardPropertyBagClassNames());
         }
      }

      Iterator var10 = var3.getPersistablePropertyNames().iterator();

      while(var10.hasNext()) {
         String var12 = (String)var10.next();
         Object var14 = this.getPropertyFromPacket(var2, var12);
         var5.put(var12, (Serializable)var14);
      }

      HashMap var11 = new HashMap();
      var7 = var3.getPersistableInvocationPropertyNames().iterator();

      while(var7.hasNext()) {
         var8 = (String)var7.next();
         if (!var2.invocationProperties.containsKey(var8)) {
            throw new IllegalArgumentException("Packet property '" + var8 + "' was flagged to be persistent, but is not present in the Packet instance being persisted.");
         }

         var9 = var2.invocationProperties.get(var8);
         if (var9 != null && !(var9 instanceof Serializable)) {
            throw new IllegalArgumentException("Packet property '" + var8 + "' was flagged to be persistent, but is not Serializable");
         }

         var11.put(var8, (Serializable)var9);
      }

      Map var13 = (Map)var2.get("weblogic.wsee.jaxws.async.PersistentContext");
      return new PersistentContext(var1, var5, var4, var13, var11);
   }

   private Object getPropertyFromPacket(Packet var1, String var2) {
      Object var3;
      if (var1.supports(var2)) {
         var3 = var1.get(var2);
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Property '" + var2 + "' was flagged to be persistent but isn't supported (no PropertySet that includes it) by the current Packet. Skipping getting a value for it when saving PeristentContext.");
         }

         var3 = null;
      }

      if (var3 != null && !(var3 instanceof Serializable)) {
         throw new IllegalArgumentException("Packet property '" + var2 + "' was flagged to be persistent, but is not Serializable");
      } else {
         return var3;
      }
   }

   public void setMessageIntoPacket(PersistentMessage var1, Packet var2) {
      var2.setMessage(var1.getMessage());
      this.setContextIntoPacket(var1.getContext(), var2);
   }

   public void setContextIntoPacket(PersistentContext var1, Packet var2) {
      Set var3 = var1.getPropertyBagClassNames();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();

         try {
            PropertySetUtil.PropertySetRetriever var6 = (PropertySetUtil.PropertySetRetriever)this.propSetRetrievers.get(var5);
            if (var6 == null) {
               synchronized(this.propSetRetrievers) {
                  var6 = (PropertySetUtil.PropertySetRetriever)this.propSetRetrievers.get(var5);
                  if (var6 == null) {
                     Class var8 = this.getClass().getClassLoader().loadClass(var5);
                     var6 = PropertySetUtil.getRetriever(var8);
                     this.propSetRetrievers.put(var5, var6);
                  }
               }
            }

            var6.getFromPacket(var2);
         } catch (Exception var12) {
            throw new RuntimeException("Error loading satellite property set back into Packet to accept PersistentContext: " + var12.toString(), var12);
         }
      }

      Map var13 = var1.getPropertyMap();
      Iterator var14 = var13.keySet().iterator();

      while(var14.hasNext()) {
         String var16 = (String)var14.next();
         Object var7 = var13.get(var16);
         if (var2.supports(var16)) {
            try {
               var2.put(var16, var7);
            } catch (Exception var10) {
               throw new IllegalArgumentException("Error setting property '" + var16 + "' onto Packet with value: " + var7 + ". Error was: " + var10.toString(), var10);
            }
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Property '" + var16 + "' was flagged stored on a persistent message but isn't supported (no PropertySet that includes it) by the current Packet. Skipping setting the value for it when restoring PeristentContext. Value is: " + var7);
         }
      }

      Map var15 = var1.getInvocationPropertyMap();
      Iterator var18 = var15.keySet().iterator();

      while(var18.hasNext()) {
         String var17 = (String)var18.next();
         Object var19 = var15.get(var17);
         var2.invocationProperties.put(var17, var19);
      }

      if (var1.getContextPropertyMap() != null) {
         var2.put("weblogic.wsee.jaxws.async.PersistentContext", var1.getContextPropertyMap());
      }

   }

   public <T> T runActionInContext(PersistentContext var1, AuthenticatedSubject var2, PrivilegedExceptionAction<T> var3) throws PrivilegedActionException {
      AuthenticatedSubject var4 = var1.getSubject(var2);
      if (var4 != null) {
         if (SubjectManager.getSubjectManager().isKernelIdentity(var4)) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Persistent context contains kernel subject");
            }

            throw new RuntimeException("Kernel identity was retrieved from context");
         }
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Persistent context does not contain subject, running as anonymous subject");
         }

         var4 = (AuthenticatedSubject)SubjectManager.getSubjectManager().getAnonymousSubject();
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("PersistentMessageFactory.runActionInContext: running as " + var4);
      }

      return SecurityServiceManager.runAs(var2, var4, var3);
   }
}
