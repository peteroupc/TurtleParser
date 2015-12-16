package com.upokecenter.test; import com.upokecenter.util.*;
/*
Written by Peter O. in 2014.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/
If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
 */
import org.junit.Assert;
import org.junit.Test;
import com.upokecenter.util.*;
import com.upokecenter.text.*;
import java.util.*;

  public class CharsetsTest {
    @Test
    public void TestShiftJIS() {
      // Adapted from the public domain Gonk test cases
      byte[] bytes;
      ICharacterEncoding charset = Encodings.GetEncoding("shift_jis");
      bytes = new byte[] { (byte)0x82, 0x58, 0x33, 0x41, 0x61, 0x33, (byte)0x82, 0x60,
        (byte)0x82, (byte)0x81, 0x33, (byte)0xb1, (byte)0xaf, 0x33, (byte)0x83, 0x41,
        (byte)0x83, (byte)0x96, 0x33, (byte)0x82, (byte)0xa0, 0x33, (byte)0x93, (byte)0xfa,
        0x33, 0x3a, 0x3c, 0x33, (byte)0x81, (byte)0x80, (byte)0x81, (byte)0x8e,
        0x33, 0x31, (byte)0x82, 0x51, 0x41, 0x61, (byte)0x82, 0x51,
        (byte)0x82, 0x60, (byte)0x82, (byte)0x81, (byte)0x82, 0x51, (byte)0xb1, (byte)0xaf,
        (byte)0x82, 0x51, (byte)0x83, 0x41, (byte)0x83, (byte)0x96, (byte)0x82, 0x51,
        (byte)0x82, (byte)0xa0, (byte)0x82, 0x51, (byte)0x93, (byte)0xfa, (byte)0x82, 0x51,
        0x3a, 0x3c, (byte)0x82, 0x51, (byte)0x81, (byte)0x80, (byte)0x81, (byte)0x8e,
        (byte)0x82, 0x51  };
      String expected =

  "\uFF19\u0033\u0041\u0061\u0033\uFF21\uFF41\u0033\uFF71\uFF6F\u0033\u30A2\u30F6\u0033\u3042\u0033\u65E5\u0033\u003A\u003C\u0033\u00F7\u2103\u0033\u0031\uFF12\u0041\u0061\uFF12\uFF21\uFF41\uFF12\uFF71\uFF6F\uFF12\u30A2\u30F6\uFF12\u3042\uFF12\u65E5\uFF12\u003A\u003C\uFF12\u00F7\u2103\uFF12"
        ;
      Assert.assertEquals(expected, Encodings.DecodeToString(charset, bytes));
    }

    private static void TestEncodingRoundTrip(String str, ICharacterEncoding
      encoding) {
      byte[] bytes;
      String str2;
      bytes = Encodings.EncodeToBytes(str, encoding);
      str2 = Encodings.DecodeToString(encoding, bytes);
      Assert.assertEquals(str, str2);
    }

    @Test
    public void TestGB18030() {
      ICharacterEncoding encoding = Encodings.GetEncoding("gb18030");
      TestEncodingRoundTrip("\uffff", encoding);
      TestEncodingRoundTrip("\ud800\udc00", encoding);
      TestEncodingRoundTrip("\udbff\udfff", encoding);
    }

    @Test
    public void TestIso2022JP() {
      byte[] bytes;
      ICharacterEncoding charset = Encodings.GetEncoding("iso-2022-jp");
      bytes = new byte[] { 0x20, 0x41, 0x61, 0x5c  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
" Aa\\",
stringTemp);
}
      // Illegal byte in escape middle state
      bytes = new byte[] { 0x1b, 0x28, 0x47, 0x21, 0x41, 0x31, 0x5c  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\ufffd\u0028\u0047!A1\\",
        stringTemp);
      }
      // Katakana
      bytes = new byte[] { 0x1b, 0x28, 0x49, 0x21, 0x41, 0x31, 0x5c  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\uff61\uff81\uff71\uff9c",
        stringTemp);
      }
      bytes = new byte[] { 0x1b, 0x28, 0x49, 0x20, 0x41, 0x61, 0x5c  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\ufffd\uff81\ufffd\uff9c",
        stringTemp);
      }
      // ASCII state via escape
      bytes = new byte[] { 0x1b, 0x28, 0x42, 0x20, 0x41, 0x61, 0x5c  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        " Aa\\",
        stringTemp);
      }
      bytes = new byte[] { 0x1b, 0x28, 0x4a, 0x20, 0x41, 0x61, 0x5c  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        " Aa\u00a5",
        stringTemp);
      }
      // JIS0208 state
   bytes = new byte[] { 0x1b, 0x24, 0x40, 0x21, 0x21, 0x21, 0x22, 0x21, 0x23  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\u3000\u3001\u3002",
        stringTemp);
      }
   bytes = new byte[] { 0x1b, 0x24, 0x42, 0x21, 0x21, 0x21, 0x22, 0x21, 0x23  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\u3000\u3001\u3002",
        stringTemp);
      }
      bytes = new byte[] { 0x1b, 0x24, 0x42, 0x21, 0x21, 0x21, 0x22, 0x0a,
        0x21, 0x23  };
      // Illegal state
   bytes = new byte[] { 0x1b, 0x24, 0x4f, 0x21, 0x21, 0x21, 0x23, 0x21, 0x23  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\ufffd\u0024\u004f!!!\u0023!#",
        stringTemp);
      }
      // Illegal state
      bytes = new byte[] { 0x1b, 0x24, 0x28, 0x4f, 0x21, 0x21, 0x21, 0x23,
        0x21, 0x23  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\ufffd\u0024\u0028\u004f!!!\u0023!#",
        stringTemp);
      }
      // Illegal state at end
      bytes = new byte[] { 0x41, 0x1b  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "A\ufffd",
        stringTemp);
      }
      bytes = new byte[] { 0x41, 0x1b, 0x27  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "A\ufffd'",
        stringTemp);
      }
      bytes = new byte[] { 0x41, 0x1b, 0x24  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "A\ufffd$",
        stringTemp);
      }
      bytes = new byte[] { 0x41, 0x1b, 0x24, 0x28  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "A\ufffd$\u0028",
        stringTemp);
      }
    }

    @Test
    public void TestEucJP() {
      byte[] bytes;
      ICharacterEncoding charset = Encodings.GetEncoding("euc-jp");
      bytes = new byte[] { (byte)0x8e  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\ufffd",
        stringTemp);
      }
      bytes = new byte[] { (byte)0x8e, 0x21  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\ufffd!",
        stringTemp);
      }
      bytes = new byte[] { (byte)0x8e, (byte)0x8e, (byte)0xa1  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\ufffd\uff61",
        stringTemp);
      }
      bytes = new byte[] { (byte)0x8f  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\ufffd",
stringTemp);
}
      bytes = new byte[] { (byte)0x8f, 0x21  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\ufffd!",
stringTemp);
}
      bytes = new byte[] { (byte)0x8f, (byte)0xa1  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\ufffd",
stringTemp);
}
      bytes = new byte[] { (byte)0x8f, (byte)0xa1, 0x21  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\ufffd!",
stringTemp);
}
      bytes = new byte[] { (byte)0x90  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\ufffd",
stringTemp);
}
      bytes = new byte[] { (byte)0x90, 0x21  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\ufffd!",
stringTemp);
}
      bytes = new byte[] { (byte)0xa1  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\ufffd",
stringTemp);
}
      bytes = new byte[] { (byte)0xa1, (byte)0xa1  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\u3000",
stringTemp);
}
      bytes = new byte[] { (byte)0x90, (byte)0xa1, (byte)0xa1  };
      {
String stringTemp = Encodings.DecodeToString(charset,
          bytes);
Assert.assertEquals(
"\ufffd\u3000",
stringTemp);
}
      bytes = new byte[] { (byte)0x90, (byte)0xa1, (byte)0xa1, (byte)0xa1  };
      {
        String stringTemp = Encodings.DecodeToString(charset, bytes);
        Assert.assertEquals(
        "\ufffd\u3000\ufffd",
        stringTemp);
      }
      bytes = new byte[] { (byte)0xa1, 0x21  };
      {
String stringTemp = Encodings.DecodeToString(charset, bytes);
Assert.assertEquals(
"\ufffd!",
stringTemp);
}
      String result;
      bytes = new byte[] { 0x15, (byte)0xf2, (byte)0xbf, (byte)0xdd, (byte)0xd7, 0x13, (byte)0xeb, (byte)0xcf,
        (byte)0x8e, (byte)0xd6, (byte)0x8f, (byte)0xec, (byte)0xe9, (byte)0x8f, (byte)0xd6, (byte)0xe6, (byte)0x8f, (byte)0xd3, (byte)0xa3,
        (byte)0x8e, (byte)0xd4, 0x66, (byte)0x8f, (byte)0xb9, (byte)0xfc, (byte)0x8e, (byte)0xb0, (byte)0x8f, (byte)0xea, (byte)0xd8,
        0x29, (byte)0x8e, (byte)0xca, (byte)0x8e, (byte)0xd4, (byte)0xc9, (byte)0xb5, 0x1e, 0x09, (byte)0x8e, (byte)0xab,
        (byte)0xc2, (byte)0xc5, (byte)0x8e, (byte)0xa7, (byte)0x8e, (byte)0xb6, 0x3d, (byte)0xe1, (byte)0xd9, (byte)0xb7, (byte)0xd5,
        0x7b, 0x05, (byte)0xe6, (byte)0xce, 0x1d, (byte)0x8f, (byte)0xbd, (byte)0xbe, (byte)0xd8, (byte)0xae, (byte)0x8e,
        (byte)0xc3, (byte)0x8f, (byte)0xc1, (byte)0xda, (byte)0xd5, (byte)0xbb, (byte)0xb2, (byte)0xa2, (byte)0xcc, (byte)0xd4, 0x42,
        (byte)0x8e, (byte)0xa2, (byte)0xed, (byte)0xd4, (byte)0xc6, (byte)0xe0, (byte)0x8f, (byte)0xe0, (byte)0xd5, (byte)0x8e, (byte)0xd8,
        (byte)0xb0, (byte)0xc8, (byte)0x8f, (byte)0xa2, (byte)0xb8, (byte)0xb9, (byte)0xf1, (byte)0x8e, (byte)0xb0, (byte)0xd9, (byte)0xc0,
        0x13  };
      result =

  "\u0015\u9ba8\u6bbc\u0013\u8a85\uff96\u9ea8\u81f2\u7c67\uff94f\u5aba\uff70\u9b8a)\uff8a\uff94\u8b2c\u001e\u0009\uff6b\u59a5\uff67\uff76=\u75ca\u834a"
        +

  "{\u0005\u8004\u001d\u5fd1\u60bd\uff83\u6595\u5a9a\u65fa\u731bB\uff62\u8f33\u5948\u8ec1\uff98\u978d\u0384\u56fd\uff70\u62c8\u0013"
          ;
      Assert.assertEquals(result, (Encodings.DecodeToString(charset, bytes)));
    }
    public static void TestSingleByteRoundTrip(String name) {
      TestSingleByteRoundTrip(Encodings.GetEncoding(name, true));
    }

    public static void TestSingleByteRoundTrip(ICharacterEncoding enc) {
      int[] codepoints = new int[256];
      int[] codesrc = new int[256];
      ICharacterEncoder encoder = enc.GetEncoder();
      ICharacterDecoder decoder = enc.GetDecoder();
      int codetotal = 0;
      byte[] bytes = new byte[256];
      for (int i = 0; i < 256; ++i) {
        bytes[i] = (byte)(i);
      }
      IByteReader ib = DataIO.ToByteReader(bytes);
      for (int i = 0; i < 256; ++i) {
        int c = decoder.ReadChar(ib);
        if (c == -1) {
 Assert.fail();
}
        if (c != -2) {
          codepoints[codetotal] = c;
          codesrc[codetotal] = i;
          ++codetotal;
        }
      }
      ArrayWriter aw = new ArrayWriter();
      for (int i = 0; i < codetotal; ++i) {
        int c = encoder.Encode(codepoints[i], aw);
        if (c < 0) {
 Assert.fail();
}
      }
      bytes = aw.ToArray();
      for (int i = 0; i < codetotal; ++i) {
        int b = ((int)bytes[i]) & 0xff;
        if (b != codesrc[i]) {
          Assert.assertEquals(codesrc[i], b);
        }
      }
    }

    @Test
    public void TestCodePages() {
      for (int j = 0; j < SingleByteNames.length; ++j) {
        ICharacterEncoding enc = Encodings.GetEncoding(SingleByteNames[j]);
        ICharacterDecoder dec = enc.GetDecoder();
        byte[] bytes = new byte[256];
        int[] ints = new int[256];
        int count = 0;
        for (int i = 0; i < 256; ++i) {
          bytes[i] = (byte)i;
        }
        IByteReader reader = DataIO.ToByteReader(bytes);
        for (int i = 0; i < 256; ++i) {
          ints[i] = dec.ReadChar(reader);
          if (ints[i] >= 0) {
            ++count;
          }
        }
        if (count != 256) {
 continue;
}
        StringBuilder builder = new StringBuilder();
     builder.append("CODEPAGE 1\nCPINFO 1 0x3f 0x3f\nMBTABLE " +
          TestCommon.IntToString(count)+ "\n");
        for (int i = 0; i < 256; ++i) {
          if (ints[i] >= 0) {
            builder.append(TestCommon.IntToString(i) + " " +
              TestCommon.IntToString(ints[i]) + "\n");
          }
        }
        builder.append("WCTABLE " + count + "\n");
        for (int i = 0; i < 256; ++i) {
          if (ints[i] >= 0) {
            builder.append(TestCommon.IntToString(ints[i]) + " " +
              TestCommon.IntToString(i) + "\n");
          }
        }
        builder.append("ENDCODEPAGE\n");
   CodePageEncoding cpe = new CodePageEncoding(Encodings.StringToInput(builder.toString()));
        TestSingleByteRoundTrip(cpe);
      }
    }

    private final String[] SingleByteNames = new String[] {
      "windows-1252",
      "us-ascii",
      "x-user-defined",
      "iso-8859-1",
      "iso-8859-2",
      "iso-8859-10",
      "windows-1250",
      "iso-8859-4",
      "windows-1257",
      "iso-8859-13",
      "iso-8859-14",
      "iso-8859-16",
      "windows-1255",
      "iso-8859-8",
      "iso-8859-8-i",
      "iso-8859-5",
      "ibm866",
      "koi8-r",
      "windows-1251",
      "x-mac-cyrillic",
      "koi8-u",
      "iso-8859-7",
      "windows-1253",
      "iso-8859-6",
      "windows-1256",
      "iso-8859-3",
      "iso-8859-15",
      "windows-1254",
      "windows-874",
      "windows-1258",
      "macintosh"
    };

    public static void TestUtfRoundTrip(
       ICharacterEncoding enc) {
      ICharacterEncoder encoder = enc.GetEncoder();
      ICharacterDecoder decoder = enc.GetDecoder();
         TestUtfRoundTrip(encoder, decoder);
    }

    public static void TestUtfRoundTrip(
       ICharacterEncoder encoder,
       ICharacterDecoder decoder) {
      ArrayWriter aw = new ArrayWriter();
      for (int i = 0; i < 0x110000; ++i) {
        if (i >= 0xd800 && i < 0xe000) {
          continue;
        }
        int e = encoder.Encode(i, aw);
        if (e == -2) {
          Assert.fail("Failed to encode " + i);
        }
      }
      IByteReader reader = DataIO.ToByteReader(aw.ToArray());
      for (int i = 0; i < 0x110000; ++i) {
        if (i >= 0xd800 && i < 0xe000) {
          continue;
        }
        int c = decoder.ReadChar(reader);
        if (c != i) {
          Assert.assertEquals(i, c);
        }
      }
    }

    public static void TestCJKRoundTrip(String name) {
      ICharacterEncoding enc = Encodings.GetEncoding(name, true);
      ICharacterEncoder encoder = enc.GetEncoder();
      ICharacterDecoder decoder = enc.GetDecoder();
      ArrayList<Integer> list = new ArrayList<Integer>();
      ArrayWriter aw = new ArrayWriter();
      for (int i = 0; i < 0x110000; ++i) {
      if (i >= 0xd800 && i < 0xe000) {
          continue;
        }
        if (i == 0xa5 || i == 0x203e || i == 0x0e || i == 0x0f || i == 0x1b ||
          i == 0x2022) {
          // ignore certain characters that intentionally
          // don't round trip in certain encodings
          continue;
        }
        int e = encoder.Encode(i, aw);
        if (e >= 0) {
          list.add(i);
        }
      }
      while (encoder.Encode(-1, aw) >= 0) {
      }
      IByteReader reader = DataIO.ToByteReader(aw.ToArray());
      for (int i = 0;i<list.size(); ++i) {
        int ch = list.get(i);
        int c = decoder.ReadChar(reader);
        if (c != ch) {
          Assert.fail(name + ": expected " + ch + ", was " + c);
        }
      }
    }
    @Test
    public void TestGBK() {
      TestCJKRoundTrip("gbk");
    }
    //@Test
    public void TestGB18030RoundTrip() {
      // TODO: Disabled for now since Encoding Standard
      // has issues here
      TestCJKRoundTrip("gb18030");
    }
    @Test
    public void TestBig5() {
      TestCJKRoundTrip("big5");
    }
    @Test
    public void TestKoreanEUC() {
      TestCJKRoundTrip("euc-kr");
    }
    @Test
    public void TestShiftJISRoundTrip() {
      TestCJKRoundTrip("shift-jis");
    }
    @Test
    public void TestEucJPRoundTrip() {
      TestCJKRoundTrip("euc-jp");
    }
    @Test
    public void TestIso2022JPRoundTrip() {
      TestCJKRoundTrip("iso-2022-jp");
    }

    @Test
    public void TestReplacementEncoding() {
      if ((Encodings.GetEncoding("replacement")) != null) {
 Assert.fail();
 }
      ICharacterEncoding enc = Encodings.GetEncoding("hz-gb-2312", true);
      ICharacterEncoder encoder = enc.GetEncoder();
      ICharacterDecoder decoder = enc.GetDecoder();
      IByteReader reader = DataIO.ToByteReader(new byte[] { 0, 0, 0, 0  });
      Assert.assertEquals(-2, decoder.ReadChar(reader));
      Assert.assertEquals(-1, decoder.ReadChar(reader));
      TestUtfRoundTrip(
        encoder,
        Encodings.GetEncoding("utf-8", true).GetDecoder());
    }

    @Test
    public void TestSingleByteEncodings() {
      for (int i = 0; i < SingleByteNames.length; ++i) {
        TestSingleByteRoundTrip(SingleByteNames[i]);
      }
    }

    @Test
    public void TestUtf7RoundTrip() {
      TestUtfRoundTrip(Encodings.GetEncoding("utf-7", true));
    }

    @Test
    public void TestUtf8RoundTrip() {
      TestUtfRoundTrip(Encodings.GetEncoding("utf-8", true));
    }

    public static List<byte[]> GenerateIllegalUtf8Sequences() {
      ArrayList<byte[]> list = new ArrayList<byte[]>();
      // Generate illegal single bytes
      for (int i = 0x80; i <= 0xff; ++i) {
        if (i < 0xc2 || i > 0xf4) {
          list.add(new byte[] { (byte)i, (byte)0x80  });
        }
        list.add(new byte[] { (byte)i  });
      }
      list.add(new byte[] { (byte)0xe0, (byte)0xa0  });
      list.add(new byte[] { (byte)0xe1, (byte)0x80  });
      list.add(new byte[] { (byte)0xef, (byte)0x80  });
      list.add(new byte[] { (byte)0xf0, (byte)0x90  });
      list.add(new byte[] { (byte)0xf1, (byte)0x80  });
      list.add(new byte[] { (byte)0xf3, (byte)0x80  });
      list.add(new byte[] { (byte)0xf4, (byte)0x80  });
      list.add(new byte[] { (byte)0xf0, (byte)0x90, (byte)0x80  });
      list.add(new byte[] { (byte)0xf1, (byte)0x80, (byte)0x80  });
      list.add(new byte[] { (byte)0xf3, (byte)0x80, (byte)0x80  });
      list.add(new byte[] { (byte)0xf4, (byte)0x80, (byte)0x80  });
      // Generate illegal multibyte sequences
      for (int i = 0x00; i <= 0xff; ++i) {
        if (i < 0x80 || i > 0xbf) {
          list.add(new byte[] { (byte)0xc2, (byte)i  });
          list.add(new byte[] { (byte)0xdf, (byte)i  });
          list.add(new byte[] { (byte)0xe1, (byte)i, (byte)0x80  });
          list.add(new byte[] { (byte)0xef, (byte)i, (byte)0x80  });
          list.add(new byte[] { (byte)0xf1, (byte)i, (byte)0x80, (byte)0x80  });
          list.add(new byte[] { (byte)0xf3, (byte)i, (byte)0x80, (byte)0x80  });
          list.add(new byte[] { (byte)0xe0, (byte)0xa0, (byte)i  });
          list.add(new byte[] { (byte)0xe1, (byte)0x80, (byte)i  });
          list.add(new byte[] { (byte)0xef, (byte)0x80, (byte)i  });
          list.add(new byte[] { (byte)0xf0, (byte)0x90, (byte)i, (byte)0x80  });
          list.add(new byte[] { (byte)0xf1, (byte)0x80, (byte)i, (byte)0x80  });
          list.add(new byte[] { (byte)0xf3, (byte)0x80, (byte)i, (byte)0x80  });
          list.add(new byte[] { (byte)0xf4, (byte)0x80, (byte)i, (byte)0x80  });
          list.add(new byte[] { (byte)0xf0, (byte)0x90, (byte)0x80, (byte)i  });
          list.add(new byte[] { (byte)0xf1, (byte)0x80, (byte)0x80, (byte)i  });
          list.add(new byte[] { (byte)0xf3, (byte)0x80, (byte)0x80, (byte)i  });
          list.add(new byte[] { (byte)0xf4, (byte)0x80, (byte)0x80, (byte)i  });
        }
        if (i < 0xa0 || i > 0xbf) {
          list.add(new byte[] { (byte)0xe0, (byte)i, (byte)0x80  });
        }
        if (i < 0x90 || i > 0xbf) {
          list.add(new byte[] { (byte)0xf0, (byte)i, (byte)0x80, (byte)0x80  });
        }
        if (i < 0x80 || i > 0x8f) {
          list.add(new byte[] { (byte)0xf4, (byte)i, (byte)0x80, (byte)0x80  });
        }
      }
      return list;
    }

    @Test
    public void TestUtf8IllegalBytes() {
      for (byte[] seq : GenerateIllegalUtf8Sequences()) {
        String str = Encodings.DecodeToString(Encodings.UTF8, seq);
        if (!(str.length()>0))Assert.fail();
        if (!(str.indexOf('\ufffd')==0))Assert.fail();
      }
    }

    @Test
    public void TestUtf16LERoundTrip() {
      TestUtfRoundTrip(Encodings.GetEncoding("utf-16le", true));
    }

    @Test
    public void TestUtf16BERoundTrip() {
      TestUtfRoundTrip(Encodings.GetEncoding("utf-16be", true));
    }

    public static void TestUtf7One(String input, String expected) {
      Assert.assertEquals(expected, Encodings.DecodeToString(
        Encodings.GetEncoding("utf-7", true),
        Encodings.EncodeToBytes(input, Encodings.UTF8)));
    }

    @Test
    public void TestUtf7() {
      TestUtf7One("\\", "\ufffd");
      TestUtf7One("~", "\ufffd");
      TestUtf7One("\u0001", "\ufffd");
      TestUtf7One("\u007f", "\ufffd");
      TestUtf7One("\r\n\t '!\"#'(),$-%@[]^&=<>;*_`{}./:|?",
        "\r\n\t '!\"#'(),$-%@[]^&=<>;*_`{}./:|?");
      TestUtf7One("x+--", "x+-");
      TestUtf7One("x+-y", "x+y");
      // Illegal byte after plus
      TestUtf7One("+!", "\ufffd!");
      TestUtf7One("+\n", "\ufffd\n");
      TestUtf7One("+\u007f", "\ufffd\ufffd");
      TestUtf7One("+", "\ufffd");
      // Incomplete byte
      TestUtf7One("+D?", "\ufffd?");
      TestUtf7One("+D\u007f", "\ufffd\ufffd");
      TestUtf7One("+D", "\ufffd");
      // Only one UTF-16 byte
      TestUtf7One("+DE?", "\ufffd?");
      TestUtf7One("+DE", "\ufffd");
      TestUtf7One("+DE\u007f", "\ufffd\ufffd");
      // UTF-16 code unit
      TestUtf7One("+DEE?", "\u0c41?");
      TestUtf7One("+DEE", "\u0c41");
      TestUtf7One("+DEE\u007f", "\u0c41\ufffd");
      // UTF-16 code unit (redundant pad bit)
      TestUtf7One("+DEF?", "\u0c41\ufffd?");
      TestUtf7One("+DEF", "\u0c41\ufffd");
      TestUtf7One("+DEF\u007f", "\u0c41\ufffd\ufffd");
      // High surrogate code unit
      TestUtf7One("+2AA?", "\ufffd?");
      TestUtf7One("+2AA", "\ufffd");
      TestUtf7One("+2AA\u007f", "\ufffd\ufffd");
      // Low surrogate code unit
      TestUtf7One("+3AA?", "\ufffd?");
      TestUtf7One("+3AA", "\ufffd");
      TestUtf7One("+3AA\u007f", "\ufffd\ufffd");
      // Surrogate pair
      TestUtf7One("+2ADcAA?", "\ud800\udc00?");
      TestUtf7One("+2ADcAA", "\ud800\udc00");
      TestUtf7One("+2ADcAA\u007f", "\ud800\udc00\ufffd");
      // High surrogate followed by surrogate pair
      TestUtf7One("+2ADYANwA?", "\ufffd\ud800\udc00?");
      TestUtf7One("+2ADYANwA", "\ufffd\ud800\udc00");
      TestUtf7One("+2ADYANwA\u007f", "\ufffd\ud800\udc00\ufffd");
      // High surrogate followed by non-surrogate
      TestUtf7One("+2AAAwA?", "\ufffd\u00c0?");
      TestUtf7One("+2AAAwA", "\ufffd\u00c0");
      TestUtf7One("+2AAAwA\u007f", "\ufffd\u00c0\ufffd");
      // Two UTF-16 code units
      TestUtf7One("+AMAA4A?", "\u00c0\u00e0?");
      TestUtf7One("+AMAA4A", "\u00c0\u00e0");
      TestUtf7One("+AMAA4A-Next", "\u00c0\u00e0Next");
      TestUtf7One("+AMAA4A!Next", "\u00c0\u00e0!Next");
      TestUtf7One("+AMAA4A\u007f", "\u00c0\u00e0\ufffd");
      // Two UTF-16 code units (redundant pad bit)
      TestUtf7One("+AMAA4B?", "\u00c0\u00e0\ufffd?");
      TestUtf7One("+AMAA4B", "\u00c0\u00e0\ufffd");
      TestUtf7One("+AMAA4B-Next", "\u00c0\u00e0\ufffdNext");
      TestUtf7One("+AMAA4B!Next", "\u00c0\u00e0\ufffd!Next");
      TestUtf7One("+AMAA4B\u007f", "\u00c0\u00e0\ufffd\ufffd");
    }
  }
