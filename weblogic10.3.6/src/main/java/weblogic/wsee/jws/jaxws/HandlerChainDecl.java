package weblogic.wsee.jws.jaxws;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.xml.XmlException;
import com.sun.java.xml.ns.javaee.HandlerChainType;
import com.sun.java.xml.ns.javaee.HandlerChainsDocument;
import com.sun.java.xml.ns.javaee.PortComponentHandlerType;
import com.sun.java.xml.ns.javaee.HandlerChainsDocument.Factory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import org.w3c.dom.Node;
import weblogic.j2ee.dd.xml.J2eeAnnotationProcessor;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.HandlerChainBean;
import weblogic.j2ee.descriptor.HandlerChainsBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.PortComponentHandlerBean;
import weblogic.wsee.jws.HandlerException;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.util.StringUtil;

public class HandlerChainDecl extends weblogic.wsee.jws.HandlerChainDecl<HandlerChainType> {
   public HandlerChainDecl(JClass var1, JClass var2) {
      super(var1, var2);
   }

   public HandlerChainDecl(JClass var1, JClass var2, ClassLoader var3) {
      super(var1, var2, var3);
   }

   protected HandlerChainType[] processSOAPMessageHandlers(JClass var1, JAnnotation var2) {
      this.addLogEvent(EventLevel.ERROR, new JwsLogEvent(var1, "type.handlerChain.jaxws.soapMessageHandlersNotSupported", new Object[]{var1.getQualifiedName()}));
      return null;
   }

   protected HandlerChainType[] processHandlerChain(JClass var1, URL var2, String var3) {
      if (!StringUtil.isEmpty(var3)) {
         this.addLogEvent(EventLevel.WARNING, new JwsLogEvent(var1, "type.handlerChain.jaxws.nameSpecified", new Object[]{var1.getQualifiedName()}));
      }

      try {
         HandlerChainsDocument var4 = Factory.parse(var2);
         var4.validate();
         return var4.getHandlerChains().getHandlerChainArray();
      } catch (XmlException var5) {
         this.addLogEvent(EventLevel.ERROR, new JwsLogEvent(var1, "type.handlerChain.chainFileNotValid", new Object[]{var2, var5.getMessage()}));
      } catch (IOException var6) {
         this.addLogEvent(EventLevel.ERROR, new JwsLogEvent(var1, "type.handlerChain.chainFileParseError", new Object[]{var2, var6.getMessage()}));
      }

      return null;
   }

   public void populatePort(PortComponentBean var1, EnvEntryBean[] var2) {
      if (this.getHandlerChains() != null) {
         if (var1.getHandlerChains() == null) {
            HandlerChainsBean var3 = var1.createHandlerChains();
            HandlerChainType[] var4 = (HandlerChainType[])this.getHandlerChains();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               HandlerChainType var7 = var4[var6];
               HandlerChainBean var8 = var3.createHandlerChain();
               Node var9;
               if (var7.xgetPortNamePattern() == null) {
                  var8.setPortNamePattern(var7.getPortNamePattern());
               } else {
                  var9 = var7.xgetPortNamePattern().getDomNode();
                  var8.setPortNamePattern(this.getQualifiedNamePattern(var9));
               }

               if (var7.getProtocolBindings() != null) {
                  var8.setProtocolBindings(var7.getProtocolBindings().toString());
               }

               if (var7.xgetServiceNamePattern() == null) {
                  var8.setServiceNamePattern(var7.getServiceNamePattern());
               } else {
                  var9 = var7.xgetServiceNamePattern().getDomNode();
                  var8.setServiceNamePattern(this.getQualifiedNamePattern(var9));
               }

               PortComponentHandlerType[] var19 = var7.getHandlerArray();
               int var10 = var19.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  PortComponentHandlerType var12 = var19[var11];
                  PortComponentHandlerBean var13 = var8.createHandler();
                  if (var12.getHandlerName() != null) {
                     var13.setHandlerName(var12.getHandlerName().getStringValue());
                  }

                  var13.setHandlerClass(var12.getHandlerClass().getStringValue());

                  for(int var14 = 0; var14 < var12.sizeOfSoapRoleArray(); ++var14) {
                     var13.addSoapRole(var12.getSoapRoleArray(var14).getStringValue());
                  }

                  if (var2 != null) {
                     EnvEntryBean[] var20 = var2;
                     int var15 = var2.length;

                     for(int var16 = 0; var16 < var15; ++var16) {
                        EnvEntryBean var17 = var20[var16];
                        EnvEntryBean var18 = var13.createEnvEntry();
                        var18.setEnvEntryName(var17.getEnvEntryName());
                        var18.setEnvEntryType(var17.getEnvEntryType());
                        var18.setEnvEntryValue(var17.getEnvEntryValue());
                     }
                  }

                  this.processAnnotations(var12, var13);
               }
            }

         }
      }
   }

   private String getQualifiedNamePattern(Node var1) {
      if (var1 != null && var1.getFirstChild() != null) {
         String var2 = var1.getFirstChild().getNodeValue();
         String var3 = var2;
         if (var2 != null) {
            int var4 = var2.indexOf(":");
            if (var4 > 0) {
               String var5 = "xmlns:" + var2.substring(0, var4);
               String var6 = null;

               for(Node var7 = var1; var7 != null; var7 = var7.getParentNode()) {
                  if (var7.getAttributes() != null && var7.getAttributes().getNamedItem(var5) != null) {
                     var6 = var7.getAttributes().getNamedItem(var5).getNodeValue();
                     if (var6 != null) {
                        break;
                     }
                  }
               }

               if (var6 != null) {
                  var3 = "{" + var6 + "}" + var2.substring(var4 + 1);
               }
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   private void processAnnotations(PortComponentHandlerType var1, PortComponentHandlerBean var2) {
      try {
         Class var3 = Class.forName(var1.getHandlerClass().getStringValue(), true, Thread.currentThread().getContextClassLoader());
         J2eeAnnotationProcessor var4 = new J2eeAnnotationProcessor();
         var4.processJ2eeAnnotations(var3, var2);
      } catch (ClassNotFoundException var5) {
         throw new HandlerException(var5);
      }
   }

   public String[] getHandlerClassNames() {
      ArrayList var1 = new ArrayList();
      HandlerChainType[] var2 = (HandlerChainType[])this.getHandlerChains();
      if (var2 != null) {
         HandlerChainType[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            HandlerChainType var6 = var3[var5];
            PortComponentHandlerType[] var7 = var6.getHandlerArray();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               PortComponentHandlerType var10 = var7[var9];
               var1.add(var10.getHandlerClass().getStringValue());
            }
         }
      }

      return (String[])var1.toArray(new String[var1.size()]);
   }
}
