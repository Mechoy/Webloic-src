package weblogic.wsee.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.xml.saaj.SOAPMessageImpl;

public class SerializableSOAPMessage implements Serializable {
   private SOAPMessage msg = null;
   private AuthenticatedSubject subject = null;

   public SerializableSOAPMessage() {
   }

   public SerializableSOAPMessage(SOAPMessage var1) {
      if (!(var1 instanceof Externalizable)) {
         throw new JAXRPCException("Invalid SOAP message type: " + var1.getClass());
      } else {
         this.msg = var1;
      }
   }

   public SOAPMessage getSOAPMessage() {
      return this.msg;
   }

   public void setSubject(AuthenticatedSubject var1) {
      this.subject = var1;
   }

   public AuthenticatedSubject getSubject() {
      return this.subject;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      if (var2 > 0) {
         this.subject = (AuthenticatedSubject)var1.readObject();
      } else {
         this.subject = null;
      }

      int var3 = var1.readInt();
      if (var3 > 0) {
         boolean var4 = var1.readBoolean();
         MessageFactory var5 = WLMessageFactory.getInstance().getMessageFactory(var4);

         try {
            this.msg = var5.createMessage();
         } catch (SOAPException var7) {
            throw new IOException(var7.getMessage());
         }

         ((SOAPMessageImpl)this.msg).readExternal(var1);
      } else {
         this.msg = null;
      }

   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      if (this.subject != null) {
         var1.writeInt(1);
         var1.writeObject(this.subject);
      } else {
         var1.writeInt(0);
      }

      if (this.msg != null) {
         var1.writeInt(1);
         var1.writeBoolean(this.isSoap12());
         ((SOAPMessageImpl)this.msg).writeExternal(var1);
      } else {
         var1.writeInt(0);
      }

   }

   private boolean isSoap12() {
      try {
         return "http://www.w3.org/2003/05/soap-envelope".equals(this.msg.getSOAPPart().getEnvelope().getNamespaceURI());
      } catch (SOAPException var2) {
         return false;
      }
   }
}
