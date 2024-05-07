package weblogic.jms.backend;

import java.util.HashSet;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.TimedSecurityParticipant;
import weblogic.security.acl.internal.AuthenticatedSubject;

class BEProducerSecurityParticipantImpl implements TimedSecurityParticipant {
   private final int hashcode;
   private JMSID producerId;
   private BEDestinationImpl destination;
   private int lifeCount;
   private AuthenticatedSubject authenticatedSubject;

   BEProducerSecurityParticipantImpl(JMSID var1, BEDestinationImpl var2, int var3, AuthenticatedSubject var4) {
      this.producerId = var1;
      this.destination = var2;
      this.lifeCount = var3;
      this.authenticatedSubject = var4;
      int var5 = 19;
      var5 = 31 * var5 + (var1 != null ? var1.hashCode() : 0);
      var5 = 31 * var5 + (var2 != null ? var2.hashCode() : 0);
      this.hashcode = var5;
   }

   JMSID getProducerId() {
      return this.producerId;
   }

   public synchronized AuthenticatedSubject getSubject() {
      return this.authenticatedSubject;
   }

   synchronized void setSubject(AuthenticatedSubject var1) {
      if (var1 != null) {
         this.authenticatedSubject = var1;
      }

   }

   public void securityLapsed() {
      this.destination.removeProducer(this.producerId);
   }

   public boolean isClosed() {
      --this.lifeCount;
      if (this.lifeCount <= 0) {
         this.destination.removeProducer(this.producerId);
         return true;
      } else {
         return false;
      }
   }

   public HashSet getAcceptedDestinations() {
      return null;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof BEProducerSecurityParticipantImpl)) {
         return false;
      } else {
         boolean var10000;
         label48: {
            label32: {
               BEProducerSecurityParticipantImpl var2 = (BEProducerSecurityParticipantImpl)var1;
               if (var2.producerId != null) {
                  if (!var2.producerId.equals(this.producerId)) {
                     break label32;
                  }
               } else if (var2.producerId != this.producerId) {
                  break label32;
               }

               if (var2.destination != null) {
                  if (var2.destination.equals(this.destination)) {
                     break label48;
                  }
               } else if (var2.destination == this.destination) {
                  break label48;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public int hashCode() {
      return this.hashcode;
   }
}
