package weblogic.auddi.uddi.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.xml.ParserWrapper;
import weblogic.auddi.util.Util;
import weblogic.auddi.xml.SchemaException;

public class XMLToTree {
   private DefaultMutableTreeNode m_root;
   private Map entryMap;

   public XMLToTree(File var1) throws IOException, SchemaException {
      this(Util.getFileContent(var1));
   }

   public XMLToTree(String var1) throws SchemaException {
      this.entryMap = new HashMap();
      Document var2 = ParserWrapper.parseRequest(var1, false);
      this.parse(var2);
   }

   public XMLToTree(Document var1) {
      this.entryMap = new HashMap();
      this.parse(var1);
   }

   private void parse(Document var1) {
      Element var2 = var1.getDocumentElement();
      String var3 = var2.getNodeName();
      this.m_root = new DefaultMutableTreeNode(var3);
      this.parse(var2, this.m_root);
      ((NodeItem)this.m_root.getUserObject()).setReadable(false);
   }

   private void parse(Node var1, DefaultMutableTreeNode var2) {
      NodeItem var3 = new NodeItem(var1);
      String var4 = var3.getValue();
      if (var4 != null && !var4.equals("")) {
         this.entryMap.put(var4, var3);
      }

      var2.setUserObject(var3);
      var3.setTreeNode(var2);
      NodeList var5 = var1.getChildNodes();
      int var6 = var5.getLength();
      if (var6 > 0) {
         for(int var7 = 0; var7 < var6; ++var7) {
            Node var8 = var5.item(var7);
            String var9 = var1.getNodeName();
            DefaultMutableTreeNode var10 = new DefaultMutableTreeNode(var9);
            if (var8.getNodeType() != 3) {
               var2.add(var10);
            }

            this.parse(var8, var10);
         }
      }

   }

   public DefaultMutableTreeNode getRoot() {
      return this.m_root;
   }

   public String getKey() {
      NodeItem var1 = (NodeItem)this.getRoot().getUserObject();
      String var2 = var1.getKey();
      return var2;
   }

   public static JFrame getJTreeFrame(XMLToTree[] var0, String var1) {
      JFrame var2 = new JFrame(var1);
      var2.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            System.exit(0);
         }
      });
      DefaultMutableTreeNode var3 = new DefaultMutableTreeNode(var1);
      final JTree var4 = new JTree(var3);
      var4.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent var1) {
            DefaultMutableTreeNode var2 = (DefaultMutableTreeNode)var4.getLastSelectedPathComponent();
            if (var2 != null) {
               Object var3 = var2.getUserObject();
               if (var3 instanceof NodeItem) {
                  NodeItem var4x = (NodeItem)var3;
                  if (var4x != null && var4x.isReadable()) {
                     System.out.println(var4x.asKRString());
                  }
               }

            }
         }
      });

      for(int var5 = 0; var5 < var0.length; ++var5) {
         var3.add(var0[var5].getRoot());
      }

      JScrollPane var6 = new JScrollPane(var4);
      var2.getContentPane().add(var6);
      var2.setBounds(100, 50, 700, 1000);
      return var2;
   }

   public static void main(String[] var0) throws Exception {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         File var3 = new File(var0[var2]);
         System.out.println("Processing " + var0[var2] + "...");
         XMLToTree var4 = new XMLToTree(var3);
         System.out.println("Done!");
         var1.add(var4);
      }

      JFrame var5 = getJTreeFrame((XMLToTree[])((XMLToTree[])var1.toArray(new XMLToTree[0])), "XMLToTree");
      var5.setVisible(true);
   }

   public NodeItem getNodeItem(String var1) {
      return (NodeItem)this.entryMap.get(var1);
   }

   public boolean hasValue(String var1) {
      NodeItem var2 = (NodeItem)this.entryMap.get(var1);
      return var2 != null;
   }
}
