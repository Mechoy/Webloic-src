package weblogic.wsee.reliability2.saf;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.TransportBackChannel;
import java.io.Externalizable;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.xml.datatype.Duration;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFManager;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.client.async.AsyncTransportProvider;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.jaxws.persistence.PersistentContext;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.jaxws.persistence.PersistentObject;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.sequence.DeliveryAssurance;
import weblogic.wsee.reliability2.sequence.DestinationMessageInfo;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;

public class DestinationSequenceSAFMap extends SequenceSAFMap<DestinationSequence, DestinationMessageInfo> {
   private static final Logger LOGGER = Logger.getLogger(DestinationSequenceSAFMap.class.getName());
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public DestinationSequenceSAFMap() throws PersistentStoreException, NamingException {
      if (KernelStatus.isServer()) {
         MyConversationLifecycleListener var1 = new MyConversationLifecycleListener();
         this._safManager.addConversationLifecycleListener(var1);
      }

      if (LOGGER.isLoggable(Level.FINE)) {
      }

   }

   public int size() {
      int var1 = 0;
      if (this._safManager != null) {
         var1 += this._safManager.getConversationNamesOnReceivingSide().size();
      }

      return var1;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsKey(Object var1) {
      return this._safManager != null && this._safManager.getConversationNamesOnReceivingSide().contains(var1);
   }

   public boolean containsValue(Object var1) {
      throw new IllegalStateException("Not supported");
   }

   public DestinationSequence get(Object var1) {
      if (!(var1 instanceof String)) {
         return null;
      } else {
         String var2 = (String)var1;
         if (this._safManager == null) {
            return null;
         } else {
            try {
               SAFConversationInfo var3 = this._safManager.getCachedConversationInfoOnReceivingSide(var2);
               return (DestinationSequence)getSequenceFromConversationInfo(var3, DestinationSequence.class);
            } catch (Exception var4) {
               throw new RuntimeException(var4.toString(), var4);
            }
         }
      }
   }

   public DestinationSequence put(String var1, final DestinationSequence var2) {
      boolean var3 = var1 == null || !this.containsKey(var1);
      if (this._safManager == null) {
         throw new IllegalStateException("Persistent DestinationSequence detected, but SAF is not available");
      } else {
         try {
            DestinationSequence var4 = null;
            final SequenceSAFMap.SequenceExternalizable var6;
            if (var3) {
               SAFConversationInfoImpl var5 = new SAFConversationInfoImpl(3);
               var5.setTransportType(3);
               var5.setDestinationType(3);
               var5.setConversationName(var1);
               var5.setDynamic(false);
               var5.setCreateConversationMessageID((String)null);
               var5.setConversationOffer((SAFConversationInfo)null);
               var5.setSourceURL(var2.getAcksToEpr().getAddress());
               var5.setDestinationURL(var2.getHostEpr().getAddress());
               this.setupSAFConversationQOS(var2, var5);
               this.setupSAFConversationTTL(var2.getExpires(), var5);
               this.setupSAFConversationIdleTimeout(var2.getIdleTimeout(), var5);
               var6 = new SequenceSAFMap.SequenceExternalizable(var2, true);
               var5.setContext(var6);
               SAFManager.ConversationNameRefinementCallback var7 = new SAFManager.ConversationNameRefinementCallback() {
                  public void conversationPreStore(SAFConversationInfo var1, SAFManager.LocationInfo var2x) {
                     if (var2.getId() == null) {
                        String var3 = WsUtil.generateRoutableUUID(var2.getPhysicalStoreName());
                        var1.setConversationName(var3);
                        var2.setId(var3);
                        String var4 = WsUtil.getStoreNameFromRoutableUUID(var3);
                        if (var4 == null || !var4.equals(var2.getPhysicalStoreName())) {
                           throw new IllegalArgumentException("SAF attempted to put seq '" + var3 + " into physical store '" + var4 + "' but the sequence itself was expecting physical store '" + var2.getPhysicalStoreName() + "'");
                        }
                     }

                     try {
                        var6.getBytes();
                     } catch (Exception var5) {
                        throw new RuntimeException(var5.toString(), var5);
                     }
                  }
               };
               this._safManager.registerConversationOnReceivingSide(var5, var7, var2.getPhysicalStoreName());
            } else {
               var4 = this.get(var1);
               SAFConversationInfo var12 = this._safManager.getCachedConversationInfoOnReceivingSide(var1);
               if (SecurityServiceManager.isKernelIdentity(getCurrentSubject())) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Bypassing update of requests in SAF for conversation: " + var2.getId() + " because we were invoked from a system thread, not a user thread. Only user threads can update the SAF conversation.");
                  }
               } else {
                  List var13 = var2.getAndClearPendingRequests();
                  Iterator var14 = var13.iterator();

                  while(var14.hasNext()) {
                     DestinationMessageInfo var8 = (DestinationMessageInfo)var14.next();
                     Packet var9 = var8.getRequestPacket();
                     if (var9 != null) {
                        this.storeSAFRequest(var2, var8);
                     }
                  }
               }

               var6 = (SequenceSAFMap.SequenceExternalizable)var12.getContext();
               var6.getBytes();

               try {
                  this._safManager.storeConversationContextOnReceivingSide(var1, var6);
               } catch (SAFException var10) {
                  if (!SequenceState.isTerminalState(var2.getState()) && !SequenceState.isClosedState(var2.getState())) {
                     throw var10;
                  }

                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Destination sequence " + var2.getId() + " was terminated before we do update the sequence to store");
                  }
               }
            }

            return var4;
         } catch (Exception var11) {
            throw new RuntimeException(var11.toString(), var11);
         }
      }
   }

   private static AuthenticatedSubject getCurrentSubject() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      return var0;
   }

   protected Externalizable createSAFRequestPayload(Packet var1, DestinationMessageInfo var2) {
      String var4;
      if (var2.getSuspendedFiber() != null) {
         PersistentContext var5 = PersistentMessageFactory.getInstance().createContextFromPacket(var2.getMessageId(), var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            var4 = AsyncTransportProvider.dumpPersistentContextContextProps(var5.getContextPropertyMap());
            LOGGER.fine("Storing SAFRequest as no-message PersistentContext for msg " + var2.getMessageId() + " with persistContext: " + var4);
         }

         PersistentObject var6 = new PersistentObject(var2, var5);
         return new SequenceSAFMap.ExternalizableWrapper(var6, false);
      } else {
         PersistentMessage var3 = PersistentMessageFactory.getInstance().createMessageFromPacket(var2.getMessageId(), var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            var4 = AsyncTransportProvider.dumpPersistentContextContextProps(var3.getContext().getContextPropertyMap());
            LOGGER.fine("Storing SAFRequest for msg " + var2.getMessageId() + " with persistContext: " + var4);
         }

         return new SequenceSAFMap.ExternalizableWrapper(var3, false);
      }
   }

   private void storeSAFRequest(DestinationSequence var1, DestinationMessageInfo var2) throws IOException, SAFException {
      Packet var3 = var2.getRequestPacket();
      if (var3 != null) {
         if (var3.getMessage() == null) {
            throw new IllegalArgumentException("Message was null for msgId " + var2.getMessageId());
         } else {
            DestinationSequence.preventRemovalOfAsyncRequestContext(var1, var2, var3);
            WsrmPropertyBag var4 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var3);
            var4.setInternalReceive(true);
            SAFRequest var5 = this.createSAFRequest(var1, var2);
            if (var5 != null) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Delivering SAFRequest for sequence " + var1.getId() + " msg num " + var2.getMessageNum() + " msgID " + var2.getMessageId());
               }

               this.restoreTransportBackChannelForRequest(var3, var1, var2);
               SAFConversationInfo var6 = this._safManager.getCachedConversationInfoOnReceivingSide(var1.getId());
               this._safManager.deliver(var6, var5);
            }
         }
      }
   }

   private void restoreTransportBackChannelForRequest(Packet var1, DestinationSequence var2, DestinationMessageInfo var3) {
      if (var1 != null) {
         WsrmPropertyBag var4 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var1);
         if (var4.getTransportBackChannel() != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("RM is returning the hijacked TransportBackChannel to the request packet ahead of final delivery of request sequence " + var2.getId() + " msg num " + var3.getMessageNum() + " msgID " + var3.getMessageId());
            }

            TransportBackChannel var5 = var4.getTransportBackChannel();
            var4.setTransportBackChannel((TransportBackChannel)null);
            var1.transportBackChannel = var5;
         }
      }

   }

   private void setupSAFConversationQOS(DestinationSequence var1, SAFConversationInfo var2) {
      DeliveryAssurance var3 = var1.getDeliveryAssurance();
      if (var3.isInOrder()) {
         var2.setInorder(true);
      }

      if (var3.getQos() == WsrmConstants.DeliveryQOS.ExactlyOnce) {
         var2.setQOS(1);
      } else if (var3.getQos() == WsrmConstants.DeliveryQOS.AtLeastOnce) {
         var2.setQOS(2);
      } else if (var3.getQos() == WsrmConstants.DeliveryQOS.AtMostOnce) {
         var2.setQOS(3);
      }

   }

   protected void setupSAFConversationTTL(Duration var1, SAFConversationInfo var2) {
      if (var1 != null) {
         long var3 = var1.getTimeInMillis(new Date());
         if (var3 <= 0L) {
            throw new RuntimeException("Invalid expiration time: " + var1.toString());
         }

         if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Setting RM sequence timetolive to " + var3 + " on seq " + var2.getConversationName());
         }

         var2.setTimeToLive(var3);
      } else {
         var2.setTimeToLive(Long.MAX_VALUE);
      }

   }

   protected void setupSAFConversationIdleTimeout(Duration var1, SAFConversationInfo var2) {
      if (var1 != null) {
         try {
            long var3 = System.currentTimeMillis();
            long var5 = var1.getTimeInMillis(new Date(var3));
            var2.setMaximumIdleTime(var5);
            if (LOGGER.isLoggable(Level.FINER)) {
               LOGGER.finer("InactivityTimeout is " + var1 + " on seq " + var2.getConversationName());
            }
         } catch (Exception var7) {
            throw new RuntimeException(var7.toString(), var7);
         }
      } else {
         var2.setMaximumIdleTime(0L);
      }

   }

   public DestinationSequence remove(Object var1) {
      if (this._safManager != null && var1 instanceof String) {
         String var2 = (String)var1;

         try {
            SAFConversationInfo var3 = this._safManager.getCachedConversationInfoOnReceivingSide(var2);
            if (var3 == null) {
               return null;
            } else {
               DestinationSequence var4 = (DestinationSequence)getSequenceFromConversationInfo(var3, DestinationSequence.class);
               if (var4 != null) {
                  this._safManager.closeConversationOnReceivingSide(var3);
                  var4.destroy();
               }

               return var4;
            }
         } catch (Exception var5) {
            throw new RuntimeException(var5.toString(), var5);
         }
      } else {
         return null;
      }
   }

   public void putAll(Map<? extends String, ? extends DestinationSequence> var1) {
      throw new IllegalStateException("Not supported");
   }

   public void clear() {
      throw new IllegalStateException("Not supported");
   }

   public Set<String> keySet() {
      HashSet var1 = new HashSet();
      this.fillSeqIdSetFromSAF(var1);
      return var1;
   }

   protected void fillSeqIdSetFromSAF(Set<String> var1) {
      if (this._safManager != null) {
         Set var2 = this._safManager.getConversationNamesOnReceivingSide();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();

            try {
               DestinationSequence var5 = this.get(var4);
               if (var5 != null) {
                  var1.add((String)var4);
               }
            } catch (Exception var6) {
               WseeRmLogger.logUnexpectedException(var6.toString(), var6);
            }
         }
      }

   }

   public Collection<DestinationSequence> values() {
      Set var1 = this.keySet();
      ArrayList var2 = new ArrayList(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         DestinationSequence var5 = this.get(var4);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }

   public Set<Map.Entry<String, DestinationSequence>> entrySet() {
      throw new IllegalStateException("Not supported");
   }

   private class MyConversationLifecycleListener implements SAFManager.ConversationLifecycleListener {
      private MyConversationLifecycleListener() {
      }

      public void ack(SAFConversationInfo var1, long var2, long var4) {
      }

      public void addToCache(boolean var1, String var2, String var3, SAFConversationInfo var4, int var5) {
      }

      public void preClose(boolean var1, boolean var2, SAFConversationInfo var3) {
      }

      public void removeFromCache(boolean var1, String var2, String var3, SAFConversationInfo var4, int var5) {
         if (!var1) {
            DestinationSequence var6 = (DestinationSequence)SequenceSAFMap.getSequenceFromConversationInfo(var4, DestinationSequence.class);
            if (var6 != null) {
               DestinationSequenceManager.getInstance().terminateSequence(var6.getId());
            }

         }
      }

      // $FF: synthetic method
      MyConversationLifecycleListener(Object var2) {
         this();
      }
   }
}
