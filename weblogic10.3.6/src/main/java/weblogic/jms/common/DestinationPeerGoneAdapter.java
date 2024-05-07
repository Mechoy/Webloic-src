package weblogic.jms.common;

import weblogic.jms.frontend.FEConnection;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.Dispatcher;

public final class DestinationPeerGoneAdapter implements JMSPeerGoneListener {
   private final DestinationImpl destination;
   private final FEConnection feConnection;
   private transient int refCount;

   public DestinationPeerGoneAdapter(DestinationImpl var1, FEConnection var2) {
      this.destination = var1;
      this.feConnection = var2;
   }

   public DestinationImpl getDestinationImpl() {
      return this.destination;
   }

   public int incrementRefCount() {
      return ++this.refCount;
   }

   public int decrementRefCount() {
      return --this.refCount;
   }

   public ID getId() {
      return this.destination.getId();
   }

   public void dispatcherPeerGone(Exception var1, Dispatcher var2) {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("DestinationImpl.jmsPeerGone()");
      }

      this.destination.dispatcherId = null;
      if (this.feConnection != null) {
         this.feConnection.removeTemporaryDestination(this.destination.destinationId);
         this.feConnection.getFrontEnd().removeBackEndDestination(this.destination);
      }

   }

   public int hashCode() {
      return this.destination.hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DestinationPeerGoneAdapter)) {
         return false;
      } else {
         DestinationPeerGoneAdapter var2 = (DestinationPeerGoneAdapter)var1;
         return this.destination.equals(var2.destination);
      }
   }
}
