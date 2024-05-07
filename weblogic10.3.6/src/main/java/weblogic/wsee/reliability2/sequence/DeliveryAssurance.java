package weblogic.wsee.reliability2.sequence;

import java.io.Serializable;
import weblogic.wsee.reliability.WsrmConstants;

public class DeliveryAssurance implements Serializable {
   private static final long serialVersionUID = 1L;
   private WsrmConstants.DeliveryQOS _qos;
   private boolean _inOrder;

   public DeliveryAssurance() {
      this._qos = WsrmConstants.DeliveryQOS.ExactlyOnce;
      this._inOrder = true;
   }

   public DeliveryAssurance(WsrmConstants.DeliveryQOS var1, boolean var2) {
      this._qos = var1;
      this._inOrder = var2;
   }

   public WsrmConstants.DeliveryQOS getQos() {
      return this._qos;
   }

   public void setQos(WsrmConstants.DeliveryQOS var1) {
      this._qos = var1;
   }

   public boolean isInOrder() {
      return this._inOrder;
   }

   public void setInOrder(boolean var1) {
      this._inOrder = var1;
   }
}
