package weblogic.wsee.reliability2.store;

import com.sun.xml.ws.api.message.Packet;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.reliability.MessageRange;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.sequence.SourceMessageInfo;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.sender.api.ConversationNotFoundException;
import weblogic.wsee.sender.api.ConversationOptions;
import weblogic.wsee.sender.api.ConversationStatusCallback;
import weblogic.wsee.sender.api.Preferences;
import weblogic.wsee.sender.api.Resources;
import weblogic.wsee.sender.api.SendingService;
import weblogic.wsee.sender.api.SendingServiceException;
import weblogic.wsee.sender.api.SendingServiceFactory;

public class SourceSequenceMap extends TimedSequenceMap<SourceSequence> {
   private static final Logger LOGGER = Logger.getLogger(SourceSequenceMap.class.getName());
   private SourceSequenceSenderFactory _senderFactory = new SourceSequenceSenderFactory(this);
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void setSequenceTimerListener(TimedSequenceMap.SequenceTimerListener var1) {
      super.setSequenceTimerListener(new SequenceTimerListenerWrapper(var1));
   }

   protected SequenceStore<SourceSequence> getOrCreateSequenceStore(String var1) throws StoreException {
      return SourceSequenceStore.getOrCreateStore(var1, this);
   }

   protected void added(SourceSequence var1) {
      super.added(var1);
      if (!var1.isNonBuffered()) {
         try {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Adding new conversation " + var1.getId() + " to sender");
            }

            this.addOrContinueConversationForSequence(var1, true);
            this.updateConversationForSequence(var1);
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }
   }

   boolean startupSequence(SourceSequence var1) {
      super.startupSequence(var1);
      var1.addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1) {
            if (var1.getPropertyName().equals("State") && var1.getOldValue() == SequenceState.CREATING && var1.getNewValue() == SequenceState.CREATED && SourceSequenceMap.this._senderFactory != null) {
               SourceSequence var2 = (SourceSequence)var1.getSource();
               SourceSequenceMap.this._senderFactory.sequenceCreated(var2.getId());
            }

         }
      });
      if (var1.isNonBuffered()) {
         return true;
      } else {
         try {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Continuing conversation " + var1.getId() + " with sender");
            }

            this.addOrContinueConversationForSequence(var1, false);
            return true;
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }
   }

   protected void updated(SourceSequence var1, SourceSequence var2) {
      super.updated(var1, var2);
      if (!var1.isNonBuffered()) {
         try {
            this.updateConversationForSequence(var1);
         } catch (Exception var4) {
            throw new RuntimeException(var4.toString(), var4);
         }
      }
   }

   private static AuthenticatedSubject getCurrentSubject() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      return var0;
   }

   private void updateConversationForSequence(SourceSequence var1) throws SendingServiceException {
      if (SecurityServiceManager.isKernelIdentity(getCurrentSubject())) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bypassing update of sender conversation : " + var1.getId() + " destination Id: " + var1.getDestinationId() + " because we were invoked from a system thread, not a user thread. Only user threads can update the sender conversation.");
         }

      } else if (SequenceState.isTerminalState(var1.getState())) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bypassing update of sender conversation : " + var1.getId() + " destination Id: " + var1.getDestinationId() + " because it is in a terminal state: " + var1.getState());
         }

      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Updating current ack ranges to sender conversation : " + var1.getId() + " destination Id: " + var1.getDestinationId());
         }

         this.handleCurrentAckRanges(var1);
         List var2 = var1.getAndClearPendingRequests();
         if (SequenceState.isTerminalState(var1.getState())) {
            if (var2.size() > 0 && LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Rejecting " + var2.size() + " pending requests for sender conversation : " + var1.getId() + " destination Id: " + var1.getDestinationId() + " because the sequence is in a terminal state: " + var1.getState());
            }
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Adding pending requests to sender conversation : " + var1.getId() + " destination Id: " + var1.getDestinationId());
            }

            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               SourceMessageInfo var4 = (SourceMessageInfo)var3.next();
               Packet var5 = var4.getRequestPacket();
               if (var5 != null) {
                  this.storeSendRequest(var1, var4);
               }
            }
         }

      }
   }

   private void addOrContinueConversationForSequence(SourceSequence var1, boolean var2) throws SendingServiceException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine((var2 ? "Adding" : "Continuing") + " conversation " + var1 + " with sender");
      }

      SendingService var3 = this.getSendingServiceForSequence(var1);
      ConversationOptions var4 = new ConversationOptions();
      var4.setBaseRetransmissionInterval(var1.getBaseRetransmissionInterval());
      var4.setExpires(var1.getExpires());
      var4.setExponentialBackoffEnabled(var1.isExponentialBackoffEnabled());
      var4.setIdleTimeout(var1.getIdleTimeout());
      var4.setInOrder(var1.getDeliveryAssurance().isInOrder());
      Resources var5 = new Resources(this._senderFactory, var4);

      try {
         boolean var10 = var3.conversationExists(var1.getId());
         if (var2) {
            if (!var10) {
               var3.addConversation(var1.getId(), var5);
            } else {
               var3.continueConversation(var1.getId(), var5);
            }
         } else if (!var10) {
            var3.continueConversation(var1.getId(), var5);
         }
      } catch (Throwable var9) {
         Throwable var6 = var9;

         try {
            ConversationStatusCallback var7 = var5.getSenderFactory().getStatusCallback(var1.getId());
            if (var7 != null) {
               var7.conversationNotStarted(var1.getId(), Arrays.asList(var6));
            } else {
               if (LOGGER.isLoggable(Level.INFO)) {
                  LOGGER.log(Level.INFO, var6.toString(), var6);
               }

               WseeRmLogger.logUnexpectedException(var6.toString(), var6);
            }
         } catch (Throwable var8) {
            if (LOGGER.isLoggable(Level.INFO)) {
               LOGGER.log(Level.INFO, var9.toString(), var9);
            }

            WseeRmLogger.logUnexpectedException(var9.toString(), var8);
         }
      }

   }

   boolean shutdownSequence(SourceSequence var1) {
      if (var1 != null && !var1.isNonBuffered()) {
         try {
            SendingService var2 = this.getSendingServiceForSequence(var1);
            var2.stopConversation(var1.getId());
         } catch (SendingServiceException var4) {
            boolean var3 = false;
            if (WsUtil.hasRootCause(var4, ConversationNotFoundException.class) && SequenceState.isTerminalState(var1.getState())) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Ignoring failure to find conversation during stop of sender conversation: " + var1.getId() + " destination Id: " + var1.getDestinationId() + " because it is in a terminal state: " + var1.getState());
               }

               var3 = true;
            }

            if (!var3) {
               WseeRmLogger.logUnexpectedException(var4.toString(), var4);
            }
         }
      }

      return super.shutdownSequence(var1);
   }

   private void storeSendRequest(SourceSequence var1, SourceMessageInfo var2) throws SendingServiceException {
      if (var1.getEndpointEpr() != null && !var1.getEndpointEpr().isAnonymous()) {
         SendingService var3 = this.getSendingServiceForSequence(var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Storing request message " + var2.getMessageNum() + " as SendRequest on conversation: " + var1.getId() + " destination Id: " + var1.getDestinationId() + " message Id: " + var2.getMessageId());
         }

         if (LOGGER.isLoggable(Level.WARNING) && (var2.getRequestPacket() == null || var2.getRequestPacket().getMessage() == null)) {
            LOGGER.warning("Storing null/empty request message " + var2.getMessageNum() + " as SendRequest on conversation: " + var1.getId() + " destination Id: " + var1.getDestinationId() + " message Id: " + var2.getMessageId());
         }

         SourceSequenceSendRequest var4 = new SourceSequenceSendRequest(var2);
         var3.addRequest(var1.getId(), var4);
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bypassing buffering of request message " + var2.getMessageNum() + " msgId " + var2.getMessageId() + " because it has a null endpoint/to address");
         }

      }
   }

   private SendingService getSendingServiceForSequence(SourceSequence var1) throws SendingServiceException {
      Preferences var2 = new Preferences();
      var2.setLogicalStoreName(var1.getLogicalStoreName());
      return SendingServiceFactory.getSendingService(var2);
   }

   private void handleCurrentAckRanges(SourceSequence var1) throws SendingServiceException {
      SendingService var2 = this.getSendingServiceForSequence(var1);
      SortedSet var3 = var1.getAckRanges();

      MessageRange var5;
      for(Iterator var4 = var3.iterator(); var4.hasNext(); var2.acknowledgeRequests(var1.getId(), var5.lowerBounds, var5.upperBounds)) {
         var5 = (MessageRange)var4.next();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Ack'ing requests " + var5.lowerBounds + "->" + var5.upperBounds + " on conversation " + var1.getId() + " with sender");
         }
      }

   }

   private class SequenceTimerListenerWrapper implements TimedSequenceMap.SequenceTimerListener {
      private TimedSequenceMap.SequenceTimerListener _listener;

      private SequenceTimerListenerWrapper(TimedSequenceMap.SequenceTimerListener var2) {
         this._listener = var2;
      }

      public void sequenceExpiration(String var1) {
         SourceSequence var2 = (SourceSequence)SourceSequenceMap.this.get(var1);
         this._listener.sequenceExpiration(var1);
         this.notifySequenceTerminated(var2);
      }

      public void idleTimeout(String var1) {
         SourceSequence var2 = (SourceSequence)SourceSequenceMap.this.get(var1);
         this._listener.sequenceExpiration(var1);
         this.notifySequenceTerminated(var2);
      }

      private void notifySequenceTerminated(SourceSequence var1) {
         if (var1 != null) {
            try {
               SendingService var2 = SourceSequenceMap.this.getSendingServiceForSequence(var1);
               var2.cancelConversation(var1.getId());
            } catch (SendingServiceException var4) {
               boolean var3 = false;
               if (WsUtil.hasRootCause(var4, ConversationNotFoundException.class)) {
                  if (SourceSequenceMap.LOGGER.isLoggable(Level.FINE)) {
                     SourceSequenceMap.LOGGER.fine("Ignoring failure to find conversation during cancel of sender conversation: " + var1.getId() + " destination Id: " + var1.getDestinationId() + " because it is in a terminal state: " + var1.getState());
                  }

                  var3 = true;
               }

               if (!var3) {
                  WseeRmLogger.logUnexpectedException(var4.toString(), var4);
               }
            }

         }
      }

      // $FF: synthetic method
      SequenceTimerListenerWrapper(TimedSequenceMap.SequenceTimerListener var2, Object var3) {
         this(var2);
      }
   }
}
