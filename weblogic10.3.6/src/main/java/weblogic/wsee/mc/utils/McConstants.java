package weblogic.wsee.mc.utils;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import weblogic.webservice.core.soap.NameImpl;

public class McConstants {
   public static final String MC_ANONYMOUS = "/anonymous?id=";

   public static String getAnonymousURITemplate(McVersion var0) {
      return var0.getNamespaceUri() + "/anonymous?id=";
   }

   public static enum FaultCode {
      SENDER("Client", "Sender"),
      RECEIVER("Server", "Receiver");

      Map<SOAPVersion, String> versionToCodeMap = new HashMap();

      private FaultCode(String var3, String var4) {
         this.versionToCodeMap.put(McConstants.SOAPVersion.SOAP_11, var3);
         this.versionToCodeMap.put(McConstants.SOAPVersion.SOAP_12, var4);
      }

      public String getCodeLocalName(SOAPVersion var1) {
         return (String)this.versionToCodeMap.get(var1);
      }

      public String getCodeQualifiedName(SOAPVersion var1) {
         String var2 = (String)this.versionToCodeMap.get(var1);
         return var1.getPrefix() + ":" + var2;
      }

      public QName getCodeQName(SOAPVersion var1) {
         String var2 = (String)this.versionToCodeMap.get(var1);
         return new QName(var1.getNamespaceUri(), var2);
      }

      public Name getCodeName(SOAPVersion var1) {
         String var2 = (String)this.versionToCodeMap.get(var1);
         return new NameImpl(var2, var1.getPrefix(), var1.getNamespaceUri());
      }
   }

   public static enum Element {
      MC_SUPPORTED("MCSupported"),
      MC("MakeConnection"),
      ADDRESS("Address"),
      MISSING_SELECTION_FAULT("MissingSelection"),
      UNSUPPORTED_SELECTION_FAULT("UnsupportedSelection"),
      UNSUPPORTED_SELECTION("UnsupportedSelection"),
      MESSAGE_PENDING("MessagePending"),
      PENDING("pending");

      private String elementName;

      private Element(String var3) {
         this.elementName = var3;
      }

      public String getElementName() {
         return this.elementName;
      }

      public String getQualifiedName(McVersion var1) {
         return var1.getPrefix() + ":" + this.elementName;
      }

      public QName getQName(McVersion var1) {
         return new QName(var1.getNamespaceUri(), this.elementName, var1.getPrefix());
      }
   }

   public static enum Action {
      MC("MakeConnection"),
      FAULT("fault");

      String elementName;

      private Action(String var3) {
         this.elementName = var3;
      }

      public String getElementName() {
         return this.elementName;
      }

      public String getActionURI(McVersion var1) {
         return var1.getNamespaceUri() + "/" + this.elementName;
      }

      public boolean matchesAnyMCVersion(String var1) {
         McVersion[] var2 = McConstants.McVersion.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            McVersion var5 = var2[var4];
            if (this.getActionURI(var5).equals(var1)) {
               return true;
            }
         }

         return false;
      }

      public static boolean matchesAnyActionAndMCVersion(String var0) {
         Action[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Action var4 = var1[var3];
            if (var4.matchesAnyMCVersion(var0)) {
               return true;
            }
         }

         return false;
      }

      public static Action valueOfElementName(String var0) {
         Action[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Action var4 = var1[var3];
            if (var4.getElementName().equals(var0)) {
               return var4;
            }
         }

         throw new IllegalArgumentException("No enum in " + Action.class.getName() + " with elementName: " + var0);
      }
   }

   public static enum SOAPVersion {
      SOAP_11("soap", "http://schemas.xmlsoap.org/soap/envelope/"),
      SOAP_12("soap12", "http://www.w3.org/2003/05/soap-envelope");

      String prefix;
      String namespaceUri;

      private SOAPVersion(String var3, String var4) {
         this.prefix = var3;
         this.namespaceUri = var4;
      }

      public String getPrefix() {
         return this.prefix;
      }

      public String getNamespaceUri() {
         return this.namespaceUri;
      }
   }

   public static enum McVersion {
      MC_11("wsmc", "http://docs.oasis-open.org/ws-rx/wsmc/200702");

      String prefix;
      String namespaceUri;

      private McVersion(String var3, String var4) {
         this.prefix = var3;
         this.namespaceUri = var4;
      }

      public static McVersion latest() {
         return MC_11;
      }

      public static McVersion forNamespaceUri(String var0) {
         McVersion[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            McVersion var4 = var1[var3];
            if (var4.getNamespaceUri().equals(var0)) {
               return var4;
            }
         }

         return latest();
      }

      public String getPrefix() {
         return this.prefix;
      }

      public String getNamespaceUri() {
         return this.namespaceUri;
      }

      public boolean isLaterThan(McVersion var1) {
         return var1.ordinal() < this.ordinal();
      }

      public boolean isLaterThanOrEqualTo(McVersion var1) {
         return var1.ordinal() <= this.ordinal();
      }

      public boolean isBefore(McVersion var1) {
         return var1.ordinal() > this.ordinal();
      }
   }
}
