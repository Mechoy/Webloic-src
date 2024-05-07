package weblogic.wsee.policy.runtime.schema;

import com.bea.xml.XmlException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import weblogic.wsee.util.Verbose;

public class BuiltinPolicyListFactory {
   public static final String POLICY_LIST_FILENAME = "CannedPolicyList.xml";
   public static final String POLICY_LIST_FILEPATH = "/weblogic/wsee/policy/runtime/CannedPolicyList.xml";
   private static boolean VERBOSE = Verbose.isVerbose(BuiltinPolicyListFactory.class);
   private static BuiltinPolicyList bpList = null;
   private static boolean isErrorState = false;

   private BuiltinPolicyListFactory() {
   }

   private static URL findCannedPolicyListUrl() {
      URL var0 = BuiltinPolicyListFactory.class.getResource("CannedPolicyList.xml");
      if (var0 == null) {
         Verbose.log((Object)"Error on URL of CannedPolicyList.xml");
         throw new IllegalStateException("Cannot find canned policy list file for CannedPolicyList.xml");
      } else {
         if (VERBOSE) {
            Verbose.log((Object)("Loading canned policy list from: " + var0));
         }

         return var0;
      }
   }

   public static BuiltinPolicyList getBuiltinPolicyList() {
      if (bpList == null) {
         if (isErrorState) {
            throw new IllegalStateException("Privious error on getting policy from url =" + findCannedPolicyListUrl());
         }

         newInstance();
      }

      return bpList;
   }

   private static void newInstance() {
      try {
         InputStream var0 = BuiltinPolicyListFactory.class.getResourceAsStream("/weblogic/wsee/policy/runtime/CannedPolicyList.xml");
         if (null == var0) {
            isErrorState = true;
            throw new IllegalArgumentException("Null inputStream on resource: /weblogic/wsee/policy/runtime/CannedPolicyList.xml");
         } else {
            WlsBuiltinPoliciesDocument.WlsBuiltinPolicies var1 = WlsBuiltinPoliciesDocument.Factory.parse(var0).getWlsBuiltinPolicies();
            if (var1.sizeOfBuiltinPolicyArray() == 0) {
               throw new IndexOutOfBoundsException("Invalid Canned Policy List");
            } else {
               BuiltinPolicyType[] var2 = var1.getBuiltinPolicyArray();
               if (null == var2) {
                  throw new IndexOutOfBoundsException("Invalid Canned Policy List = null");
               } else {
                  bpList = new BuiltinPolicyList(var2);
                  isErrorState = false;
               }
            }
         }
      } catch (IOException var3) {
         Verbose.log("IO Exception on getting BuiltinPolicyList on url = " + findCannedPolicyListUrl(), var3);
         isErrorState = true;
         throw new IllegalArgumentException("Bad url on IO" + findCannedPolicyListUrl());
      } catch (XmlException var4) {
         Verbose.log((Object)("XML Exception on reading BuiltinPolicyList on url = " + findCannedPolicyListUrl()));
         isErrorState = true;
         Verbose.log((Object)("Error message " + var4.getMessage()));
         isErrorState = true;
         throw new IllegalArgumentException("Bad url on XML exception" + findCannedPolicyListUrl());
      }
   }
}
