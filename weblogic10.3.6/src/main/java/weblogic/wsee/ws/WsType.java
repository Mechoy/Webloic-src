package weblogic.wsee.ws;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xml.XmlObject;
import javax.xml.namespace.QName;
import weblogic.wsee.util.HolderUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.XBeanUtil;

public class WsType {
   private String name;
   private int type;
   private XmlTypeName xmlName;
   private QName elementName;
   private Class javaType;
   private boolean isXmlObjectForTypeOrDocument = false;
   private boolean isXmlObjectForDocument = false;
   private boolean isArrayOfXmlObjectForType = false;
   private boolean isArrayOfXmlObjectForDocument = false;
   private Class xmlObjectArrayComponentClass = null;
   protected boolean isHeader = false;
   private boolean isOptionalElement = false;
   public static final int IN = 0;
   public static final int OUT = 1;
   public static final int IN_OUT = 2;
   public static final int RETURN = 3;

   WsType(String var1, int var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("name can not be null");
      } else {
         this.name = var1;
         this.setMode(var2);
      }
   }

   public void checkAndSetXmlObject(Class var1) {
      if (var1.isArray()) {
         Class var2;
         for(var2 = var1.getComponentType(); var2.isArray(); var2 = var2.getComponentType()) {
         }

         if (XmlObject.class.isAssignableFrom(var2)) {
            this.xmlObjectArrayComponentClass = var2;
         } else {
            try {
               ClassLoader var3 = var2.getClassLoader();
               if (var3 != null) {
                  Class var4 = var3.loadClass("org.apache.xmlbeans.XmlObject");
                  if (var4 != null && var4.isAssignableFrom(var2)) {
                     this.xmlObjectArrayComponentClass = var2;
                  }
               }
            } catch (ClassNotFoundException var6) {
            }
         }

         if (this.xmlObjectArrayComponentClass != null) {
            if (XBeanUtil.xmlBeanIsDocumentType(this.xmlObjectArrayComponentClass)) {
               this.isArrayOfXmlObjectForDocument = true;
            } else {
               this.isArrayOfXmlObjectForType = true;
            }
         }
      } else {
         if (XmlObject.class.isAssignableFrom(var1)) {
            this.isXmlObjectForDocument = XBeanUtil.xmlBeanIsDocumentType(var1);
            this.isXmlObjectForTypeOrDocument = true;
            return;
         }

         try {
            ClassLoader var7 = var1.getClassLoader();
            if (var7 != null) {
               Class var8 = var7.loadClass("org.apache.xmlbeans.XmlObject");
               if (var8 != null && var8.isAssignableFrom(var1)) {
                  this.isXmlObjectForDocument = XBeanUtil.xmlBeanIsDocumentType(var1);
                  this.isXmlObjectForTypeOrDocument = true;
                  return;
               }
            }
         } catch (ClassNotFoundException var5) {
         }
      }

   }

   public boolean isXmlObjectForTypeOrDocument() {
      return this.isXmlObjectForTypeOrDocument;
   }

   public boolean isXmlObjectForDocument() {
      return this.isXmlObjectForDocument;
   }

   public boolean isArrayOfXmlObjectForType() {
      return this.isArrayOfXmlObjectForType;
   }

   public boolean isArrayOfXmlObjectForDocument() {
      return this.isArrayOfXmlObjectForDocument;
   }

   public boolean isAnyXmlObject() {
      return this.isXmlObjectForTypeOrDocument || this.isXmlObjectForDocument || this.isArrayOfXmlObjectForType || this.isArrayOfXmlObjectForDocument;
   }

   public Class getXmlObjectArrayComponentClass() {
      return this.xmlObjectArrayComponentClass;
   }

   public int getMode() {
      return this.type;
   }

   public String getModeAsString() {
      switch (this.type) {
         case 0:
            return "IN";
         case 1:
            return "OUT";
         case 2:
            return "INOUT";
         case 3:
            return "RETURN";
         default:
            return null;
      }
   }

   void setMode(int var1) {
      this.type = var1;
      switch (var1) {
         case 0:
            return;
         case 1:
            return;
         case 2:
            return;
         case 3:
            return;
         default:
            throw new IllegalArgumentException("Invalide type value " + var1);
      }
   }

   public String getName() {
      return this.name;
   }

   public XmlTypeName getXmlName() {
      return this.xmlName;
   }

   void setXmlName(XmlTypeName var1) {
      assert var1 != null : "xmlName can not be null";

      this.xmlName = var1;
   }

   public QName getElementName() {
      return this.elementName;
   }

   void setElementName(QName var1) {
      this.elementName = var1;
   }

   public Class getJavaType() {
      return this.javaType;
   }

   public void setJavaType(Class var1) {
      assert var1 != null : "javaType can not be null";

      this.javaType = HolderUtil.getRealType(var1);
      this.checkAndSetXmlObject(var1);
   }

   public boolean isHeader() {
      return this.isHeader;
   }

   public boolean isOptionalElement() {
      return this.isOptionalElement;
   }

   public void setOptionalElement(boolean var1) {
      this.isOptionalElement = var1;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("type", this.type);
      var1.writeField("xmlName", this.xmlName);
      var1.writeField("javaType", this.javaType);
      var1.writeField("name", this.name);
      var1.writeField("isHeader", this.isHeader);
      var1.writeField("isOptionalElement", this.isOptionalElement);
      var1.end();
   }
}
