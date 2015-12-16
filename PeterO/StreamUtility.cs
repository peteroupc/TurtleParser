/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
*/
namespace PeterO {
using System;
using PeterO.Text;
using System.IO;
using PeterO;

public sealed class StreamUtility {
    /// <summary>Copies the data from one input stream to an output stream.
    /// @param stream A readable data stream @param output A writable data
    /// stream to write the data. @ An I/O error occurred.</summary>
    /// <param name='stream'>Not documented yet.</param>
    /// <param name='output'>Not documented yet.</param>
public static void copyStream(IReader stream, Stream
    output) {
    var buffer = new byte[8192];
    while (true) {
      int count = stream.Read(buffer, 0, buffer.Length);
      if (count< 0) {
        break;
      }
      output.Write(buffer, 0, count);
    }
  }

  public static void skipToEnd(IReader stream) {
    if (stream == null) {
 return;
}
    while (true) {
      var x = new byte[1024];
      try {
        int c = stream.Read(x, 0, x.Length);
        if (c< 0) {
          break;
        }
      } catch (IOException) {
        break;  // maybe this stream is already closed
       }
    }
  }

  private StreamUtility() {}
}
}
