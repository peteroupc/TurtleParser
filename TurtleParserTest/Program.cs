using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using PeterO;
using PeterO.Rdf;

namespace EncodingTest {
  internal class Program {
    internal static void Main() {
       foreach (var f in Directory.GetFiles(
        ".",
        "*.ttl")) {
         using (var fs = new FileStream(f, FileMode.Open)) {
                    Console.WriteLine(f);
          var bs = DataIO.ToReader(fs);
          var ttlp = new TurtleParser(bs);
          var sets = ttlp.Parse();
          foreach (var rdf in sets) {
            Console.WriteLine(rdf);
          }
        }
      }
    }
  }
}
