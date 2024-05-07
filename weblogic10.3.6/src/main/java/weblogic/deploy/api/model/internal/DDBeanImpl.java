package weblogic.deploy.api.model.internal;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.XpathListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.model.WebLogicDDBean;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.xml.xpath.DOMXPath;
import weblogic.xml.xpath.XPathException;

public class DDBeanImpl implements WebLogicDDBean {
   private static final boolean debug = Debug.isDebug("model");
   protected Node node;
   protected String xpath = null;
   protected WebLogicDeployableObject dObject = null;
   protected String id = null;
   protected List childBeans = new ArrayList();
   private DDBeanRootImpl ddRoot = null;
   NamedNodeMap attrs = null;
   String[] names = null;
   private DDBean parent;
   private String text = null;

   public DDBeanImpl(WebLogicDeployableObject var1) {
      this.dObject = var1;
   }

   protected void initDD(Node var1, String var2, DDBean var3) {
      this.node = var1;
      this.xpath = var2;
      if (debug) {
         Debug.say("Creating DDBean with xpath: " + var2);
      }

      this.parent = var3;
      if (var1 != null) {
         this.initAttributes();
      }
   }

   private void initAttributes() {
      this.attrs = this.node.getAttributes();
      if (this.attrs != null) {
         for(int var2 = 0; var2 < this.attrs.getLength(); ++var2) {
            Node var1 = this.attrs.item(var2);
            if (var1.getNodeName().equals("id")) {
               this.id = var1.getNodeValue();
               break;
            }
         }
      }

   }

   public String getXpath() {
      return this.xpath;
   }

   public String getText() {
      return this.getNodeText();
   }

   public String getId() {
      return this.id;
   }

   public DDBeanRoot getRoot() {
      return this.ddRoot;
   }

   public DDBean[] getChildBean(String var1) {
      ConfigHelper.checkParam("xpath", var1);
      if (debug) {
         Debug.say("[" + this.getXpath() + "] getting nodes with xpath: " + var1);
      }

      DDBean[] var2 = this.findBean(var1);
      if (var2.length > 0) {
         return var2;
      } else {
         try {
            DOMXPath var3 = new DOMXPath(var1);
            Node var4 = this.getNode();
            if (var4 == null) {
               return null;
            } else {
               Set var5 = var3.evaluateAsNodeset(var4);
               if (var5 != null && var5.size() != 0) {
                  Iterator var6 = var5.iterator();
                  ArrayList var7 = new ArrayList();

                  while(var6.hasNext()) {
                     Node var9 = (Node)var6.next();
                     DDBeanImpl var8 = new DDBeanImpl(this.dObject);
                     var8.initDD(var9, var1, this);
                     this.addChild(var8);
                     var7.add(var8);
                  }

                  return (DDBean[])((DDBean[])var7.toArray(new DDBean[0]));
               } else {
                  if (debug) {
                     Debug.say("No nodes for xpath: " + var1);
                  }

                  return null;
               }
            }
         } catch (XPathException var10) {
            if (debug) {
               var10.printStackTrace();
            }

            return null;
         }
      }
   }

   public String[] getText(String var1) {
      ConfigHelper.checkParam("path", var1);
      DDBean[] var2 = this.getChildBean(var1);
      if (var2 == null) {
         return null;
      } else {
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = var2[var4].getText();
         }

         return var3;
      }
   }

   public void addXpathListener(String var1, XpathListener var2) {
   }

   public void removeXpathListener(String var1, XpathListener var2) {
   }

   public String[] getAttributeNames() {
      if (this.names != null) {
         return this.names;
      } else if (this.attrs == null) {
         return null;
      } else {
         this.names = new String[this.attrs.getLength()];

         for(int var1 = 0; var1 < this.attrs.getLength(); ++var1) {
            this.names[var1] = this.attrs.item(var1).getNodeName();
         }

         return this.names;
      }
   }

   public String getAttributeValue(String var1) {
      ConfigHelper.checkParam("attrName", var1);
      if (this.attrs == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < this.attrs.getLength(); ++var3) {
            Node var2 = this.attrs.item(var3);
            if (var2.getNodeName().equals(var1)) {
               return var2.getNodeValue();
            }
         }

         return null;
      }
   }

   public Node getNode() {
      if (this.node == null) {
      }

      return this.node;
   }

   public void addChild(DDBeanImpl var1) {
      this.childBeans.add(var1);
      var1.setRoot(this.getRoot());
   }

   protected void setRoot(DDBeanRoot var1) {
      this.ddRoot = (DDBeanRootImpl)var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getXpath());
      if (this.getText() != null && this.getText().length() > 0) {
         var1.append(": <");
         var1.append(this.getText());
         var1.append(">");
      }

      if (this.childBeans.size() > 0) {
         Iterator var2 = this.childBeans.iterator();

         while(var2.hasNext()) {
            var1.append("\n");
            var1.append(var2.next().toString());
         }
      }

      return var1.toString();
   }

   public int hashCode() {
      return this.getXpath().hashCode();
   }

   public boolean equals(Object var1) {
      new Exception("[DDBeanImpl].equals() expensive equals method");
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         return var1 instanceof DDBeanImpl && this.getNode() != null && this.getNode() == ((DDBeanImpl)var1).getNode();
      }
   }

   DDBean[] findBean(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var4 = this.childBeans.iterator();

      while(var4.hasNext()) {
         DDBeanImpl var3 = (DDBeanImpl)var4.next();
         if (var1 == var3.getXpath()) {
            var2.add(var3);
         } else if (var1 != null && var1.equals(var3.getXpath())) {
            var2.add(var3);
         }
      }

      return (DDBean[])((DDBean[])var2.toArray(new DDBean[0]));
   }

   private String getNodeText() {
      if (this.text == null && this.getNode() != null) {
         this.text = getTextFromNode(this.node);
      }

      return this.text;
   }

   private static final String getTextFromNode(Node var0) {
      try {
         TransformerFactory var1 = TransformerFactory.newInstance();
         Transformer var2 = var1.newTransformer();
         var2.setOutputProperty("omit-xml-declaration", "yes");
         StringWriter var3 = new StringWriter();
         NodeList var4 = var0.getChildNodes();
         int var5 = 0;

         for(int var6 = var4.getLength(); var5 < var6; ++var5) {
            Node var7 = var4.item(var5);
            var2.transform(new DOMSource(var7), new StreamResult(var3));
         }

         String var10 = var3.toString();
         return var10;
      } catch (TransformerConfigurationException var8) {
         return null;
      } catch (TransformerException var9) {
         return null;
      }
   }
}
