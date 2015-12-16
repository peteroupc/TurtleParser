package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;
import com.upokecenter.text.*;

  public class EncodingUtf16BE implements ICharacterEncoding {
   public ICharacterDecoder GetDecoder() {
      return EncodingUtf16.GetDecoder2(true);
    }

    public ICharacterEncoder GetEncoder() {
      return EncodingUtf16.GetEncoder2(true);
    }
  }
