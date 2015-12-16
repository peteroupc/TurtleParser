package com.upokecenter.test; import com.upokecenter.util.*;
import org.junit.Assert;
import org.junit.Test;
import com.upokecenter.text.*;

  public class EncodingsTest {
    @Test
    public void TestDecodeToString() {
      // not implemented yet
    }
    @Test
    public void TestEncodeToBytes() {
      try {
        ICharacterInput ici = null;
        Encodings.EncodeToBytes(ici, Encodings.UTF8);
        Assert.fail("Should have failed");
      } catch (NullPointerException ex) {
        System.out.print("");
      } catch (Exception ex) {
        Assert.fail(ex.toString());
        throw new IllegalStateException("", ex);
      }
      try {
        Encodings.EncodeToBytes("test", null);
        Assert.fail("Should have failed");
      } catch (NullPointerException ex) {
        System.out.print("");
      } catch (Exception ex) {
        Assert.fail(ex.toString());
        throw new IllegalStateException("", ex);
      }
    }
    @Test
    public void TestEncodeToWriter() {
      // not implemented yet
    }
    @Test
    public void TestGetDecoderInput() {
      // not implemented yet
    }
    @Test
    public void TestGetEncoding() {
      if ((Encodings.GetEncoding("utf-8")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("Utf-8")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("uTf-8")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("utF-8")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("UTF-8")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("utg-8")) != null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("utf-9")) != null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("   utf-8    ")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("   utf-8")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("utf-8    ")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("\t\tutf-8\t\t")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding(" \r\n utf-8 \r ")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("\nutf-8\n")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("\tutf-8\t")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("\rutf-8\r")) == null) {
        Assert.fail();
      }
      if ((Encodings.GetEncoding("\futf-8\f")) == null) {
        Assert.fail();
      }
    }
    @Test
    public void TestInputToString() {
      // not implemented yet
    }
    @Test
    public void TestResolveAlias() {
      Assert.assertEquals("", Encodings.ResolveAlias(null));
      Assert.assertEquals("", Encodings.ResolveAlias(""));
      {
        String stringTemp = Encodings.ResolveAlias("iso-8859-1");
        Assert.assertEquals(
        "windows-1252",
        stringTemp);
      }
      {
        String stringTemp = Encodings.ResolveAlias("windows-1252");
        Assert.assertEquals(
        "windows-1252",
        stringTemp);
      }
      {
        String stringTemp = Encodings.ResolveAlias("us-ascii");
        Assert.assertEquals(
        "windows-1252",
        stringTemp);
      }
      Assert.assertEquals("", Encodings.ResolveAlias("utf-7"));
      Assert.assertEquals("", Encodings.ResolveAlias("replacement"));
      {
        String stringTemp = Encodings.ResolveAlias("hz-gb-2312");
        Assert.assertEquals(
        "replacement",
        stringTemp);
      }
    }
    @Test
    public void TestResolveAliasForEmail() {
      Assert.assertEquals("", Encodings.ResolveAliasForEmail(null));
      Assert.assertEquals("",
           Encodings.ResolveAliasForEmail(""));
      {
        String stringTemp = Encodings.ResolveAliasForEmail("iso-8859-1");
        Assert.assertEquals(
        "iso-8859-1",
        stringTemp);
      }
      {
        String stringTemp = Encodings.ResolveAliasForEmail("windows-1252");
        Assert.assertEquals(
        "windows-1252",
        stringTemp);
      }
      {
        String stringTemp = Encodings.ResolveAliasForEmail("us-ascii");
        Assert.assertEquals(
        "us-ascii",
        stringTemp);
      }
      {
        String stringTemp = Encodings.ResolveAliasForEmail("utf-7");
        Assert.assertEquals(
        "utf-7",
        stringTemp);
      }
      Assert.assertEquals("", Encodings.ResolveAliasForEmail(
    "replacement"));
      {
        String stringTemp = Encodings.ResolveAliasForEmail("hz-gb-2312");
        Assert.assertEquals(
        "replacement",
        stringTemp);
      }
    }

    @Test
    public void TestStringToBytes() {
      // not implemented yet
    }
    @Test
    public void TestStringToInput() {
      try {
        Encodings.StringToInput(null, 0, 0);
        Assert.fail("Should have failed");
      } catch (NullPointerException ex) {
        System.out.print("");
      } catch (Exception ex) {
        Assert.fail(ex.toString());
        throw new IllegalStateException("", ex);
      }
      try {
        Encodings.StringToInput("t", -1, 1);
        Assert.fail("Should have failed");
      } catch (IllegalArgumentException ex) {
        System.out.print("");
      } catch (Exception ex) {
        Assert.fail(ex.toString());
        throw new IllegalStateException("", ex);
      }
      try {
        Encodings.StringToInput("t", 5, 1);
        Assert.fail("Should have failed");
      } catch (IllegalArgumentException ex) {
        System.out.print("");
      } catch (Exception ex) {
        Assert.fail(ex.toString());
        throw new IllegalStateException("", ex);
      }
      try {
        Encodings.StringToInput("t", 0, -1);
        Assert.fail("Should have failed");
      } catch (IllegalArgumentException ex) {
        System.out.print("");
      } catch (Exception ex) {
        Assert.fail(ex.toString());
        throw new IllegalStateException("", ex);
      }
      try {
        Encodings.StringToInput("t", 0, 5);
        Assert.fail("Should have failed");
      } catch (IllegalArgumentException ex) {
        System.out.print("");
      } catch (Exception ex) {
        Assert.fail(ex.toString());
        throw new IllegalStateException("", ex);
      }
      try {
        Encodings.StringToInput("tt", 1, 2);
        Assert.fail("Should have failed");
      } catch (IllegalArgumentException ex) {
        System.out.print("");
      } catch (Exception ex) {
        Assert.fail(ex.toString());
        throw new IllegalStateException("", ex);
      }
    }
  }
