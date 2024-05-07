package weblogic.wsee.util;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.xml.saaj.MessageFactoryImpl;
import weblogic.xml.saaj.SAAJMetaFactoryImpl;

public class WLMessageFactory {
   private static WLMessageFactory wlMessageFactory = new WLMessageFactory();
   private SAAJMetaFactoryImpl metaFactory = new SAAJMetaFactoryImpl();

   private WLMessageFactory() {
   }

   public static WLMessageFactory getInstance() {
      return wlMessageFactory;
   }

   public MessageFactory getMessageFactory(boolean var1) {
      try {
         return this.metaFactory.newMessageFactory(SaajUtil.getSoapProtocol(var1));
      } catch (SOAPException var3) {
         throw new ExceptionInInitializerError(var3);
      }
   }

   public static SOAPMessage createMessageWithStreamingAttachments() throws SOAPException {
      MessageFactoryImpl var0 = (MessageFactoryImpl)getInstance().getMessageFactory(false);

      try {
         return var0.createMessage(true, false);
      } catch (IOException var2) {
         throw new SOAPException(var2);
      }
   }

   public static SOAPMessage createMessageWithStreamingAttachments(MimeHeaders var0, InputStream var1, boolean var2) throws SOAPException {
      MessageFactoryImpl var3 = (MessageFactoryImpl)getInstance().getMessageFactory(var2);

      try {
         return var3.createMessage(var0, var1, true, false);
      } catch (IOException var5) {
         throw new SOAPException(var5);
      }
   }

   public static SOAPMessage createMessageWithStreamingAttachments(MimeHeaders var0, InputStream var1) throws SOAPException {
      return createMessageWithStreamingAttachments(var0, var1, false);
   }
}
