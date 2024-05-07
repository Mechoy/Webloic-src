package weblogic.wsee.security.wssc.base.faults;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wst.faults.WSTFaultException;

public abstract class WSCFaultException extends WSTFaultException {
   private String prefix_wsc;
   private String xmlns_wsc;

   public WSCFaultException(String var1, String var2, String var3) {
      super(var1);
      this.prefix_wsc = var2;
      this.xmlns_wsc = var3;
   }

   public QName getFault() {
      return new QName(this.xmlns_wsc, this.faultCode, this.prefix_wsc);
   }
}
