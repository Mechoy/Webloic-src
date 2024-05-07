package weblogic.servlet.ejb2jsp.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTML.Tag;
import weblogic.tools.ui.AWTUtils;

public class HelpFrame extends JFrame implements HyperlinkListener {
   private static final boolean debug = false;
   private boolean wasVisible;
   JFrame parent;
   JEditorPane html;
   JScrollPane scroll;
   HTMLDocument doc;
   private Map anchors = new HashMap();

   public void hyperlinkUpdate(HyperlinkEvent var1) {
      if (var1.getEventType() == EventType.ACTIVATED) {
         URL var2 = var1.getURL();
         String var3 = var1.getDescription();
         Object var4 = var1.getSource();
         p("hyperlink event: u='" + var2 + "' desc='" + var3 + "' src type=" + var4.getClass().getName());
         if (var3 != null) {
            this.scroll2anchor(var3);
         }
      }
   }

   public void scroll2anchor(String var1) {
      Integer var2 = (Integer)this.anchors.get(var1);
      if (var2 != null) {
         int var3 = var2;
         p("scroll to " + var3);
         Dimension var4;
         if (!this.isVisible() && !this.wasVisible) {
            this.wasVisible = true;
            var4 = this.getPreferredSize();
            var4.width = Math.max(var4.width, 350);
            this.setSize(var4.width + 30, 400);
            AWTUtils.centerOnWindow(this, this.parent);
         }

         this.setVisible(true);
         var4 = null;

         Rectangle var8;
         try {
            var8 = this.html.modelToView(var3);
            p("got rect=" + var8);
         } catch (Exception var7) {
            var7.printStackTrace();
            return;
         }

         JViewport var5 = this.scroll.getViewport();
         Point var6 = new Point();
         var6.x = 0;
         var6.y = var8.y;
         var5.setViewPosition(var6);
      }
   }

   static void p(String var0) {
   }

   static String readHTML(String var0) throws Exception {
      FileInputStream var1 = new FileInputStream(var0);
      byte[] var2 = new byte[var1.available()];
      var1.read(var2);
      var1.close();
      String var3 = new String(var2);
      return var3;
   }

   static String readHTML(URL var0) throws Exception {
      InputStream var1 = var0.openStream();
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte[1024];
      boolean var4 = false;

      int var6;
      while((var6 = var1.read(var3)) > 0) {
         var2.write(var3, 0, var6);
      }

      var1.close();
      var3 = var2.toByteArray();
      String var5 = new String(var3);
      return var5;
   }

   private Image getFrameIcon() {
      return AWTUtils.loadImage(this.getClass().getClassLoader(), "/weblogic/graphics/W.gif");
   }

   public HelpFrame(String var1, JFrame var2, URL var3) throws Exception {
      super(var1);
      Image var4 = this.getFrameIcon();
      if (var4 != null) {
         this.setIconImage(var4);
      }

      this.parent = var2;
      String var5 = readHTML(var3);
      this.html = new JEditorPane("text/html", var5);
      this.html.setEditable(false);
      this.html.addHyperlinkListener(this);
      this.scroll = new JScrollPane(this.html);
      this.getContentPane().add(this.scroll);
      this.doc = (HTMLDocument)this.html.getDocument();
      HTMLDocument.Iterator var6 = this.doc.getIterator(Tag.A);

      for(boolean var7 = false; var6.isValid(); var6.next()) {
         AttributeSet var8 = var6.getAttributes();
         Enumeration var9 = var8.getAttributeNames();

         while(var9.hasMoreElements()) {
            Object var10 = var9.nextElement();
            Object var11 = var8.getAttribute(var10);
            String var12 = var10.toString();
            String var13 = var11.toString();
            if (var12.equals("name") && !var13.equals("a")) {
               int var14 = var6.getStartOffset();
               p("got anchor \"" + var13 + "\" at offset " + var14);
               this.anchors.put(var13, new Integer(var14));
               break;
            }
         }
      }

      p("CTOR done, anchors=" + this.anchors);
   }
}
