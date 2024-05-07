package weblogic.wsee.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;

public class WLSSOAPFaultException extends SOAPFaultException {
   public static final String SOAP_ENVELOPE_NS = "http://schemas.xmlsoap.org/soap/envelope/";

   public WLSSOAPFaultException(String var1, String var2, String var3, Throwable var4) {
      super(new QName("http://schemas.xmlsoap.org/soap/envelope/", var1), var3, var2, SOAPFaultUtil.newDetail(var4, false));
   }

   public WLSSOAPFaultException(QName var1, String var2, String var3, Detail var4) {
      super(var1, var2, var3, var4);
   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      var1.println("Detail:");
      var1.println(this.getDetail());
   }

   public void printStackTrace(PrintStream var1) {
      super.printStackTrace(var1);
      var1.println("Detail:");
      var1.println(this.getDetail());
   }
}
