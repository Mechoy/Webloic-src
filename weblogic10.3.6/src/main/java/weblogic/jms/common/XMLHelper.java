package weblogic.jms.common;

import com.bea.wls.jms.message.BooleanType;
import com.bea.wls.jms.message.ByteType;
import com.bea.wls.jms.message.BytesType;
import com.bea.wls.jms.message.CharType;
import com.bea.wls.jms.message.DestinationType;
import com.bea.wls.jms.message.DoubleType;
import com.bea.wls.jms.message.FloatType;
import com.bea.wls.jms.message.IntType;
import com.bea.wls.jms.message.LongType;
import com.bea.wls.jms.message.MapBodyType;
import com.bea.wls.jms.message.PropertyType;
import com.bea.wls.jms.message.ShortType;
import com.bea.wls.jms.message.StreamBodyType;
import com.bea.wls.jms.message.StringType;
import com.bea.wls.jms.message.WLJMSMessageDocument;
import com.bea.xml.SchemaType;
import com.bea.xml.XmlCursor;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import com.bea.xml.XmlObject.Factory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.jms.extensions.WLMessage;
import weblogic.xml.jaxp.WebLogicDocumentBuilderFactory;

public class XMLHelper {
   public static Document getDocument(WLMessage var0) throws javax.jms.JMSException {
      return getDocument(var0, (List)null, (List)null, true);
   }

   public static String getXMLText(WLMessage var0, boolean var1) throws javax.jms.JMSException {
      WLJMSMessageDocument var2 = getWLJMSMessageDocument(var0, (List)null, (List)null, var1);
      if (var2 == null) {
         return null;
      } else {
         XmlOptions var3 = new XmlOptions();
         var3.setSavePrettyPrint();
         return var2.xmlText(var3);
      }
   }

   public static String getXMLText(WLMessage var0) throws javax.jms.JMSException {
      return getXMLText(var0, true);
   }

   static Document getDocument(WLMessage var0, List var1, List var2, boolean var3) throws javax.jms.JMSException {
      WLJMSMessageDocument var4 = getWLJMSMessageDocument(var0, var1, var2, var3);
      return var4 == null ? null : (Document)var4.newDomNode();
   }

   public static WLMessage createMessage(Document var0) throws javax.jms.JMSException, IOException, ClassNotFoundException {
      Object var1 = null;

      try {
         WLJMSMessageDocument var2 = WLJMSMessageDocument.Factory.parse((Node)var0.getDocumentElement());
         return createMessage(var2);
      } catch (XmlException var3) {
         throw new JMSException(var3);
      }
   }

   public static WLMessage createMessage(String var0) throws javax.jms.JMSException, IOException, ClassNotFoundException {
      try {
         WLJMSMessageDocument var1 = WLJMSMessageDocument.Factory.parse(var0);
         return createMessage(var1);
      } catch (XmlException var2) {
         throw new JMSException(var2);
      }
   }

   private static WLMessage createMessage(WLJMSMessageDocument var0) throws javax.jms.JMSException, IOException, ClassNotFoundException {
      WLJMSMessageDocument.WLJMSMessage var1 = var0.getWLJMSMessage();
      WLJMSMessageDocument.WLJMSMessage.Header var2 = var1.getHeader();
      WLJMSMessageDocument.WLJMSMessage.Body var3 = var1.getBody();
      Object var4 = null;
      if (var3 == null) {
         var4 = new HdrMessageImpl();
         processHeader(var2, (WLMessage)var4);
      } else if (var3.isSetText()) {
         var4 = new TextMessageImpl();
         processHeader(var2, (WLMessage)var4);
         processTextBody(var3, (TextMessageImpl)var4);
      } else if (var3.isSetObject()) {
         var4 = new ObjectMessageImpl();
         processHeader(var2, (WLMessage)var4);
         processObjectBody(var3, (ObjectMessageImpl)var4);
      } else if (var3.isSetBytes()) {
         var4 = new BytesMessageImpl();
         processHeader(var2, (WLMessage)var4);
         processBytesBody(var3, (BytesMessageImpl)var4);
      } else if (var3.isSetStream()) {
         var4 = new StreamMessageImpl();
         processHeader(var2, (WLMessage)var4);
         processStreamBody(var3, (StreamMessageImpl)var4);
      } else if (var3.isSetMap()) {
         var4 = new MapMessageImpl();
         processHeader(var2, (WLMessage)var4);
         processMapBody(var3, (MapMessageImpl)var4);
      } else if (var3.isSetXML()) {
         var4 = new XMLMessageImpl();
         processHeader(var2, (WLMessage)var4);
         processXMLBody(var3, (XMLMessageImpl)var4);
      } else {
         var4 = new HdrMessageImpl();
         processHeader(var2, (WLMessage)var4);
      }

      return (WLMessage)var4;
   }

   public static Document parse(String var0) throws ParserConfigurationException, SAXException, IOException {
      WebLogicDocumentBuilderFactory var1;
      try {
         var1 = new WebLogicDocumentBuilderFactory();
      } catch (FactoryConfigurationError var6) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("XML Factory Configuration Error: " + var6.getMessage());
         }

         throw var6;
      }

      DocumentBuilder var2;
      try {
         var1.setNamespaceAware(true);
         var2 = var1.newDocumentBuilder();
      } catch (ParserConfigurationException var8) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("Can not create XML Document Builder: " + var8.getMessage());
         }

         throw var8;
      }

      InputSource var3 = new InputSource();
      var3.setCharacterStream(new StringReader(var0));
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("XMLMessageImpl.parse(): message :");
         JMSDebug.JMSCommon.debug(var0);
      }

      Document var4 = null;

      try {
         var4 = var2.parse(var3);
      } catch (SAXException var7) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("Input message is not a well-formed XML message: " + var7.getMessage());
         }

         throw var7;
      }

      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("Parsed DOM document is -" + var4 + "-");
      }

      return var4;
   }

   private static WLJMSMessageDocument getWLJMSMessageDocument(WLMessage var0, List var1, List var2, boolean var3) throws javax.jms.JMSException {
      MessageImpl var4 = (MessageImpl)var0;
      WLJMSMessageDocument var5 = WLJMSMessageDocument.Factory.newInstance();
      WLJMSMessageDocument.WLJMSMessage var6 = var5.addNewWLJMSMessage();
      createHeader(var6, var4, var1, var2);
      if (var3) {
         WLJMSMessageDocument.WLJMSMessage.Body var7 = var6.addNewBody();
         switch (var4.getType()) {
            case 1:
               createBytesBody(var7, (BytesMessageImpl)var4);
            case 2:
            default:
               break;
            case 3:
               createMapBody(var7, (MapMessageImpl)var4);
               break;
            case 4:
               createObjectBody(var7, (ObjectMessageImpl)var4);
               break;
            case 5:
               createStreamBody(var7, (StreamMessageImpl)var4);
               break;
            case 6:
               createTextBody(var7, (TextMessageImpl)var4);
               break;
            case 7:
               try {
                  createXMLBody(var7, (XMLMessageImpl)var4);
               } catch (XmlException var9) {
                  throw new JMSException(var9);
               }
         }
      }

      return var5;
   }

   private static JMSMessageId getMessageId(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, "<.>");
      if (!var1.hasMoreElements()) {
         return null;
      } else {
         String var2 = var1.nextToken();
         if (!var1.hasMoreElements()) {
            return null;
         } else {
            String var3 = var1.nextToken();
            String var4 = var1.nextToken();
            String var5 = var1.nextToken();
            return new JMSMessageId(Integer.parseInt(var3), Long.parseLong(var4), Integer.parseInt(var5));
         }
      }
   }

   private static void processHeader(WLJMSMessageDocument.WLJMSMessage.Header var0, WLMessage var1) throws javax.jms.JMSException {
      if (var0.isSetJMSMessageID()) {
         MessageImpl var2 = (MessageImpl)var1;
         String var3 = var0.getJMSMessageID();
         if (var3.contains("ID:N") || var3.contains("ID:P")) {
            var2.setOldMessage(true);
         }

         var2.setId(getMessageId(var3));
      }

      if (var0.isSetJMSCorrelationID()) {
         var1.setJMSCorrelationID(var0.getJMSCorrelationID());
      }

      if (var0.isSetJMSDeliveryMode()) {
         WLJMSMessageDocument.WLJMSMessage.Header.JMSDeliveryMode.Enum var9 = var0.getJMSDeliveryMode();
         var1.setJMSDeliveryMode(var9.intValue() == 1 ? 2 : 1);
      }

      String var4;
      String var5;
      String var6;
      String var7;
      String var8;
      DestinationType var10;
      DestinationType.Destination var11;
      if (var0.isSetJMSDestination()) {
         var10 = var0.getJMSDestination();
         var11 = var10.getDestination();
         var4 = var11.getName();
         var5 = var11.getServerName();
         var6 = var11.getServerURL();
         var7 = var11.getApplicationName();
         var8 = var11.getModuleName();
         var1.setJMSDestination(new DestinationImpl((byte)0, var5, var4, var7, var8));
      }

      if (var0.isSetJMSExpiration()) {
         var1.setJMSExpiration(var0.getJMSExpiration());
      }

      if (var0.isSetJMSPriority()) {
         var1.setJMSPriority(var0.getJMSPriority());
      }

      if (var0.isSetJMSRedelivered()) {
         var1.setJMSRedelivered(var0.getJMSRedelivered());
      }

      if (var0.isSetJMSTimestamp()) {
         var1.setJMSTimestamp(var0.getJMSTimestamp());
      }

      if (var0.isSetJMSReplyTo()) {
         var10 = var0.getJMSReplyTo();
         var11 = var10.getDestination();
         var4 = var11.getName();
         var5 = var11.getServerName();
         var6 = var11.getServerURL();
         var7 = var11.getApplicationName();
         var8 = var11.getModuleName();
         var1.setJMSReplyTo(new DestinationImpl((byte)0, var5, var4, var7, var8));
      }

      if (var0.isSetJMSType()) {
         var1.setJMSType(var0.getJMSType());
      }

      if (var0.isSetProperties()) {
         processHeaderProperties(var0, var1);
      }

      ((MessageImpl)var1).includeJMSXDeliveryCount(true);
   }

   private static Element getBodyTypeElement(Node var0) {
      Node var1 = var0.getFirstChild();
      return var1 == null ? null : (Element)var1.getNextSibling();
   }

   private static void processXMLBody(WLJMSMessageDocument.WLJMSMessage.Body var0, XMLMessageImpl var1) throws javax.jms.JMSException {
      WLJMSMessageDocument.WLJMSMessage.Body.XML var2 = var0.getXML();
      XmlOptions var3 = new XmlOptions();
      var3.setUseDefaultNamespace();
      XmlCursor var4 = var2.newCursor();
      boolean var5 = var4.toFirstChild();
      String var6 = var4.xmlText(var3);
      var1.setText(var6);
   }

   private static void processMapBody(WLJMSMessageDocument.WLJMSMessage.Body var0, MapMessageImpl var1) throws javax.jms.JMSException {
      MapBodyType var2 = var0.getMap();
      MapBodyType.NameValue[] var3 = var2.getNameValueArray();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            MapBodyType.NameValue var5 = var3[var4];
            String var6 = var5.getName();
            if (var5.isSetBoolean()) {
               var1.setBoolean(var6, var5.getBoolean());
            } else if (var5.isSetByte()) {
               var1.setByte(var6, var5.getByte());
            } else if (var5.isSetBytes()) {
               var1.setBytes(var6, var5.getBytes());
            } else if (var5.isSetChar()) {
               var1.setChar(var6, var5.getChar().charAt(0));
            } else if (var5.isSetDouble()) {
               var1.setDouble(var6, var5.getDouble());
            } else if (var5.isSetFloat()) {
               var1.setFloat(var6, var5.getFloat());
            } else if (var5.isSetInt()) {
               var1.setInt(var6, var5.getInt().intValue());
            } else if (var5.isSetLong()) {
               var1.setLong(var6, var5.getLong());
            } else if (var5.isSetShort()) {
               var1.setShort(var6, var5.getShort());
            } else if (var5.isSetString()) {
               var1.setString(var6, var5.getString());
            } else {
               var1.setObject(var6, (Object)null);
            }
         }

      }
   }

   private static void processStreamBody(WLJMSMessageDocument.WLJMSMessage.Body var0, StreamMessageImpl var1) throws javax.jms.JMSException {
      StreamBodyType var2 = var0.getStream();
      XmlCursor var3 = var2.newCursor();

      while(var3.hasNextToken()) {
         XmlCursor.TokenType var4 = var3.toNextToken();
         if (var4.isStart()) {
            XmlObject var5 = var3.getObject();
            SchemaType var6 = var5.schemaType();
            if (var6.equals(BooleanType.type)) {
               var1.writeBoolean(((BooleanType)var5).getBooleanValue());
            } else if (var6.equals(ByteType.type)) {
               var1.writeByte(((ByteType)var5).getByteValue());
            } else if (var6.equals(BytesType.type)) {
               var1.writeBytes(((BytesType)var5).getByteArrayValue());
            } else if (var6.equals(CharType.type)) {
               var1.writeChar(((CharType)var5).getStringValue().charAt(0));
            } else if (var6.equals(DoubleType.type)) {
               var1.writeDouble(((DoubleType)var5).getDoubleValue());
            } else if (var6.equals(FloatType.type)) {
               var1.writeFloat(((FloatType)var5).getFloatValue());
            } else if (var6.equals(IntType.type)) {
               var1.writeInt(((IntType)var5).getBigDecimalValue().intValue());
            } else if (var6.equals(LongType.type)) {
               var1.writeLong(((LongType)var5).getLongValue());
            } else if (var6.equals(ShortType.type)) {
               var1.writeShort(((ShortType)var5).getShortValue());
            } else {
               if (!var6.equals(StringType.type)) {
                  throw new javax.jms.JMSException("Invalid stream body type " + var6);
               }

               var1.writeString(((StringType)var5).getStringValue());
            }
         }
      }

   }

   private static void processBytesBody(WLJMSMessageDocument.WLJMSMessage.Body var0, BytesMessageImpl var1) throws javax.jms.JMSException {
      byte[] var2 = var0.getBytes();
      if (var2 != null) {
         var1.writeBytes(var2);
      }

   }

   private static void processObjectBody(WLJMSMessageDocument.WLJMSMessage.Body var0, ObjectMessageImpl var1) throws javax.jms.JMSException, IOException, ClassNotFoundException {
      byte[] var2 = var0.getObject();
      ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
      ObjectMessageImpl.ObjectInputStream2 var4 = var1.new ObjectInputStream2(var3);
      var1.setObject((Serializable)var4.readObject());
   }

   private static void processTextBody(WLJMSMessageDocument.WLJMSMessage.Body var0, TextMessageImpl var1) throws javax.jms.JMSException {
      var1.setText(var0.getText());
   }

   private static void processHeaderProperties(WLJMSMessageDocument.WLJMSMessage.Header var0, WLMessage var1) throws javax.jms.JMSException {
      PropertyType var2 = var0.getProperties();
      PropertyType.Property[] var3 = var2.getPropertyArray();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            PropertyType.Property var5 = var3[var4];
            String var6 = var5.getName();
            if (var5.isSetString()) {
               var1.setStringProperty(var6, var5.getString());
            } else if (var5.isSetLong()) {
               var1.setLongProperty(var6, var5.getLong());
            } else if (var5.isSetShort()) {
               var1.setShortProperty(var6, var5.getShort());
            } else if (var5.isSetInt()) {
               var1.setIntProperty(var6, var5.getInt().intValue());
            } else if (var5.isSetFloat()) {
               var1.setFloatProperty(var6, var5.getFloat());
            } else if (var5.isSetDouble()) {
               var1.setDoubleProperty(var6, var5.getDouble());
            } else if (var5.isSetByte()) {
               var1.setByteProperty(var6, var5.getByte());
            } else if (var5.isSetBoolean()) {
               var1.setBooleanProperty(var6, var5.getBoolean());
            } else {
               var1.setObjectProperty(var6, (Object)null);
            }
         }

      }
   }

   private static void createHeader(WLJMSMessageDocument.WLJMSMessage var0, MessageImpl var1, List var2, List var3) throws javax.jms.JMSException {
      WLJMSMessageDocument.WLJMSMessage.Header var4 = var0.addNewHeader();
      String var5;
      if (var2 == null || var2.contains("JMSMessageID")) {
         var5 = var1.getJMSMessageID();
         if (var5 != null) {
            var4.setJMSMessageID(var5);
         }
      }

      if (var2 == null || var2.contains("JMSCorrelationID")) {
         var5 = var1.getJMSCorrelationID();
         if (var5 != null) {
            var4.setJMSCorrelationID(var5);
         }
      }

      if (var2 == null || var2.contains("JMSType")) {
         var5 = var1.getJMSType();
         if (var5 != null) {
            var4.setJMSType(var5);
         }
      }

      if (var2 == null || var2.contains("JMSDeliveryMode")) {
         var4.setJMSDeliveryMode(var1.getJMSDeliveryMode() == 2 ? WLJMSMessageDocument.WLJMSMessage.Header.JMSDeliveryMode.PERSISTENT : WLJMSMessageDocument.WLJMSMessage.Header.JMSDeliveryMode.NON_PERSISTENT);
      }

      DestinationType var6;
      DestinationType.Destination var7;
      DestinationImpl var8;
      if (var2 == null || var2.contains("JMSDestination")) {
         var8 = var1.getDestination();
         if (var8 != null) {
            var6 = var4.addNewJMSDestination();
            var7 = var6.addNewDestination();
            var7.setName(var8.getDestinationName());
            var7.setServerName(var8.getServerName());
            var7.setApplicationName(var8.getApplicationName());
            var7.setModuleName(var8.getModuleName());
         }
      }

      if (var2 == null || var2.contains("JMSExpiration")) {
         var4.setJMSExpiration(var1.getJMSExpiration());
      }

      if (var2 == null || var2.contains("JMSPriority")) {
         var4.setJMSPriority(var1.getJMSPriority());
      }

      if (var2 == null || var2.contains("JMSRedelivered")) {
         var4.setJMSRedelivered(var1.getJMSRedelivered());
      }

      if (var2 == null || var2.contains("JMSTimestamp")) {
         var4.setJMSTimestamp(var1.getJMSTimestamp());
      }

      if (var2 == null || var2.contains("JMSReplyTo")) {
         var8 = (DestinationImpl)var1.getJMSReplyTo();
         if (var8 != null) {
            var6 = var4.addNewJMSReplyTo();
            var7 = var6.addNewDestination();
            var7.setName(var8.getDestinationName());
            var7.setServerName(var8.getServerName());
            var7.setApplicationName(var8.getApplicationName());
            var7.setModuleName(var8.getModuleName());
         }
      }

      createHeaderProperties(var4, var1, var3);
   }

   private static void createHeaderProperties(WLJMSMessageDocument.WLJMSMessage.Header var0, MessageImpl var1, List var2) throws javax.jms.JMSException {
      Enumeration var3 = null;
      boolean var4 = var1.includeJMSXDeliveryCount(true);

      try {
         var3 = var1.getPropertyNames();
      } finally {
         var1.includeJMSXDeliveryCount(var4);
      }

      if (var3 != null && var3.hasMoreElements()) {
         PropertyType var5 = var0.addNewProperties();

         while(true) {
            String var6;
            do {
               if (!var3.hasMoreElements()) {
                  return;
               }

               var6 = (String)var3.nextElement();
            } while(var2 != null && !var2.contains("%properties%") && !var2.contains(var6));

            PropertyType.Property var7 = var5.addNewProperty();
            var7.setName(var6);
            Object var8 = var1.getObjectProperty(var6);
            if (var8 instanceof String) {
               var7.setString((String)var8);
            } else if (var8 instanceof Long) {
               var7.setLong((Long)var8);
            } else if (var8 instanceof Short) {
               var7.setShort((Short)var8);
            } else if (var8 instanceof Integer) {
               var7.setInt(BigInteger.valueOf(((Integer)var8).longValue()));
            } else if (var8 instanceof Float) {
               var7.setFloat((Float)var8);
            } else if (var8 instanceof Double) {
               var7.setDouble((Double)var8);
            } else if (var8 instanceof Byte) {
               var7.setByte((Byte)var8);
            } else if (var8 instanceof Boolean) {
               var7.setBoolean((Boolean)var8);
            }
         }
      }
   }

   private static void createTextBody(WLJMSMessageDocument.WLJMSMessage.Body var0, TextMessageImpl var1) throws javax.jms.JMSException {
      var0.setText(var1.getText());
   }

   private static void createStreamBody(WLJMSMessageDocument.WLJMSMessage.Body var0, StreamMessageImpl var1) throws javax.jms.JMSException {
      StreamBodyType var2 = var0.addNewStream();
      StreamMessageImpl var3 = (StreamMessageImpl)var1.copy();

      while(true) {
         Object var4;
         try {
            var4 = var3.readObject();
         } catch (MessageEOFException var6) {
            return;
         }

         if (var4 != null) {
            if (var4 instanceof byte[]) {
               var2.addBytes((byte[])((byte[])var4));
            } else if (var4 instanceof Number) {
               if (var4 instanceof Byte) {
                  var2.addByte((Byte)var4);
               } else if (var4 instanceof Short) {
                  var2.addShort((Short)var4);
               } else if (var4 instanceof Integer) {
                  var2.addInt(BigInteger.valueOf(((Integer)var4).longValue()));
               } else if (var4 instanceof Long) {
                  var2.addLong((Long)var4);
               } else if (var4 instanceof Float) {
                  var2.addFloat((Float)var4);
               } else {
                  if (!(var4 instanceof Double)) {
                     throw new javax.jms.JMSException("Invalid Stream value type " + var4.getClass().getName());
                  }

                  var2.addDouble((Double)var4);
               }
            } else if (var4 instanceof Boolean) {
               var2.addBoolean((Boolean)var4);
            } else if (var4 instanceof String) {
               var2.addString((String)var4);
            } else {
               if (!(var4 instanceof Character)) {
                  throw new javax.jms.JMSException("Invalid Stream value type " + var4.getClass().getName());
               }

               var2.addChar(var4.toString());
            }
         }
      }
   }

   private static void createBytesBody(WLJMSMessageDocument.WLJMSMessage.Body var0, BytesMessageImpl var1) throws javax.jms.JMSException {
      BytesMessageImpl var2 = (BytesMessageImpl)var1.copy();
      var2.decompressMessageBody();
      byte[] var3 = var2.getBodyBytes();
      var0.setBytes(var3);
   }

   private static void createMapBody(WLJMSMessageDocument.WLJMSMessage.Body var0, MapMessageImpl var1) throws javax.jms.JMSException {
      MapBodyType var2 = var0.addNewMap();
      Enumeration var4 = var1.getMapNames();

      while(var4.hasMoreElements()) {
         String var5 = (String)var4.nextElement();
         MapBodyType.NameValue var6 = var2.addNewNameValue();
         var6.setName(var5);
         Object var3 = var1.getObject(var5);
         if (var3 != null) {
            if (var3 instanceof byte[]) {
               var6.setBytes((byte[])((byte[])var3));
            } else if (var3 instanceof Number) {
               if (var3 instanceof Byte) {
                  var6.setByte((Byte)var3);
               } else if (var3 instanceof Short) {
                  var6.setShort((Short)var3);
               } else if (var3 instanceof Integer) {
                  var6.setInt(BigInteger.valueOf(((Integer)var3).longValue()));
               } else if (var3 instanceof Long) {
                  var6.setLong((Long)var3);
               } else if (var3 instanceof Float) {
                  var6.setFloat((Float)var3);
               } else {
                  if (!(var3 instanceof Double)) {
                     throw new javax.jms.JMSException("Invalid Map value type " + var3.getClass().getName());
                  }

                  var6.setDouble((Double)var3);
               }
            } else if (var3 instanceof Boolean) {
               var6.setBoolean((Boolean)var3);
            } else if (var3 instanceof String) {
               var6.setString((String)var3);
            } else {
               if (!(var3 instanceof Character)) {
                  throw new javax.jms.JMSException("Invalid Map value type " + var3.getClass().getName());
               }

               var6.setChar(((Character)var3).toString());
            }
         }
      }

   }

   private static void createObjectBody(WLJMSMessageDocument.WLJMSMessage.Body var0, ObjectMessageImpl var1) throws javax.jms.JMSException {
      var1.decompressMessageBody();
      var0.setObject(var1.getBodyBytes());
   }

   private static void createXMLBody(WLJMSMessageDocument.WLJMSMessage.Body var0, XMLMessageImpl var1) throws XmlException, javax.jms.JMSException {
      WLJMSMessageDocument.WLJMSMessage.Body.XML var2 = var0.addNewXML();
      var2.set(Factory.parse(var1.getDocument()));
   }
}
