package weblogic.wsee.message.soap;

import java.io.IOException;
import java.util.Locale;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.jws.container.InvokeException;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.xml.saaj.MessageFactoryImpl;
import weblogic.xml.saaj.SOAPMessageImpl;
import weblogic.xml.saaj.util.HeaderUtils;

public class SoapMessageContext extends WlMessageContext implements SOAPMessageContext {
   private static final boolean verbose = Verbose.isVerbose(SoapMessageContext.class);
   protected SOAPMessage msg;
   private MessageFactory messageFactory;
   private boolean soap12;
   private boolean isMsgReset;
   private boolean _hasFault;

   public SoapMessageContext() {
      this(false);
   }

   public SoapMessageContext(boolean var1) {
      this.soap12 = false;
      this.isMsgReset = true;
      this.soap12 = var1;
      this.messageFactory = WLMessageFactory.getInstance().getMessageFactory(this.soap12);
      if (verbose) {
         Verbose.log((Object)"Created");
      }

   }

   public SOAPMessage getMessage() {
      if (this.msg == null) {
         try {
            this.msg = this.messageFactory.createMessage();
         } catch (SOAPException var2) {
            throw new JAXRPCException(var2);
         }
      }

      return this.msg;
   }

   public void setMessage(SOAPMessage var1) {
      if (verbose) {
         Verbose.log((Object)("set Message called: " + var1));
      }

      this.msg = var1;
      if (this.msg != null) {
         this.setHeaders(new SoapMsgHeaders(var1));
      }

      this.isMsgReset = true;
   }

   public String[] getRoles() {
      return new String[0];
   }

   public MessageFactory getMessageFactory() {
      return this.messageFactory;
   }

   public SOAPMessage clearMessage() {
      return this.clearMessage(false);
   }

   public SOAPMessage clearMessage(boolean var1) {
      if (verbose) {
         Verbose.log((Object)"Reset message");
      }

      try {
         SOAPMessage var2 = ((MessageFactoryImpl)this.messageFactory).createMessage(false, var1);
         String var3 = (String)this.getProperty("weblogic.wsee.client.xmlcharset");
         if (!StringUtil.isEmpty(var3) && !var3.toLowerCase(Locale.ENGLISH).equals("utf-8")) {
            this.setCharacterEncoding(var3, var1, var2);
         }

         this.setMessage(var2);
      } catch (SOAPException var4) {
         throw new JAXRPCException(var4);
      } catch (IOException var5) {
         throw new JAXRPCException(var5);
      }

      return this.msg;
   }

   private void setCharacterEncoding(String var1, boolean var2, SOAPMessage var3) {
      var2 = false;
      String var4 = HeaderUtils.constructContentTypeHeader((String)null, var1, (SOAPMessageImpl)var3, var2, this.soap12);
      var3.getMimeHeaders().setHeader("Content-Type", var4);

      try {
         var3.setProperty("javax.xml.soap.character-set-encoding", var1);
         var3.setProperty("javax.xml.soap.write-xml-declaration", "true");
      } catch (Exception var6) {
         throw new InvokeException("can't change soap message characterEncoding", var6);
      }
   }

   public boolean hasFault() {
      boolean var1 = super.hasFault();
      if (var1) {
         return true;
      } else if (this.getProperty("weblogic.wsee.ignore.fault") != null) {
         return false;
      } else {
         if (this.isMsgReset) {
            this.isMsgReset = false;
            SOAPMessage var2 = this.getMessage();

            try {
               if (var2 != null) {
                  SOAPBody var3 = var2.getSOAPBody();
                  if (var3 != null) {
                     this._hasFault = var3.hasFault();
                  }
               }
            } catch (SOAPException var4) {
               throw new JAXRPCException("Failed to call hasFault", var4);
            }
         }

         return this._hasFault;
      }
   }

   public void setFault(Throwable var1) {
      if (var1 == null && this.hasFault()) {
         this.clearMessage();
      }

      super.setFault(var1);
      if (var1 != null) {
         if (SOAPFaultUtil.isVersionMismatch(var1)) {
            this.setMessage(SOAPFaultUtil.createVersionMismatchMsg(this.soap12, var1));
         } else {
            this.setMessage(SOAPFaultUtil.exception2Fault(this.messageFactory, var1));
         }

      }
   }

   public boolean isSoap12() {
      return this.soap12;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("hasFault", "" + this.hasFault());
      var1.writeMap("properties", this.propertyMap);
      var1.end();
   }
}
