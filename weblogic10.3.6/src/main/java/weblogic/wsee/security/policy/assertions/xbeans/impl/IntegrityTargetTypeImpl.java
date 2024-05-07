package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.AlgorithmType;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityTargetType;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.wsee.security.policy.assertions.xbeans.TransformType;

public class IntegrityTargetTypeImpl extends XmlComplexContentImpl implements IntegrityTargetType {
   private static final long serialVersionUID = 1L;
   private static final QName DIGESTALGORITHM$0 = new QName("http://www.bea.com/wls90/security/policy", "DigestAlgorithm");
   private static final QName TRANSFORM$2 = new QName("http://www.bea.com/wls90/security/policy", "Transform");
   private static final QName MESSAGEPARTS$4 = new QName("http://www.bea.com/wls90/security/policy", "MessageParts");

   public IntegrityTargetTypeImpl(SchemaType var1) {
      super(var1);
   }

   public AlgorithmType getDigestAlgorithm() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         AlgorithmType var2 = null;
         var2 = (AlgorithmType)this.get_store().find_element_user(DIGESTALGORITHM$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setDigestAlgorithm(AlgorithmType var1) {
      this.generatedSetterHelperImpl(var1, DIGESTALGORITHM$0, 0, (short)1);
   }

   public AlgorithmType addNewDigestAlgorithm() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         AlgorithmType var2 = null;
         var2 = (AlgorithmType)this.get_store().add_element_user(DIGESTALGORITHM$0);
         return var2;
      }
   }

   public TransformType[] getTransformArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(TRANSFORM$2, var2);
         TransformType[] var3 = new TransformType[var2.size()];
         var2.toArray(var3);
         return var3;
      }
   }

   public TransformType getTransformArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         TransformType var3 = null;
         var3 = (TransformType)this.get_store().find_element_user(TRANSFORM$2, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return var3;
         }
      }
   }

   public int sizeOfTransformArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(TRANSFORM$2);
      }
   }

   public void setTransformArray(TransformType[] var1) {
      this.check_orphaned();
      this.arraySetterHelper(var1, TRANSFORM$2);
   }

   public void setTransformArray(int var1, TransformType var2) {
      this.generatedSetterHelperImpl(var2, TRANSFORM$2, var1, (short)2);
   }

   public TransformType insertNewTransform(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         TransformType var3 = null;
         var3 = (TransformType)this.get_store().insert_element_user(TRANSFORM$2, var1);
         return var3;
      }
   }

   public TransformType addNewTransform() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         TransformType var2 = null;
         var2 = (TransformType)this.get_store().add_element_user(TRANSFORM$2);
         return var2;
      }
   }

   public void removeTransform(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(TRANSFORM$2, var1);
      }
   }

   public MessagePartsType getMessageParts() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         MessagePartsType var2 = null;
         var2 = (MessagePartsType)this.get_store().find_element_user(MESSAGEPARTS$4, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setMessageParts(MessagePartsType var1) {
      this.generatedSetterHelperImpl(var1, MESSAGEPARTS$4, 0, (short)1);
   }

   public MessagePartsType addNewMessageParts() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         MessagePartsType var2 = null;
         var2 = (MessagePartsType)this.get_store().add_element_user(MESSAGEPARTS$4);
         return var2;
      }
   }
}
