package com.upokecenter.test;
/*
Written in 2013 by Peter O.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/
If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
 */
import org.junit.Assert;

  final class TestCommon {
private TestCommon() {
}
    public static String ToByteArrayString(byte[] bytes) {
      if (bytes == null) {
 return "null";
}
      StringBuilder sb = new StringBuilder();
      String hex = "0123456789ABCDEF";
      sb.append("new byte[] { ");
      for (int i = 0; i < bytes.length; ++i) {
        if (i > 0) {
          sb.append(", ");  }
        if ((bytes[i] & 0x80) != 0) {
          sb.append("(byte)0x");
        } else {
          sb.append("0x");
        }
        sb.append(hex.charAt((bytes[i] >> 4) & 0xf));
        sb.append(hex.charAt(bytes[i] & 0xf));
      }
      sb.append("}");
      return sb.toString();
    }

    private static void ReverseChars(char[] chars, int offset, int length) {
      int half = length >> 1;
      int right = offset + length - 1;
      for (int i = 0; i < half; i++, right--) {
        char value = chars[offset + i];
        chars[offset + i] = chars[right];
        chars[right] = value;
      }
    }

    private static String Digits = "0123456789";

    public static String LongToString(long longValue) {
      if (longValue == Long.MIN_VALUE) {
 return "-9223372036854775808";
}
      if (longValue == 0L) {
 return "0";
}
      boolean neg = longValue < 0;
      char[] chars = new char[24];
      int count = 0;
      if (neg) {
        chars[0] = '-';
        ++count;
        longValue = -longValue;
      }
      while (longValue != 0) {
        char digit = Digits.charAt((int)(longValue % 10));
        chars[count++] = digit;
        longValue /= 10;
      }
      if (neg) {
        ReverseChars(chars, 1, count - 1);
      } else {
        ReverseChars(chars, 0, count);
      }
      return new String(chars, 0, count);
    }

    public static String IntToString(int value) {
      if (value == Integer.MIN_VALUE) {
 return "-2147483648";
}
      if (value == 0) {
 return "0";
}
      boolean neg = value < 0;
      char[] chars = new char[24];
      int count = 0;
      if (neg) {
        chars[0] = '-';
        ++count;
        value = -value;
      }
      while (value != 0) {
        char digit = Digits.charAt((int)(value % 10));
        chars[count++] = digit;
        value /= 10;
      }
      if (neg) {
        ReverseChars(chars, 1, count - 1);
      } else {
        ReverseChars(chars, 0, count);
      }
      return new String(chars, 0, count);
    }

    private static boolean ByteArraysEqual(byte[] arr1, byte[] arr2) {
      if (arr1 == null) {
 return arr2 == null;
}
      if (arr2 == null) {
 return false;
}
      if (arr1.length != arr2.length) {
        return false;
      }
      for (int i = 0; i < arr1.length; ++i) {
        if (arr1[i] != arr2[i]) {
 return false;
}
      }
      return true;
    }

    public static void AssertByteArraysEqual(byte[] arr1, byte[] arr2) {
      if (!ByteArraysEqual(arr1, arr2)) {
     Assert.fail("Expected " + ToByteArrayString(arr1) + ", got " +
       ToByteArrayString(arr2));
      }
    }

    public static void AssertEqualsHashCode(Object o, Object o2) {
      if (o.equals(o2)) {
        if (!o2.equals(o)) {
          Assert.fail(
String.format(java.util.Locale.US,"%s equals %s but not vice versa",
o,
o2));
        }
        // Test for the guarantee that equal objects
        // must have equal hash codes
        if (o2.hashCode() != o.hashCode()) {
          // Don't use Assert.assertEquals directly because it has
          // quite a lot of overhead
          Assert.fail(
String.format(java.util.Locale.US,"%s and %s don't have equal hash codes",
o,
o2));
        }
      } else {
        if (o2.equals(o)) {
          Assert.fail(String.format(java.util.Locale.US,"%s does not equal %s but not vice versa",
o,
o2));
        }
        // At least check that hashCode doesn't throw
        try {
 o.hashCode();
} catch (Exception ex) {
Assert.fail(ex.toString());
throw new IllegalStateException("", ex);
}
        try {
 o2.hashCode();
} catch (Exception ex) {
Assert.fail(ex.toString());
throw new IllegalStateException("", ex);
}
      }
    }
  }
