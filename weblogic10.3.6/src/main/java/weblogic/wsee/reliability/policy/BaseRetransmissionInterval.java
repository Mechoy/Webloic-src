package weblogic.wsee.reliability.policy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.wsdl.WsdlWriter;

public class BaseRetransmissionInterval implements Externalizable {
   public static final QName BASE_RETRANSMISSION_INTERVAL = new QName("http://schemas.xmlsoap.org/ws/2005/02/rm/policy", "BaseRetransmissionInterval");
   public static final QName MILLISECONDS_ATTRIBUTE = new QName("Milliseconds");
   private long milliSeconds;

   public BaseRetransmissionInterval() {
   }

   public BaseRetransmissionInterval(long var1) {
      this.milliSeconds = var1;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(BASE_RETRANSMISSION_INTERVAL, var1);
      DOMUtils.addAttribute(var2, MILLISECONDS_ATTRIBUTE, Long.toString(this.milliSeconds));
      return var2;
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, BASE_RETRANSMISSION_INTERVAL.getLocalPart(), "http://schemas.xmlsoap.org/ws/2005/02/rm/policy");
      var2.setAttribute(var3, MILLISECONDS_ATTRIBUTE.getLocalPart(), (String)null, (String)Long.toString(this.milliSeconds));
   }

   public long getInterval() {
      return this.milliSeconds;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof BaseRetransmissionInterval) {
         BaseRetransmissionInterval var2 = (BaseRetransmissionInterval)var1;
         if (var2.getInterval() == this.getInterval()) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      return BASE_RETRANSMISSION_INTERVAL.hashCode() + (int)this.milliSeconds;
   }

   public QName getName() {
      return BASE_RETRANSMISSION_INTERVAL;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.milliSeconds = var1.readLong();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeLong(this.milliSeconds);
   }
}
