package weblogic.wsee.wsdl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.util.ToStringWriter;

public class WsdlWriter {
   private Element root;
   private Map namespaces = new HashMap();
   private int count = 0;
   private static final String PREFIX = "WL5G3N";
   HashSet<String> reservedPrefixes = new HashSet();
   private Document document;
   private String targetNS;
   private WsdlAddressInfo wsdlAddressInfo;
   private WsdlPort currentWsdlPort;
   private String relativeToRootFile = "";
   private Map attachedPolices = new LinkedHashMap();

   public WsdlWriter(Document var1, Element var2, String var3) {
      this.root = var2;
      this.document = var1;
      var2.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", var3);
      this.namespaces.put(var3, "");
   }

   public WsdlWriter() {
   }

   public void setWsdlAddressInfo(WsdlAddressInfo var1) {
      this.wsdlAddressInfo = var1;
   }

   public WsdlAddressInfo getWsdlAddressInfo() {
      return this.wsdlAddressInfo;
   }

   public void setCurrentWsdlPort(WsdlPort var1) {
      this.currentWsdlPort = var1;
   }

   public String getImportPrefix() {
      return this.wsdlAddressInfo != null ? this.wsdlAddressInfo.getImportPrefix() : null;
   }

   public String getEndpointURL(String var1) {
      return this.wsdlAddressInfo != null && this.currentWsdlPort != null ? this.wsdlAddressInfo.getServiceUrl(this.currentWsdlPort.getName(), var1) : null;
   }

   public String getTargetNS() {
      return this.targetNS;
   }

   public void setTargetNS(String var1) {
      this.targetNS = var1;
   }

   public String getRelativeToRootFile() {
      return this.relativeToRootFile;
   }

   public void setRelativeToRootFile(String var1) {
      this.relativeToRootFile = var1;
   }

   public void setAttachedPolices(Map var1) {
      this.attachedPolices = var1;
   }

   public Map getAttachedPolices() {
      return this.attachedPolices;
   }

   public void addPrefix(String var1, String var2) {
      if (var1.startsWith("WL5G3N")) {
         this.reservedPrefixes.add(var1);
      }

      this.namespaces.put(var2, var1);
      this.root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var1, var2);
   }

   public String createPrefix(String var1) {
      String var2 = (String)this.namespaces.get(var1);
      if (var2 == null) {
         do {
            var2 = "WL5G3N" + this.count;
            ++this.count;
         } while(this.reservedPrefixes.contains(var2));

         this.addPrefix(var2, var1);
      } else if (var2.startsWith("WL5G3N") && !this.reservedPrefixes.contains(var2)) {
         this.reservedPrefixes.add(var2);
      }

      return var2;
   }

   public void setAttribute(Element var1, String var2, String var3, QName var4) {
      String var5 = this.createPrefix(var4.getNamespaceURI());
      if ("".equals(var5)) {
         this.setAttribute(var1, var2, var3, var4.getLocalPart());
      } else {
         this.setAttribute(var1, var2, var3, var5 + ":" + var4.getLocalPart());
      }

   }

   public void setAttribute(Element var1, String var2, String var3, String var4) {
      if (var3 != null && !WsdlConstants.wsdlNS.equals(var3)) {
         String var5 = this.createPrefix(var3);
         if ("".equals(var5)) {
            var1.setAttributeNS(var3, var2, var4);
         } else {
            var1.setAttributeNS(var3, var5 + ":" + var2, var4);
         }
      } else {
         var1.setAttribute(var2, var4);
      }

   }

   public Element addChild(Element var1, String var2, String var3) {
      String var4 = this.createPrefix(var3);
      Element var5;
      if ("".equals(var4)) {
         var5 = this.document.createElementNS(var3, var2);
      } else {
         var5 = this.document.createElementNS(var3, var4 + ":" + var2);
      }

      var1.appendChild(var5);
      return var5;
   }

   public void addText(Element var1, String var2) {
      if (var2 != null) {
         var1.appendChild(this.document.createTextNode(var2));
      }

   }

   public boolean isSameNS(String var1) {
      if (this.targetNS != null && !this.targetNS.equals("")) {
         return this.targetNS.equals(var1);
      } else {
         return var1 == null || var1.equals("");
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
