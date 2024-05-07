package weblogic.xml.dom;

import org.w3c.dom.DocumentFragment;

public final class DocumentFragmentNode extends NodeImpl implements DocumentFragment {
   public DocumentFragmentNode() {
      this.setNodeType((short)11);
   }

   public String getNodeName() {
      return "#document-fragment";
   }

   public static void main(String[] var0) throws Exception {
      DocumentFragmentNode var1 = new DocumentFragmentNode();
      var1.appendChild(new TextNode("a text\n"));
      var1.appendChild(new TextNode("b text\n"));
      var1.appendChild(new TextNode("c text\n"));
      ElementNode var2 = new ElementNode();
      var2.setLocalName("parent");
      var2.appendChild(var1);
      System.out.println(var2);
      DocumentFragmentNode var3 = new DocumentFragmentNode();
      var3.appendChild(new ElementNode((String)null, "a", (String)null));
      var3.appendChild(new ElementNode((String)null, "b", (String)null));
      var3.appendChild(new ElementNode((String)null, "c", (String)null));
      var2.appendChild(var3);
      System.out.println(var2);
   }
}
