package weblogic.management.descriptors.application.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class XMLMBeanImpl extends XMLElementMBeanDelegate implements XMLMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_entityMappings = false;
   private List entityMappings;
   private boolean isSet_parserFactory = false;
   private ParserFactoryMBean parserFactory;

   public EntityMappingMBean[] getEntityMappings() {
      if (this.entityMappings == null) {
         return new EntityMappingMBean[0];
      } else {
         EntityMappingMBean[] var1 = new EntityMappingMBean[this.entityMappings.size()];
         var1 = (EntityMappingMBean[])((EntityMappingMBean[])this.entityMappings.toArray(var1));
         return var1;
      }
   }

   public void setEntityMappings(EntityMappingMBean[] var1) {
      EntityMappingMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEntityMappings();
      }

      this.isSet_entityMappings = true;
      if (this.entityMappings == null) {
         this.entityMappings = Collections.synchronizedList(new ArrayList());
      } else {
         this.entityMappings.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.entityMappings.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EntityMappings", var2, this.getEntityMappings());
      }

   }

   public void addEntityMapping(EntityMappingMBean var1) {
      this.isSet_entityMappings = true;
      if (this.entityMappings == null) {
         this.entityMappings = Collections.synchronizedList(new ArrayList());
      }

      this.entityMappings.add(var1);
   }

   public void removeEntityMapping(EntityMappingMBean var1) {
      if (this.entityMappings != null) {
         this.entityMappings.remove(var1);
      }
   }

   public ParserFactoryMBean getParserFactory() {
      return this.parserFactory;
   }

   public void setParserFactory(ParserFactoryMBean var1) {
      ParserFactoryMBean var2 = this.parserFactory;
      this.parserFactory = var1;
      this.isSet_parserFactory = var1 != null;
      this.checkChange("parserFactory", var2, this.parserFactory);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<xml");
      var2.append(">\n");
      if (null != this.getParserFactory()) {
         var2.append(this.getParserFactory().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getEntityMappings()) {
         for(int var3 = 0; var3 < this.getEntityMappings().length; ++var3) {
            var2.append(this.getEntityMappings()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</xml>\n");
      return var2.toString();
   }
}
