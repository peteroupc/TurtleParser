using System;
using System.IO;
using PeterO;

namespace EncodingTest {
  internal class Program {
    internal static void Main() {
       foreach (var f in Directory.GetFiles(
         ".",
         "*.ttl")) {
         using (var fs = new FileStream(f, FileMode.Open)) {
                    Console.WriteLine(f);
                    var bs = DataIO.ToReader(fs);
                    var ttlp = new PeterO.Rdf.TurtleParser(bs);
                    var sets = ttlp.Parse();
                    foreach (var rdf in sets) {
            Console.WriteLine(rdf);
          }
        }
      }
    }
  }
}
