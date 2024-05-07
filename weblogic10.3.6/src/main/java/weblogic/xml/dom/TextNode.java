package weblogic.xml.dom;

import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

public class TextNode extends ChildNode implements CharacterData, CDATASection, Text, Comment {
   private String dataCache;
   private StringBuffer modifiableData;

   public TextNode() {
      this.setNodeType((short)3);
   }

   public TextNode(String var1) {
      this();
      this.dataCache = var1;
   }

   public String getData() throws DOMException {
      return this.dataCache != null ? this.dataCache : this.modifiableData.toString();
   }

   public void setData(String var1) throws DOMException {
      this.dataCache = var1;
      this.modifiableData = null;
   }

   public int getLength() {
      if (this.dataCache != null) {
         return this.dataCache.length();
      } else {
         return this.modifiableData != null ? this.modifiableData.length() : 0;
      }
   }

   public String substringData(int var1, int var2) throws DOMException {
      if (this.dataCache != null) {
         return this.dataCache.substring(var1, var2);
      } else {
         return this.modifiableData != null ? this.modifiableData.substring(var1, var2) : null;
      }
   }

   private void modifyOnWrite() {
      if (this.dataCache != null) {
         this.modifiableData = new StringBuffer(this.dataCache);
         this.dataCache = null;
      }

   }

   public void appendData(String var1) throws DOMException {
      this.modifiableData.append(var1);
   }

   public void insertData(int var1, String var2) throws DOMException {
      this.modifiableData.insert(var1, var2);
   }

   public void deleteData(int var1, int var2) throws DOMException {
      this.modifiableData.delete(var1, var2);
   }

   public void replaceData(int var1, int var2, String var3) throws DOMException {
      this.modifiableData.replace(var1, var2, var3);
   }

   public Text splitText(int var1) throws DOMException {
      TextNode var2 = new TextNode();
      String var3 = this.substringData(0, var1);
      String var4 = this.substringData(var1, this.getLength() - var1);
      this.setData(var3);
      var2.setData(var4);
      this.getParentNode().insertBefore(var2, this.getNextSibling());
      return var2;
   }

   public String getNodeName() {
      switch (this.getNodeType()) {
         case 4:
            return "#cdata-section";
         case 8:
            return "#comment";
         default:
            return "#text";
      }
   }

   public String getNodeValue() {
      return this.getData();
   }

   public void setNodeValue(String var1) {
      this.setData(var1);
   }

   public Comment asComment() {
      this.setNodeType((short)8);
      return this;
   }

   public CDATASection asCDATA() {
      this.setNodeType((short)4);
      return this;
   }

   public void print(StringBuffer var1, int var2) {
      var1.append(this.toString());
   }

   public String toString() {
      String var1 = this.getData();
      if (var1 == null) {
         var1 = "";
      }

      switch (this.getNodeType()) {
         case 4:
            return "<![CDATA[" + var1 + "]]>";
         case 8:
            return "<!--" + var1 + "-->";
         default:
            return var1;
      }
   }

   public Text replaceWholeText(String var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String getWholeText() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public boolean isElementContentWhitespace() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }
}
