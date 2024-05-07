package weblogic.jms.extensions;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import weblogic.jms.common.BytesMessageImpl;
import weblogic.jms.common.HdrMessageImpl;
import weblogic.jms.common.MapMessageImpl;
import weblogic.jms.common.ObjectMessageImpl;
import weblogic.jms.common.StreamMessageImpl;
import weblogic.jms.common.TextMessageImpl;
import weblogic.jms.common.XMLMessageImpl;

public final class JMSMessageFactoryImpl implements WLMessageFactory {
   private static WLMessageFactory messageFactory = null;

   public static final synchronized WLMessageFactory getFactory() {
      if (messageFactory == null) {
         messageFactory = new JMSMessageFactoryImpl();
      }

      return messageFactory;
   }

   JMSMessageFactoryImpl() {
   }

   public final Message createMessage() {
      return new HdrMessageImpl();
   }

   public final Message createMessage(Document var1) throws DOMException, JMSException, IOException, ClassNotFoundException {
      try {
         Class var2 = Class.forName("weblogic.jms.common.XMLHelper");
         Method var6 = var2.getMethod("createMessage", Document.class);
         return (Message)var6.invoke((Object)null, var1);
      } catch (InvocationTargetException var4) {
         Throwable var3 = var4.getTargetException();
         if (var3 instanceof DOMException) {
            throw (DOMException)var3;
         } else if (var3 instanceof JMSException) {
            throw (JMSException)var3;
         } else if (var3 instanceof IOException) {
            throw (IOException)var3;
         } else if (var3 instanceof ClassNotFoundException) {
            throw (ClassNotFoundException)var3;
         } else {
            throw new AssertionError(var3);
         }
      } catch (Exception var5) {
         throw new AssertionError(var5);
      }
   }

   public final BytesMessage createBytesMessage() {
      return new BytesMessageImpl();
   }

   public final MapMessage createMapMessage() {
      return new MapMessageImpl();
   }

   public final ObjectMessage createObjectMessage() {
      return new ObjectMessageImpl();
   }

   public final ObjectMessage createObjectMessage(Serializable var1) throws JMSException {
      ObjectMessageImpl var2 = new ObjectMessageImpl();
      var2.setObject(var1);
      return var2;
   }

   public final StreamMessage createStreamMessage() {
      return new StreamMessageImpl();
   }

   public final TextMessage createTextMessage() {
      return new TextMessageImpl();
   }

   public final TextMessage createTextMessage(String var1) {
      return new TextMessageImpl(var1);
   }

   public final TextMessage createTextMessage(StringBuffer var1) {
      return new TextMessageImpl(var1.toString());
   }

   public final XMLMessage createXMLMessage() {
      return new XMLMessageImpl();
   }

   public final XMLMessage createXMLMessage(String var1) {
      return new XMLMessageImpl(var1);
   }

   public final XMLMessage createXMLMessage(Document var1) throws JMSException {
      return new XMLMessageImpl(var1);
   }
}
