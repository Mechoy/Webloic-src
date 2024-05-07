package weblogic.wsee.jws.jaxrpc;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.xml.XmlException;
import com.sun.java.xml.ns.j2Ee.ParamValueType;
import com.sun.java.xml.ns.j2Ee.PortComponentHandlerType;
import com.sun.java.xml.ns.j2Ee.XsdQNameType;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.PortComponentHandlerBean;
import weblogic.wsee.jws.HandlerException;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.wseegen.schemas.HandlerChainType;
import weblogic.wsee.tools.wseegen.schemas.HandlerConfig;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.UniqueNameSet;

public class HandlerChainDecl extends weblogic.wsee.jws.HandlerChainDecl<HandlerChainType> {
   public HandlerChainDecl(JClass var1, JClass var2) {
      super(var1, var2);
   }

   protected HandlerChainType[] processSOAPMessageHandlers(JClass var1, JAnnotation var2) {
      JAnnotationValue var3 = var2.getValue("value");
      if (var3 == null) {
         return null;
      } else {
         JAnnotation[] var4 = var3.asAnnotationArray();
         HandlerChainType var5 = HandlerChainType.Factory.newInstance();
         UniqueNameSet var6 = new UniqueNameSet();
         if (var4 != null) {
            JAnnotation[] var7 = var4;
            int var8 = var4.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               JAnnotation var10 = var7[var9];
               PortComponentHandlerType var11 = var5.addNewHandler();
               String var12 = "";
               JAnnotationValue var13 = var10.getValue("className");
               if (var13 != null) {
                  var12 = var13.asString();
               }

               var11.addNewHandlerClass().setStringValue(var12);
               JAnnotationValue var14 = var10.getValue("name");
               String var15;
               if (!StringUtil.isEmpty(var14.asString())) {
                  var15 = var6.add(var14.asString());
               } else {
                  var15 = var6.add(var12);
               }

               var11.addNewHandlerName().setStringValue(var15);
               JAnnotationValue var16 = var10.getValue("initParams");
               int var20;
               if (var16 != null) {
                  JAnnotation[] var17 = var16.asAnnotationArray();
                  if (var17 != null) {
                     JAnnotation[] var18 = var17;
                     int var19 = var17.length;

                     for(var20 = 0; var20 < var19; ++var20) {
                        JAnnotation var21 = var18[var20];
                        ParamValueType var22 = var11.addNewInitParam();
                        var22.addNewParamName().setStringValue(var21.getValue("name").asString());
                        var22.addNewParamValue().setStringValue(var21.getValue("value").asString());
                     }
                  }
               }

               JAnnotationValue var28 = var10.getValue("roles");
               int var25;
               String[] var31;
               if (var28 != null) {
                  String[] var29 = var28.asStringArray();
                  var31 = var29;
                  var20 = var29.length;

                  for(var25 = 0; var25 < var20; ++var25) {
                     String var26 = var31[var25];
                     var11.addNewSoapRole().setStringValue(var26);
                  }
               }

               JAnnotationValue var30 = var10.getValue("headers");
               if (var30 != null) {
                  var31 = var30.asStringArray();
                  String[] var24 = var31;
                  var25 = var31.length;

                  for(int var27 = 0; var27 < var25; ++var27) {
                     String var23 = var24[var27];
                     var11.addNewSoapHeader().setQNameValue(QName.valueOf(var23));
                  }
               }
            }
         }

         return new HandlerChainType[]{var5};
      }
   }

   protected HandlerChainType[] processHandlerChain(JClass var1, URL var2, String var3) {
      try {
         HandlerConfig var4 = HandlerConfig.Factory.parse(var2);
         var4.validate();
         HandlerChainType[] var5 = var4.getHandlerConfig().getHandlerChainArray();
         HandlerChainType[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            HandlerChainType var9 = var6[var8];
            if (var9.getHandlerChainName() != null && var9.getHandlerChainName().getStringValue().equals(var3.trim())) {
               return new HandlerChainType[]{var9};
            }
         }

         this.addLogEvent(EventLevel.ERROR, new JwsLogEvent(var1, "type.handlerChain.chainNotFound", new Object[]{var3, var2}));
      } catch (XmlException var10) {
         this.addLogEvent(EventLevel.ERROR, new JwsLogEvent(var1, "type.handlerChain.chainFileNotValid", new Object[]{var2, var10.getMessage()}));
      } catch (IOException var11) {
         this.addLogEvent(EventLevel.ERROR, new JwsLogEvent(var1, "type.handlerChain.chainFileParseError", new Object[]{var2, var11.getMessage()}));
      }

      return null;
   }

   public void populatePort(PortComponentBean var1, EnvEntryBean[] var2) {
      if (this.getHandlerChains() != null) {
         HandlerChainType[] var3 = (HandlerChainType[])this.getHandlerChains();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            HandlerChainType var6 = var3[var5];
            PortComponentHandlerType[] var7 = var6.getHandlerArray();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               PortComponentHandlerType var10 = var7[var9];
               if (var10.getHandlerName() == null) {
                  throw new HandlerException("Could not locate handler in Handler Chain:  " + var6.getHandlerChainName().getStringValue() + ". Handler Config file might be missing j2ee namespace. ");
               }

               PortComponentHandlerBean var11 = var1.createHandler();
               var11.setHandlerName(var10.getHandlerName().getStringValue());
               var11.setHandlerClass(var10.getHandlerClass().getStringValue());
               ParamValueType[] var12 = var10.getInitParamArray();
               int var13 = var12.length;

               int var14;
               for(var14 = 0; var14 < var13; ++var14) {
                  ParamValueType var15 = var12[var14];
                  ParamValueBean var16 = var11.createInitParam();
                  var16.setParamName(var15.getParamName().getStringValue());
                  var16.setParamValue(var15.getParamValue().getStringValue());
               }

               XsdQNameType[] var17 = var10.getSoapHeaderArray();
               var13 = var17.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  XsdQNameType var20 = var17[var14];
                  var11.addSoapHeader(var20.getQNameValue());
               }

               com.sun.java.xml.ns.j2Ee.String[] var18 = var10.getSoapRoleArray();
               var13 = var18.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  com.sun.java.xml.ns.j2Ee.String var21 = var18[var14];
                  var11.addSoapRole(var21.getStringValue());
               }

               if (var2 != null) {
                  EnvEntryBean[] var19 = var2;
                  var13 = var2.length;

                  for(var14 = 0; var14 < var13; ++var14) {
                     EnvEntryBean var22 = var19[var14];
                     EnvEntryBean var23 = var11.createEnvEntry();
                     var23.setEnvEntryName(var22.getEnvEntryName());
                     var23.setEnvEntryType(var22.getEnvEntryType());
                     var23.setEnvEntryValue(var22.getEnvEntryValue());
                  }
               }
            }
         }

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

               try {
                  var1.add(var10.getHandlerClass().getStringValue());
               } catch (NullPointerException var12) {
                  throw new HandlerException("Could not parse a handler for chain [" + var6.getHandlerChainName() + "], Pls. check the validity of the chain configuration file");
               }
            }
         }
      }

      return (String[])var1.toArray(new String[var1.size()]);
   }
}
