package weblogic.wsee.wsdl;

import javax.xml.namespace.QName;

public interface WsdlFilter {
   boolean isPortSupported(QName var1);

   boolean isMessagePartSupported(QName var1, String var2);

   String rewritePortUrl(QName var1, String var2, String var3, String var4, String var5, String var6);
}
