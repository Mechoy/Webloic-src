package weblogic.wsee.jws.wlw;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.Text;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlObject.Factory;
import org.w3c.dom.Node;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLSOAPFactory;

public class JwsSoapFaultHelper {
   public static final String URI_FAULT_CODE = "http://www.bea.com/2003/04/jwFaultCode/";
   public static final String SOAP_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";
   public static final String SOAP12_ENVELOPE;
   private static final Name DETAIL_KEY;

   public static void fillFault(SOAPFault var0, int var1, SoapFaultException var2) throws SOAPException {
      if (var0 == null) {
         throw new SOAPException("Cannot fill SOAPFault since it's null");
      } else {
         String var4;
         Method var5;
         Method var7;
         Method var9;
         XmlObject var22;
         Class var23;
         if (var1 == 1 && (var2.soapFaultVersion() == 1 || var2.soapFaultVersion() == 0)) {
            try {
               var22 = var2.getFault();
               if (var22 != null) {
                  var23 = getContextClassLoader().loadClass("weblogic.wsee.jws.wlw.schemas.soap11.Fault");
                  var5 = var23.getMethod("getFaultcode");
                  QName var24 = (QName)var5.invoke(var22);
                  var0.addNamespaceDeclaration("fault", var24.getNamespaceURI());
                  var0.setFaultCode("fault:" + var24.getLocalPart());
                  var7 = var23.getMethod("getFaultstring");
                  var0.setFaultString((String)var7.invoke(var22));
                  Method var25 = var23.getMethod("getFaultactor");
                  var0.setFaultActor((String)var25.invoke(var22));
                  var9 = var23.getMethod("getDetail");
                  XmlObject var26 = (XmlObject)var9.invoke(var22);
                  if (var26 != null) {
                     setDetailFromFault(var0, var26);
                  }
               } else {
                  var4 = var2.isCausedBySender() ? "Client" : "Server";
                  var0.addNamespaceDeclaration("fault", "http://schemas.xmlsoap.org/soap/envelope/");
                  var0.setFaultCode("fault:" + var4);
                  var0.setFaultString("Undefined");
                  if (var2.hasDetail()) {
                     setDetail(var0, var2.getDetail());
                  }
               }
            } catch (Exception var20) {
               throw new SOAPException(var20);
            }
         } else if (var1 == 2 && (var2.soapFaultVersion() == 2 || var2.soapFaultVersion() == 0)) {
            try {
               var22 = var2.getFault();
               if (var22 != null) {
                  var23 = getContextClassLoader().loadClass("weblogic.wsee.jws.wlw.schemas.soap12.Fault");
                  if (!var23.isAssignableFrom(var22.getClass())) {
                     throw new SOAPException("Unexpected content in wlw SoapFaultException: " + var22.getClass().getName());
                  }

                  var5 = var23.getMethod("getCode");
                  Object var6 = var5.invoke(var22);
                  var7 = var6.getClass().getMethod("getValue");
                  QName var8 = (QName)var7.invoke(var6);
                  var0.setFaultCode(var8);
                  var9 = var23.getMethod("getReason");
                  Object var10 = var9.invoke(var22);
                  Method var11 = var10.getClass().getMethod("getTextArray");
                  XmlString[] var12 = (XmlString[])((XmlString[])var11.invoke(var10));
                  XmlString[] var13 = var12;
                  int var14 = var12.length;

                  for(int var15 = 0; var15 < var14; ++var15) {
                     XmlString var16 = var13[var15];
                     Method var17 = var16.getClass().getMethod("getLang");
                     String var18 = (String)var17.invoke(var16);
                     Locale var19 = var18 == null ? Locale.getDefault() : new Locale(var18);
                     var0.addFaultReasonText(var16.getStringValue(), var19);
                  }

                  Method var27 = var23.getMethod("getRole");
                  var0.setFaultRole((String)var27.invoke(var22));
                  Method var28 = var23.getMethod("getDetail");
                  XmlObject var29 = (XmlObject)var28.invoke(var22);
                  if (var29 != null) {
                     setDetailFromFault(var0, var29);
                  }
               } else {
                  var4 = var2.isCausedBySender() ? "Sender" : "Receiver";
                  var0.setFaultCode(var4);
                  var0.addFaultReasonText("Undefined", Locale.US);
                  if (var2.hasDetail()) {
                     setDetail(var0, var2.getDetail());
                  }
               }
            } catch (Exception var21) {
               throw new SOAPException(var21);
            }
         } else {
            String var3 = "JWSError";
            var4 = "Wrong SOAP Fault version. SOAP Fault Version must be the same version with the SOAP protocol used. See com.bea.control.Context.getProtocol().";
            if (var0.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")) {
               var0.setFaultCode(var3);
               var0.addFaultReasonText(var4, Locale.US);
            } else {
               var0.addNamespaceDeclaration("fault", "http://www.bea.com/2003/04/jwFaultCode/");
               var0.setFaultCode("fault:" + var3);
               var0.setFaultString(var4);
            }
         }

      }
   }

   public static SoapFaultException createExceptionFromFault(SOAPFault var0, int var1) throws UnRecognizedFaultException {
      LinkedList var2;
      Detail var3;
      Class var4;
      Class var5;
      Method var6;
      XmlObject var7;
      Method var8;
      Method var9;
      if (var1 == 1) {
         var2 = null;
         var3 = var0.getDetail();
         if (var3 != null) {
            var2 = getDetailList(var3);
         }

         try {
            var4 = getContextClassLoader().loadClass("weblogic.wsee.jws.wlw.schemas.soap11.Fault$Factory");
            var5 = getContextClassLoader().loadClass("weblogic.wsee.jws.wlw.schemas.soap11.Fault");
            var6 = var4.getMethod("newInstance");
            var7 = (XmlObject)var6.invoke((Object)null);
            var8 = var5.getMethod("setFaultactor", String.class);
            var8.invoke(var7, var0.getFaultActor());
            var9 = var5.getMethod("setFaultstring", String.class);
            var9.invoke(var7, var0.getFaultString());
            Name var23 = var0.getFaultCodeAsName();
            QName var24 = new QName(var23.getURI(), var23.getLocalName(), var23.getPrefix());
            Method var25 = var5.getMethod("setFaultcode", QName.class);
            var25.invoke(var7, var24);
            if (var2 != null && var2.size() > 0) {
               Method var26 = var5.getMethod("addNewDetail");
               XmlObject var28 = (XmlObject)var26.invoke(var7);
               var28.set((XmlObject)var2.get(0));
            }

            return new SoapFaultException(var7);
         } catch (Exception var21) {
            throw new UnRecognizedFaultException(var21);
         }
      } else if (var1 == 2) {
         var2 = null;
         var3 = var0.getDetail();
         if (var3 != null) {
            var2 = getDetailList(var3);
         }

         try {
            var4 = getContextClassLoader().loadClass("weblogic.wsee.jws.wlw.schemas.soap12.Fault$Factory");
            var5 = getContextClassLoader().loadClass("weblogic.wsee.jws.wlw.schemas.soap12.Fault");
            var6 = var4.getMethod("newInstance");
            var7 = (XmlObject)var6.invoke((Object)null);
            var8 = var5.getMethod("setRole", String.class);
            var8.invoke(var7, var0.getFaultRole());
            var9 = var5.getMethod("addNewReason");
            Object var10 = var9.invoke(var7);
            Method var11 = var10.getClass().getMethod("addNewText");
            Iterator var12 = var0.getFaultReasonTexts();
            Iterator var13 = var0.getFaultReasonLocales();

            while(var12.hasNext()) {
               Locale var14 = Locale.getDefault();
               if (var13.hasNext()) {
                  var14 = (Locale)var13.next();
               }

               String var15 = var12.next().toString();
               XmlString var16 = (XmlString)var11.invoke(var10);
               var16.setStringValue(var15);
               Method var17 = var16.getClass().getMethod("setLang", String.class);
               var17.invoke(var16, var14.getCountry());
            }

            Name var27 = var0.getFaultCodeAsName();
            QName var29 = new QName(var27.getURI(), var27.getLocalName(), var27.getPrefix());
            Method var30 = var5.getMethod("addNewCode");
            Object var31 = var30.invoke(var7);
            Method var18 = var31.getClass().getMethod("setValue", QName.class);
            var18.invoke(var31, var29);
            if (var2 != null && var2.size() > 0) {
               Method var19 = var5.getMethod("addNewDetail");
               XmlObject var20 = (XmlObject)var19.invoke(var7);
               var20.set((XmlObject)var2.get(0));
            }

            return new SoapFaultException(var7);
         } catch (Exception var22) {
            var22.printStackTrace();
            throw new UnRecognizedFaultException(var22);
         }
      } else {
         throw new IllegalArgumentException("Unrecognized soap version");
      }
   }

   private static LinkedList<XmlObject> getDetailList(Detail var0) throws UnRecognizedFaultException {
      LinkedList var1 = new LinkedList();
      Iterator var2 = var0.getDetailEntries();

      boolean var3;
      do {
         if (!var2.hasNext()) {
            return var1;
         }

         var3 = false;
         Object var4 = var2.next();
         if (var4 instanceof Text) {
            Text var5 = (Text)var4;
            var1.add(getXmlObjectFromText(var5.getValue()));
            var3 = true;
         } else if (var4 instanceof DetailEntry) {
            DetailEntry var8 = (DetailEntry)var4;
            Node var6 = var8.getFirstChild();
            if (var6 != null && var6 instanceof Text) {
               Text var7 = (Text)var6;
               var1.add(getXmlObjectFromText(var7.getValue()));
               var3 = true;
            }
         }
      } while(var3);

      throw new UnRecognizedFaultException("Cannot create SoapFaultException");
   }

   private static final XmlObject getXmlObjectFromText(String var0) throws UnRecognizedFaultException {
      try {
         Verbose.log((Object)"****** About to unmarshal the following xml text: ");
         Verbose.log((Object)var0);
         return Factory.parse(var0);
      } catch (XmlException var2) {
         throw new UnRecognizedFaultException(var2);
      }
   }

   private static void setDetail(SOAPFault var0, XmlObject[] var1) throws SOAPException {
      if (var1 != null && var1.length > 0) {
         if (var0.getDetail() != null) {
            var0.getDetail().detachNode();
         }

         Detail var2 = var0.addDetail();
         XmlObject[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            XmlObject var6 = var3[var5];
            DetailEntry var7 = var2.addDetailEntry(DETAIL_KEY);
            var7.addTextNode(var6.xmlText());
            Verbose.log((Object)("***** Added following detail entry: " + var6.xmlText()));
         }
      }

   }

   private static void setDetailFromFault(SOAPFault var0, XmlObject var1) throws SOAPException {
      if (var1 != null) {
         if (var0.getDetail() != null) {
            var0.getDetail().detachNode();
         }

         Detail var2 = var0.addDetail();
         var2.addTextNode(var1.xmlText());
      }

   }

   private static final ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   static {
      String var0 = System.getProperty("com.bea.jws.soap12.env");
      if (var0 != null) {
         SOAP12_ENVELOPE = var0;
      } else {
         SOAP12_ENVELOPE = "http://www.w3.org/2003/05/soap-envelope";
      }

      Name var3 = null;

      try {
         var3 = WLSOAPFactory.createSOAPFactory().createName("WLW_FAULT_DETAIL");
      } catch (SOAPException var2) {
         Verbose.log("Unable to create Name for WLW_FAULT_DETAIL", var2);
      }

      if (var3 != null) {
         DETAIL_KEY = var3;
      } else {
         throw new RuntimeException("Unable to create Name for WLW_FAULT_DETAIL");
      }
   }
}
