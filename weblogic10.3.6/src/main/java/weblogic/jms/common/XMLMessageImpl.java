package weblogic.jms.common;

import java.io.CharArrayWriter;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.extensions.XMLMessage;
import weblogic.utils.StackTraceUtilsClient;
import weblogic.utils.io.StringInput;
import weblogic.utils.io.StringOutput;
import weblogic.xml.dom.DOMDeserializer;
import weblogic.xml.dom.DOMSerializer;
import weblogic.xml.stax.bin.BinaryXMLStreamReader;
import weblogic.xml.stax.bin.BinaryXMLStreamWriter;

public final class XMLMessageImpl extends MessageImpl implements XMLMessage, Externalizable {
   private static final long serialVersionUID = -7021112875012439613L;
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private static final byte EXTVERSION3 = 3;
   private static final byte VERSIONMASK = 127;
   private static final boolean mydebug = false;
   private String text;
   private PayloadStream payloadToken;
   private PayloadText payloadText;
   private byte subflag = 0;
   private WeakReference selectedDocument;

   public XMLMessageImpl() {
   }

   public XMLMessageImpl(String var1) {
      this.text = var1;
   }

   public XMLMessageImpl(Document var1) throws javax.jms.JMSException {
      this.setDocumentInternal(var1);
   }

   public void setText(String var1) throws javax.jms.JMSException {
      this.writeMode();
      this.text = var1;
      this.payloadToken = null;
      this.payloadText = null;
   }

   public String getText() throws javax.jms.JMSException {
      this.decompressMessageBody();
      if (this.text != null) {
         return this.text;
      } else if (this.payloadText != null) {
         try {
            this.text = this.payloadText.readUTF8();
            this.payloadText = null;
            return this.text;
         } catch (IOException var3) {
            throw new JMSException(var3);
         }
      } else {
         Document var1 = null;
         if (this.payloadToken != null) {
            try {
               var1 = detokenize(this.payloadToken);
            } catch (ParserConfigurationException var5) {
               throw serializationJMSException(var5);
            } catch (XMLStreamException var6) {
               throw serializationJMSException(var6);
            } catch (IOException var7) {
               throw new JMSException(var7);
            }
         }

         if (var1 != null) {
            try {
               this.text = serialize(var1);
            } catch (TransformerException var4) {
               throw serializationJMSException(var4);
            }

            return this.text;
         } else {
            return null;
         }
      }
   }

   public synchronized void setDocument(Document var1) throws javax.jms.JMSException {
      this.writeMode();
      this.text = null;
      this.payloadText = null;
      this.setDocumentInternal(var1);
   }

   private void setDocumentInternal(Document var1) throws javax.jms.JMSException {
      try {
         if (var1 == null) {
            this.payloadToken = null;
         } else {
            this.payloadToken = tokenize(var1);
         }

      } catch (XMLStreamException var3) {
         throw serializationJMSException(var3);
      } catch (IOException var4) {
         throw new JMSException(var4);
      }
   }

   public Document getDocument() throws javax.jms.JMSException {
      this.decompressMessageBody();
      if (this.payloadToken != null) {
         try {
            return detokenize(this.payloadToken);
         } catch (ParserConfigurationException var2) {
            throw deserializationJMSException(var2);
         } catch (XMLStreamException var3) {
            throw deserializationJMSException(var3);
         } catch (IOException var4) {
            throw new JMSException(var4);
         }
      } else {
         if (this.payloadText != null) {
            try {
               this.text = this.payloadText.readUTF8();
            } catch (IOException var8) {
               throw new JMSException(var8);
            }

            this.payloadText = null;
         }

         if (this.text != null) {
            try {
               return deserialize(this.text);
            } catch (ParserConfigurationException var5) {
               throw deserializationJMSException(var5);
            } catch (SAXException var6) {
               throw deserializationJMSException(var6);
            } catch (IOException var7) {
               throw deserializationJMSException(var7);
            }
         } else {
            return null;
         }
      }
   }

   public byte getType() {
      return 7;
   }

   public MessageImpl copy() throws javax.jms.JMSException {
      XMLMessageImpl var1 = new XMLMessageImpl();
      this.copy(var1);
      var1.text = this.text;
      if (this.payloadToken != null) {
         var1.payloadToken = this.payloadToken.copyPayloadWithoutSharedStream();
      }

      if (this.payloadText != null) {
         var1.payloadText = this.payloadText.copyPayloadWithoutSharedText();
      }

      var1.subflag = this.subflag;
      return var1;
   }

   public static Document deserialize(String var0) throws ParserConfigurationException, SAXException, IOException {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("XMLMessageImpl.parse(): message: " + var0);
      }

      DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
      DocumentBuilder var2 = var1.newDocumentBuilder();
      return var2.parse(new InputSource(new StringReader(var0)));
   }

   private static String serialize(Document var0) throws TransformerException {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("XMLMessageImpl.serialize(): " + var0);
      }

      CharArrayWriter var1 = new CharArrayWriter();
      TransformerFactory var2 = TransformerFactory.newInstance();
      Transformer var3 = var2.newTransformer();
      DOMSource var4 = new DOMSource(var0);
      StreamResult var5 = new StreamResult(var1);
      var3.transform(var4, var5);
      return var1.toString();
   }

   private static PayloadStream tokenize(Document var0) throws XMLStreamException, IOException {
      BufferOutputStream var1 = PayloadFactoryImpl.createOutputStream();
      BinaryXMLStreamWriter var2 = new BinaryXMLStreamWriter(var1);
      DOMSerializer.serialize(var0, var2);
      return (PayloadStream)var1.moveToPayload();
   }

   private static Document detokenize(PayloadStream var0) throws XMLStreamException, ParserConfigurationException, IOException {
      BufferInputStream var1 = var0.getInputStream();
      BinaryXMLStreamReader var2 = new BinaryXMLStreamReader(var1);
      return (Document)DOMDeserializer.deserialize(var2);
   }

   public Object parse() throws Exception {
      Document var1 = null;
      if (this.selectedDocument != null) {
         var1 = (Document)this.selectedDocument.get();
      }

      if (var1 != null) {
         return var1;
      } else {
         this.selectedDocument = null;
         var1 = this.getDocumentForSelection();
         if (var1 != null) {
            this.selectedDocument = new WeakReference(var1);
         }

         return var1;
      }
   }

   public void nullBody() {
      this.text = null;
      this.payloadText = null;
      this.payloadToken = null;
   }

   private static javax.jms.JMSException serializationJMSException(Exception var0) {
      javax.jms.JMSException var1 = new javax.jms.JMSException("failed to serialize message");
      var1.initCause(var0);
      return var1;
   }

   private static javax.jms.JMSException deserializationJMSException(Exception var0) {
      javax.jms.JMSException var1 = new javax.jms.JMSException("failed to deserialize message");
      var1.initCause(var0);
      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("XMLMessage[id=" + this.getJMSMessageID() + ",text=\n");

      try {
         var1.append(this.getText());
      } catch (javax.jms.JMSException var3) {
         var1.append(StackTraceUtilsClient.throwable2StackTrace(var3));
      }

      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      int var2 = Integer.MAX_VALUE;
      boolean var5 = true;
      ObjectOutput var3;
      if (var1 instanceof MessageImpl.JMSObjectOutputWrapper) {
         var3 = ((MessageImpl.JMSObjectOutputWrapper)var1).getInnerObjectOutput();
         var2 = ((MessageImpl.JMSObjectOutputWrapper)var1).getCompressionThreshold();
         var5 = ((MessageImpl.JMSObjectOutputWrapper)var1).getReadStringAsObject();
      } else {
         var3 = var1;
      }

      byte var4;
      if (var3 instanceof PeerInfoable) {
         PeerInfo var6 = ((PeerInfoable)var3).getPeerInfo();
         if (var6.getMajor() >= 9) {
            var4 = (byte)(3 | (this.shouldCompress(var3, var2) ? -128 : 0));
         } else {
            var4 = 2;
         }
      } else {
         var4 = 3;
      }

      var3.writeByte(var4);
      if ((var4 & 127) == 3) {
         if (this.isCompressed()) {
            var3.writeByte(this.subflag);
            this.flushCompressedMessageBody(var3);
         } else if ((var4 & -128) != 0) {
            if (this.payloadToken != null) {
               var3.writeByte(1);
               this.writeExternalCompressPayload(var3, this.payloadToken);
            } else if (this.payloadText != null) {
               var3.writeByte(2);
               this.writeExternalCompressPayload(var3, this.payloadText);
            } else {
               var3.writeByte(4);
               this.writeExternalCompressPayload(var3, PayloadFactoryImpl.convertObjectToPayload(this.text));
            }
         } else if (this.payloadToken != null) {
            var3.writeBoolean(true);
            var3.writeByte(1);
            this.payloadToken.writeLengthAndData(var3);
         } else if (this.text != null && this.text.length() > 0) {
            var3.writeBoolean(true);
            if (var3 instanceof StringOutput) {
               var3.writeByte(2 | (var5 ? 128 : 0));
               ((StringOutput)var3).writeUTF8(this.text);
            } else {
               var3.writeByte(4);
               var3.writeObject(this.text);
            }
         } else if (this.payloadText != null) {
            var3.writeBoolean(true);
            var3.writeByte(2 | (var5 ? 128 : 0));
            this.payloadText.writeLengthAndData(var3);
         } else {
            var3.writeBoolean(false);
         }
      } else {
         String var17 = null;
         PayloadText var7 = null;
         PayloadStream var8 = null;
         byte var9 = 0;
         if (this.isCompressed()) {
            var9 = this.subflag;
         } else if (this.text != null) {
            var17 = this.text;
         } else if (this.payloadText != null) {
            var7 = this.payloadText;
            var9 = 2;
         } else if (this.payloadToken != null) {
            var8 = this.payloadToken;
            var9 = 1;
         }

         if (var17 == null && (var7 != null || var8 != null)) {
            if ((var9 & 2) != 0) {
               var17 = this.payloadText.readUTF8();
            } else {
               BufferInputStream var10;
               if ((var9 & 4) != 0) {
                  try {
                     var10 = var7.getInputStream();
                     ObjectInputStream var19 = new ObjectInputStream(var10);
                     var17 = (String)var19.readObject();
                  } catch (ClassNotFoundException var16) {
                     IOException var11 = new IOException(JMSClientExceptionLogger.logErrorInteropXMLMessageLoggable().getMessage());
                     var11.initCause(var16);
                     throw var11;
                  }
               } else if ((var9 & 1) != 0) {
                  var10 = null;

                  IOException var12;
                  Document var18;
                  try {
                     var18 = detokenize(var8);
                  } catch (ParserConfigurationException var14) {
                     var12 = new IOException(JMSClientExceptionLogger.logErrorInteropXMLMessageLoggable().getMessage());
                     var12.initCause(var14);
                     throw var12;
                  } catch (XMLStreamException var15) {
                     var12 = new IOException(JMSClientExceptionLogger.logErrorInteropXMLMessageLoggable().getMessage());
                     var12.initCause(var15);
                     throw var12;
                  }

                  try {
                     var17 = serialize(var18);
                  } catch (TransformerException var13) {
                     var12 = new IOException(JMSClientExceptionLogger.logErrorInteropXMLMessageLoggable().getMessage());
                     var12.initCause(var13);
                     throw var12;
                  }
               }
            }
         }

         if (var17 != null && var17.length() > 0) {
            var3.writeBoolean(true);
            var3.writeObject(var17);
         } else {
            var3.writeBoolean(false);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      int var2 = var1.readByte();
      switch (var2 & 127) {
         case 1:
            this.text = var1.readUTF();
            break;
         case 2:
            if (var1.readBoolean()) {
               this.text = (String)var1.readObject();
            }
            break;
         case 3:
            if ((var2 & -128) != 0) {
               this.subflag = var1.readByte();
               this.readExternalCompressedMessageBody(var1);
            } else {
               boolean var3 = var1.readBoolean();
               if (var3) {
                  byte var4 = var1.readByte();
                  if ((var4 & 1) != 0) {
                     this.payloadToken = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
                  } else if ((var4 & 2) != 0) {
                     if (var1 instanceof StringInput && (var4 & 128) != 0) {
                        this.text = ((StringInput)var1).readUTF8();
                     } else {
                        this.payloadText = (PayloadText)PayloadFactoryImpl.createPayload((InputStream)var1);
                     }
                  } else if ((var4 & 4) != 0) {
                     this.text = (String)var1.readObject();
                  }
               }
            }
            break;
         default:
            throw JMSUtilities.versionIOException(var2, 1, 3);
      }

   }

   public long getPayloadSize() {
      if (super.bodySize != -1L) {
         return super.bodySize;
      } else if (this.isCompressed()) {
         return super.bodySize = (long)this.getCompressedMessageBodySize();
      } else if (this.text != null) {
         return super.bodySize = (long)this.text.length();
      } else if (this.payloadToken != null) {
         return super.bodySize = (long)this.payloadToken.getLength();
      } else {
         return this.payloadText != null ? (super.bodySize = (long)this.payloadText.getLength()) : (super.bodySize = 0L);
      }
   }

   public void decompressMessageBody() throws javax.jms.JMSException {
      if (this.isCompressed()) {
         try {
            Payload var1 = this.decompress();
            if ((this.subflag & 2) != 0) {
               this.text = ((PayloadText)var1).readUTF8();
            } else if ((this.subflag & 4) != 0) {
               ObjectInputStream var2 = new ObjectInputStream(var1.getInputStream());
               this.text = (String)var2.readObject();
            } else if ((this.subflag & 1) != 0) {
               this.payloadToken = (PayloadStream)var1;
            }

            this.cleanupCompressedMessageBody();
         } catch (IOException var3) {
            throw new JMSException(JMSClientExceptionLogger.logErrorDeserializeMessageBodyLoggable().getMessage(), var3);
         } catch (ClassNotFoundException var4) {
            throw new JMSException(JMSClientExceptionLogger.logErrorDeserializeMessageBodyLoggable().getMessage(), var4);
         }
      }
   }

   private Document getDocumentForSelection() throws javax.jms.JMSException {
      Document var1 = null;
      String var2 = this.text;
      PayloadStream var3 = this.payloadToken;
      PayloadText var4 = this.payloadText;
      if (this.isCompressed()) {
         try {
            Payload var5 = this.decompress();
            if ((this.subflag & 2) != 0) {
               var2 = ((PayloadText)var5).readUTF8();
            } else if ((this.subflag & 4) != 0) {
               ObjectInputStream var6 = new ObjectInputStream(var5.getInputStream());
               var2 = (String)var6.readObject();
            } else if ((this.subflag & 1) != 0) {
               var3 = (PayloadStream)var5;
            }
         } catch (IOException var14) {
            throw new JMSException(JMSClientExceptionLogger.logErrorDeserializeMessageBodyLoggable().getMessage(), var14);
         } catch (ClassNotFoundException var15) {
            throw new JMSException(JMSClientExceptionLogger.logErrorDeserializeMessageBodyLoggable().getMessage(), var15);
         }
      }

      if (var3 != null) {
         try {
            var1 = detokenize(var3);
         } catch (ParserConfigurationException var11) {
            throw deserializationJMSException(var11);
         } catch (XMLStreamException var12) {
            throw deserializationJMSException(var12);
         } catch (IOException var13) {
            throw deserializationJMSException(var13);
         }
      }

      if (var4 != null) {
         try {
            var2 = var4.readUTF8();
         } catch (IOException var10) {
            throw new JMSException(var10);
         }
      }

      if (var2 != null) {
         try {
            var1 = deserialize(var2);
         } catch (ParserConfigurationException var7) {
            throw deserializationJMSException(var7);
         } catch (SAXException var8) {
            throw deserializationJMSException(var8);
         } catch (IOException var9) {
            throw deserializationJMSException(var9);
         }
      }

      return var1;
   }
}
