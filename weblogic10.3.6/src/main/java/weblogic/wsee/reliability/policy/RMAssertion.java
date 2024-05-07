package weblogic.wsee.reliability.policy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.wsdl.WsdlWriter;

public class RMAssertion extends PolicyAssertion implements Externalizable {
   public static final QName RM_ASSERTION = new QName("http://schemas.xmlsoap.org/ws/2005/02/rm/policy", "RMAssertion");
   private InactivityTimeout inactivityTimeout;
   private AcknowledgementInterval ackInterval;
   private BaseRetransmissionInterval baseRetransmissionInterval;
   private ExponentialBackoff exponentialBackoff;
   private SequenceExpires seqExpires;
   private SequenceQOS seqQos;

   public InactivityTimeout getInactivityTimeout() {
      return this.inactivityTimeout;
   }

   public void setInactivityTimeout(InactivityTimeout var1) {
      this.inactivityTimeout = var1;
   }

   public AcknowledgementInterval getAckInterval() {
      return this.ackInterval;
   }

   public void setAckInterval(AcknowledgementInterval var1) {
      this.ackInterval = var1;
   }

   public BaseRetransmissionInterval getBaseRetransmissionInterval() {
      return this.baseRetransmissionInterval;
   }

   public void setBaseRetransmissionInterval(BaseRetransmissionInterval var1) {
      this.baseRetransmissionInterval = var1;
   }

   public ExponentialBackoff getExponentialBackoff() {
      return this.exponentialBackoff;
   }

   public void setExponentialBackoff(ExponentialBackoff var1) {
      this.exponentialBackoff = var1;
   }

   public SequenceExpires getSeqExpires() {
      return this.seqExpires;
   }

   public void setSeqExpires(SequenceExpires var1) {
      this.seqExpires = var1;
   }

   public SequenceQOS getSeqQos() {
      return this.seqQos;
   }

   public void setSeqQos(SequenceQOS var1) {
      this.seqQos = var1;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(RM_ASSERTION, var1);
      if (this.optional) {
         PolicyHelper.addOptionalAttribute(var2, this.getPolicyNamespaceUri());
      }

      if (this.inactivityTimeout != null) {
         var2.appendChild(this.inactivityTimeout.serialize(var1));
      }

      if (this.baseRetransmissionInterval != null) {
         var2.appendChild(this.baseRetransmissionInterval.serialize(var1));
      }

      if (this.exponentialBackoff != null) {
         var2.appendChild(this.exponentialBackoff.serialize(var1));
      }

      if (this.ackInterval != null) {
         var2.appendChild(this.ackInterval.serialize(var1));
      }

      if (this.seqExpires != null) {
         var2.appendChild(this.seqExpires.serialize(var1));
      }

      if (this.seqQos != null) {
         var2.appendChild(this.seqQos.serialize(var1));
      }

      return var2;
   }

   protected void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, RM_ASSERTION.getLocalPart(), "http://schemas.xmlsoap.org/ws/2005/02/rm/policy");
      if (this.optional) {
         PolicyHelper.addOptionalAttribute(var3, (String)null);
      }

      if (this.inactivityTimeout != null) {
         this.inactivityTimeout.write(var3, var2);
      }

      if (this.baseRetransmissionInterval != null) {
         this.baseRetransmissionInterval.write(var3, var2);
      }

      if (this.exponentialBackoff != null) {
         this.exponentialBackoff.write(var3, var2);
      }

      if (this.ackInterval != null) {
         this.ackInterval.write(var3, var2);
      }

      if (this.seqExpires != null) {
         this.seqExpires.write(var3, var2);
      }

      if (this.seqQos != null) {
         this.seqQos.write(var3, var2);
      }

   }

   private boolean checkEqual(Object var1, Object var2) {
      if (var1 == null) {
         return var2 == null;
      } else {
         return var2 == null ? false : var2.equals(var1);
      }
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof RMAssertion) {
         RMAssertion var2 = (RMAssertion)var1;
         if (!this.checkEqual(this.inactivityTimeout, var2.getInactivityTimeout())) {
            return false;
         } else if (!this.checkEqual(this.baseRetransmissionInterval, var2.getBaseRetransmissionInterval())) {
            return false;
         } else if (!this.checkEqual(this.exponentialBackoff, var2.getExponentialBackoff())) {
            return false;
         } else if (!this.checkEqual(this.ackInterval, var2.getAckInterval())) {
            return false;
         } else if (!this.checkEqual(this.seqExpires, var2.getSeqExpires())) {
            return false;
         } else {
            return this.checkEqual(this.seqQos, var2.getSeqQos());
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return RM_ASSERTION.hashCode() + (this.inactivityTimeout != null ? this.inactivityTimeout.hashCode() : 0) + (this.ackInterval != null ? this.ackInterval.hashCode() : 0) + (this.baseRetransmissionInterval != null ? this.baseRetransmissionInterval.hashCode() : 0) + (this.exponentialBackoff != null ? this.exponentialBackoff.hashCode() : 0) + (this.seqExpires != null ? this.seqExpires.hashCode() : 0) + (this.seqQos != null ? this.seqQos.hashCode() : 0);
   }

   public QName getName() {
      return RM_ASSERTION;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      int var2 = var1.readInt();
      if (var2 == 1) {
         this.inactivityTimeout = new InactivityTimeout();
         this.inactivityTimeout.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this.ackInterval = new AcknowledgementInterval();
         this.ackInterval.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this.baseRetransmissionInterval = new BaseRetransmissionInterval();
         this.baseRetransmissionInterval.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this.exponentialBackoff = new ExponentialBackoff();
         this.exponentialBackoff.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this.seqExpires = new SequenceExpires();
         this.seqExpires.readExternal(var1);
      }

      var2 = var1.readInt();
      if (var2 == 1) {
         this.seqQos = new SequenceQOS();
         this.seqQos.readExternal(var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      if (this.inactivityTimeout != null) {
         var1.writeInt(1);
         this.inactivityTimeout.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this.ackInterval != null) {
         var1.writeInt(1);
         this.ackInterval.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this.baseRetransmissionInterval != null) {
         var1.writeInt(1);
         this.baseRetransmissionInterval.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this.exponentialBackoff != null) {
         var1.writeInt(1);
         this.exponentialBackoff.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this.seqExpires != null) {
         var1.writeInt(1);
         this.seqExpires.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

      if (this.seqQos != null) {
         var1.writeInt(1);
         this.seqQos.writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

   }
}
