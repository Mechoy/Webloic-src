package weblogic.wsee.async;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.xml.domimpl.Saver;
import weblogic.xml.domimpl.SaverOptions;
import weblogic.xml.jaxp.WebLogicDocumentBuilderFactory;

public class AsyncWsdlFactory {
   public static final String WSDL_NAMESPACE_URI = "http://schemas.xmlsoap.org/wsdl/";

   public static byte[] create(byte[] var0) {
      try {
         ByteArrayInputStream var1 = new ByteArrayInputStream(var0);
         DocumentBuilderFactory var2 = WebLogicDocumentBuilderFactory.newInstance();
         var2.setNamespaceAware(true);
         DocumentBuilder var3 = var2.newDocumentBuilder();
         Document var4 = var3.parse(var1);
         NodeList var5 = var4.getDocumentElement().getChildNodes();

         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            Node var7 = var5.item(var6);
            if (var7.getNodeType() == 1) {
               Element var8 = (Element)var7;
               System.out.println("## Found element with local name: " + var8.getLocalName() + " and URI=" + var8.getNamespaceURI());
               if (("portType".equals(var8.getLocalName()) || "binding".equals(var8.getLocalName())) && "http://schemas.xmlsoap.org/wsdl/".equals(var8.getNamespaceURI())) {
                  System.out.println("Converting operations in the above element");
                  convertOperationsToAsync(var4, var8);
               }
            }
         }

         ByteArrayOutputStream var10 = new ByteArrayOutputStream();
         SaverOptions var11 = SaverOptions.getDefaults();
         var11.setPrettyPrint(true);
         var11.setWriteXmlDeclaration(true);
         Saver.save(var10, var4, var11);
         return var10.toByteArray();
      } catch (Exception var9) {
         throw new RuntimeException(var9.toString(), var9);
      }
   }

   private static void convertOperationsToAsync(Document var0, Element var1) {
      NodeList var2 = var1.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/", "operation");
      ArrayList var3 = new ArrayList();

      Element var5;
      for(int var4 = 0; var4 < var2.getLength(); ++var4) {
         var5 = (Element)var2.item(var4);
         System.out.println("Converting operation: " + var5.getAttribute("name"));
         Element var6 = null;
         Element var7 = null;
         NodeList var8 = var5.getChildNodes();

         for(int var9 = 0; var9 < var8.getLength(); ++var9) {
            Node var10 = var8.item(var9);
            System.out.println("Found child node of operation: " + var10.getLocalName());
            if (var10.getNodeType() == 1 && var10.getLocalName().equals("input")) {
               var6 = (Element)var10;
            } else if (var10.getNodeType() == 1 && var10.getLocalName().equals("output")) {
               var7 = (Element)var10;
            }
         }

         if (var7 == null) {
            var3.add(var5);
         } else {
            if (var6 == null) {
               String var15 = var7.getNamespaceURI();
               String var17 = var7.getPrefix();
               String var11;
               if (var17 != null) {
                  var11 = var17 + ":";
               } else {
                  var11 = "";
               }

               var11 = var11 + "input";
               var6 = var0.createElementNS(var15, var11);
               var5.insertBefore(var6, var7);
            } else {
               clearElement(var6);
            }

            NodeList var16 = var7.getChildNodes();

            for(int var18 = 0; var18 < var16.getLength(); ++var18) {
               Node var20 = var16.item(var18);
               var6.appendChild(var20);
            }

            NamedNodeMap var19 = var7.getAttributes();

            for(int var21 = 0; var21 < var19.getLength(); ++var21) {
               Attr var12 = (Attr)var19.item(var21);
               Attr var13 = (Attr)var12.cloneNode(true);
               var6.setAttributeNodeNS(var13);
            }

            var5.removeChild(var7);
         }
      }

      Iterator var14 = var3.iterator();

      while(var14.hasNext()) {
         var5 = (Element)var14.next();
         var1.removeChild(var5);
      }

   }

   private static void clearElement(Element var0) {
      NodeList var1 = var0.getChildNodes();

      for(int var2 = 0; var2 < var1.getLength(); ++var2) {
         Node var3 = var1.item(var2);
         var0.removeChild(var3);
      }

      NamedNodeMap var5 = var0.getAttributes();

      for(int var6 = 0; var6 < var5.getLength(); ++var6) {
         Attr var4 = (Attr)var5.item(var6);
         var0.removeAttributeNode(var4);
      }

   }

   public static void main(String[] var0) {
      if (var0.length != 2) {
         System.err.println("Usage: AsyncWsdlFactory <input WSDL file> <output WSDL file>");
         System.exit(1);
      }

      File var1 = new File(var0[0]);
      File var2 = new File(var0[1]);
      FileInputStream var3 = null;
      FileOutputStream var4 = null;
      byte var5 = 0;

      try {
         var3 = new FileInputStream(var1);
         ByteArrayOutputStream var6 = new ByteArrayOutputStream();
         byte[] var8 = new byte[2048];

         int var7;
         while((var7 = var3.read(var8)) > 0) {
            var6.write(var8, 0, var7);
         }

         byte[] var9 = create(var6.toByteArray());
         ByteArrayInputStream var10 = new ByteArrayInputStream(var9);
         var4 = new FileOutputStream(var2);

         while((var7 = var10.read(var8)) > 0) {
            var4.write(var8, 0, var7);
         }
      } catch (Exception var23) {
         var23.printStackTrace();
         var5 = 1;
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Exception var22) {
               var22.printStackTrace();
            }
         }

         if (var4 != null) {
            try {
               var4.close();
            } catch (Exception var21) {
               var21.printStackTrace();
            }
         }

      }

      System.exit(var5);
   }
}
