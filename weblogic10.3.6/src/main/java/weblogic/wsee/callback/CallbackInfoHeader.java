package weblogic.wsee.callback;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class CallbackInfoHeader extends MsgHeader {
   private static final long serialVersionUID = 3753830341027224049L;
   private String stubName;
   private String serviceURI;
   private String appVersion;
   public static final QName NAME;
   public static final MsgHeaderType TYPE;
   private boolean roleRequired = false;

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public String getStubName() {
      return this.stubName;
   }

   public void setStubName(String var1) {
      this.stubName = var1;
   }

   public void setServiceURI(String var1) {
      this.serviceURI = var1;
   }

   public String getServiceURI() {
      return this.serviceURI;
   }

   public boolean isRoleRequired() {
      return this.roleRequired;
   }

   public void setRoleRequired(boolean var1) {
      this.roleRequired = var1;
   }

   public String getAppVersion() {
      return this.appVersion;
   }

   public void setAppVersion(String var1) {
      this.appVersion = var1;
   }

   public void write(Element var1) throws MsgHeaderException {
      if (this.stubName != null) {
         DOMUtils.addValueNS(var1, "http://www.openuri.org/2006/03/callback", "callback:StubName", this.stubName);
      }

      if (this.serviceURI != null) {
         DOMUtils.addValueNS(var1, "http://www.openuri.org/2006/03/callback", "callback:ServiceURI", this.serviceURI);
      }

      if (this.appVersion != null) {
         DOMUtils.addValueNS(var1, "http://www.openuri.org/2006/03/callback", "callback:AppVersion", this.appVersion);
      }

      if (this.roleRequired) {
         DOMUtils.addValueNS(var1, "http://www.openuri.org/2006/03/callback", "callback:RoleRequired", "true");
      }

   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.stubName = DOMUtils.getOptionalValueByTagNameNS(var1, "http://www.openuri.org/2006/03/callback", "StubName");
         this.serviceURI = DOMUtils.getOptionalValueByTagNameNS(var1, "http://www.openuri.org/2006/03/callback", "ServiceURI");
         this.appVersion = DOMUtils.getOptionalValueByTagNameNS(var1, "http://www.openuri.org/2006/03/callback", "AppVersion");
         String var2 = DOMUtils.getOptionalValueByTagNameNS(var1, "http://www.openuri.org/2006/03/callback", "RoleRequired");
         if ("true".equals(var2)) {
            this.roleRequired = true;
         } else {
            this.roleRequired = false;
         }

      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not read class name", var3);
      }
   }

   public String convertToWlw81StringForm() {
      StringBuffer var1 = new StringBuffer();
      if (this.stubName == null) {
         throw new JAXRPCException("No stub name found in callback information");
      } else if (this.serviceURI == null) {
         throw new JAXRPCException("No service URI found in callback information");
      } else {
         var1.append("stn=" + this.stubName + ";");
         var1.append("su=" + this.serviceURI + ";");
         if (this.appVersion != null) {
            var1.append("av=" + this.appVersion + ";");
         }

         if (this.roleRequired) {
            var1.append("rr=true");
         }

         return var1.toString();
      }
   }

   public void parseFromWlw81StringForm(String var1) {
      if (var1 == null) {
         throw new JAXRPCException("Input callback information is null");
      } else {
         int var2 = var1.indexOf("stn=");
         if (var2 < 0) {
            throw new JAXRPCException("No stub name found in callback information " + var1);
         } else {
            var2 += "stn=".length();
            int var3 = var1.indexOf(";", var2);
            if (var3 <= var2) {
               throw new JAXRPCException("No stub name value found in callback information " + var1);
            } else {
               this.stubName = var1.substring(var2, var3);
               var2 = var1.indexOf("su=", var3);
               if (var2 < 0) {
                  throw new JAXRPCException("No service URI found in callback information " + var1);
               } else {
                  var2 += "su=".length();
                  var3 = var1.indexOf(";", var2);
                  if (var3 <= var2) {
                     throw new JAXRPCException("No service URI value found in callback information " + var1);
                  } else {
                     this.serviceURI = var1.substring(var2, var3);
                     var2 = var1.indexOf("av=", var3);
                     if (var2 >= 0) {
                        var2 += "av=".length();
                        var3 = var1.indexOf(";", var2);
                        if (var3 <= var2) {
                           throw new JAXRPCException("No app version value found in callback information " + var1);
                        }

                        this.appVersion = var1.substring(var2, var3);
                     }

                     var2 = var1.indexOf("rr=", var3);
                     if (var2 >= 0) {
                        var2 += "rr=".length();
                        var3 = var1.indexOf(";", var2);
                        if (var3 <= var2) {
                           throw new JAXRPCException("No role required value found in callback information " + var1);
                        }

                        this.roleRequired = "true".equals(var1.substring(var2, var3));
                     }

                  }
               }
            }
         }
      }
   }

   static {
      NAME = CallbackConstants.CALLBACK_INFO_HEADER;
      TYPE = new MsgHeaderType();
   }
}
