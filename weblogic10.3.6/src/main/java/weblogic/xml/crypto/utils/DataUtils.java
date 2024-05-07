package weblogic.xml.crypto.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.crypto.NodeSetDataImpl;
import weblogic.xml.crypto.OctetData;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.OctetStreamData;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.dom.Builder;
import weblogic.xml.dom.ElementNode;

public class DataUtils {
   public static Node getNode(Data var0) throws XMLSignatureException {
      Node var1 = null;
      if (var0 instanceof NodeSetData) {
         Iterator var2 = ((NodeSetData)var0).iterator();
         if (!var2.hasNext()) {
            return null;
         }

         var1 = (Node)var2.next();
         if (var2.hasNext()) {
            throw new XMLSignatureException("NodeSetData has more than one node.");
         }
      }

      return var1;
   }

   public static NodeSetData extractNodeSetData(Data var0) throws XMLSignatureException {
      if (var0 instanceof NodeSetData) {
         return (NodeSetData)var0;
      } else if (var0 instanceof OctetStreamData) {
         InputStream var3 = ((OctetStreamData)var0).getOctetStream();
         return getNodeSetData(var3);
      } else if (var0 instanceof OctetData) {
         OctetData var1 = (OctetData)var0;
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1.getBytes());
         return getNodeSetData(var2);
      } else {
         throw new XMLSignatureException("Unsupported Data object.");
      }
   }

   private static NodeSetData getNodeSetData(InputStream var0) throws XMLSignatureException {
      Node var1 = null;

      try {
         var1 = getNode(var0);
      } catch (XMLStreamException var3) {
         throw new XMLSignatureException("Failed to convert stream to node.", var3);
      }

      return new NodeSetDataImpl(DOMUtils.getNodeSet(var1, true));
   }

   public static InputStream getInputStream(Data var0) throws XMLSignatureException {
      InputStream var1 = null;
      if (var0 instanceof OctetStreamData) {
         var1 = ((OctetStreamData)var0).getOctetStream();
      }

      return var1;
   }

   private static Node getNode(InputStream var0) throws XMLStreamException {
      XMLInputFactory var1 = XMLInputFactory.newInstance();
      XMLStreamReader var2 = var1.createXMLStreamReader(var0);
      Builder.nextTag(var2);
      return Builder.read(new ElementNode(), var2);
   }

   public static byte[] getBytes(Data var0) throws XMLSignatureException, weblogic.xml.stream.XMLStreamException, IOException {
      if (var0 instanceof OctetData) {
         return ((OctetData)var0).getBytes();
      } else {
         if (var0 instanceof OctetStreamData) {
            InputStream var1 = ((OctetStreamData)var0).getOctetStream();
            if (var1 != null) {
               return getBytes(var1);
            }
         }

         throw new XMLSignatureException("Can not convert Data object " + var0 + " into byte[].");
      }
   }

   private static byte[] getBytes(InputStream var0) throws IOException {
      ByteVector var1 = new ByteVector();
      var1.addElements((InputStream)(new BufferedInputStream(var0)));
      return var1.minSizedElementArray();
   }

   public static String toString(Data var0) {
      if (!(var0 instanceof NodeSetData)) {
         if (var0 instanceof OctetStreamData) {
            return "[OctetStreamData, not reading it]";
         } else {
            return var0 instanceof OctetData ? new String("[OctetData, as String in platform default encoding:" + new String(((OctetData)var0).getBytes())) + "]" : "Unsupported Data object.";
         }
      } else {
         NodeSetData var1 = (NodeSetData)var0;
         StringBuffer var2 = new StringBuffer("[NodeSetData:");
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Node var4 = (Node)var3.next();
            String var5 = var4.getLocalName();
            if (var5 == null) {
               var5 = var4.getNodeName();
            } else {
               short var6 = var4.getNodeType();
               if (var6 == 1 || var6 == 2) {
                  var5 = var5 + "[" + var4.getPrefix() + "=" + var4.getNamespaceURI() + "]";
               }
            }

            var2.append("[Node:" + var5);
            var2.append("]");
         }

         var2.append("]");
         return var2.toString();
      }
   }
}
