package weblogic.servlet.jsp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletContext;
import weblogic.servlet.internal.WebAppServletContext;

public final class JspExceptionWriter extends PrintWriter {
   private String packagePrefix;
   private ServletContext sc;
   private boolean transformed = false;

   public JspExceptionWriter(String var1, Writer var2, ServletContext var3) {
      super(var2, true);
      this.packagePrefix = var1;
      this.sc = var3;
   }

   public void println(String var1) {
      if (var1.startsWith("\tat " + this.packagePrefix + ".")) {
         try {
            var1 = var1.substring(4);
            StringTokenizer var2 = new StringTokenizer(var1, "()");
            String var3 = var2.nextToken();
            String var4 = var3.substring(0, var3.lastIndexOf("."));
            String var5 = var3.substring(var3.lastIndexOf(".") + 1);
            String var6 = var2.nextToken();
            if (var6.equals("Native Method")) {
               super.println("\tat " + var1 + " [native method]");
            } else {
               var2 = new StringTokenizer(var6, ":");
               String var7 = var2.nextToken();
               int var8 = Integer.parseInt(var2.nextToken());
               String var9 = var4.replace('.', '/') + ".java";
               ClassLoader var10 = ((WebAppServletContext)this.sc).getServletClassLoader();
               InputStream var11 = var10.getResourceAsStream(var9);
               if (var11 == null) {
                  super.println("\tat " + var1 + " [no source]");
               } else {
                  Vector var12 = new Vector();
                  BufferedReader var13 = new BufferedReader(new InputStreamReader(var11));
                  int var14 = 0;

                  while((var1 = var13.readLine()) != null) {
                     ++var14;
                     if (var14 == var8) {
                        int var15 = var1.lastIndexOf("//[ ");
                        if (var15 != -1) {
                           var1 = var1.substring(var15 + 4);
                           var2 = new StringTokenizer(var1);

                           try {
                              var12.addElement(var2.nextToken(";"));
                              var2.nextToken(":");

                              try {
                                 var12.addElement(var2.nextToken(": ]"));
                                 var13.close();
                                 var11 = this.sc.getResourceAsStream((String)var12.elementAt(0));
                                 if (var11 != null) {
                                    var13 = new BufferedReader(new InputStreamReader(var11));
                                    var14 = 1;
                                    int var16 = Integer.parseInt((String)var12.elementAt(1));

                                    while((var1 = var13.readLine()) != null) {
                                       if (var14++ == var16) {
                                          var12.addElement(var1);
                                          break;
                                       }
                                    }

                                    var13.close();
                                 }
                              } catch (NoSuchElementException var18) {
                                 var12 = null;
                              }
                           } catch (NoSuchElementException var19) {
                              var12 = null;
                           }
                           break;
                        }
                     }
                  }

                  if (var12 == null) {
                     super.println("\tat " + var1 + " [could not parse]");
                  } else {
                     String var23 = (String)var12.elementAt(0);
                     super.println("\tat " + var4 + (var5.equals("_jspService") ? "" : "." + var5) + "(" + var23 + ":" + var12.elementAt(1) + ")");
                     this.transformed = true;
                  }
               }
            }
         } catch (NoSuchElementException var20) {
            super.println("\tat " + var1 + " [could not parse]");
         } catch (NumberFormatException var21) {
            super.println("\tat " + var1 + " [no line number]");
         } catch (IOException var22) {
            super.println("\tat " + var1);
         }
      } else {
         if (!this.transformed) {
            super.println(var1);
         }

      }
   }

   public void println(Object var1) {
      this.println(var1.toString());
   }

   public void println(char[] var1) {
      this.println(new String(var1));
   }
}
