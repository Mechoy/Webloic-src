package weblogic.wsee.wsdl.internal;

import javax.xml.namespace.QName;
import weblogic.wsee.wsdl.WsdlFilter;

class DefaultWsdlFilter implements WsdlFilter {
   public boolean isPortSupported(QName var1) {
      return true;
   }

   public boolean isMessagePartSupported(QName var1, String var2) {
      return true;
   }

   public String rewritePortUrl(QName var1, String var2, String var3, String var4, String var5, String var6) {
      return var2 + "://" + var3 + ":" + var4 + var5;
   }
}
