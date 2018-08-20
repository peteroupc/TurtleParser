/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/
namespace PeterO {
using System;
using PeterO.Text;

    /// <summary>Not documented yet.</summary>
  ///
  ///
public interface IMarkableCharacterInput : ICharacterInput {
    /// <summary>Gets the zero-based character position in the stream from
    /// the last-set mark.</summary>
    /// <returns>The return value is not documented yet.</returns>
  ///
  ///
   int getMarkPosition();

    /// <summary>Moves the stream position back the given number of
    /// characters.</summary>
    /// <param name='count'>The parameter <paramref name='count'/> is not
    /// documented yet.</param>
  ///
  ///
   void moveBack(int count);

    /// <summary>Sets a mark on the stream's current position.</summary>
    /// <returns>The return value is not documented yet.</returns>
  ///
  ///
   int setHardMark();

    /// <summary>Sets the stream's position from the last set mark.
    /// <param name='pos'>Zero-based character offset from the last set
    /// mark.</param></summary>
    /// <param name='pos'>The parameter <paramref name='pos'/> is not
    /// documented yet.</param>
  ///
  ///
   void setMarkPosition(int pos);

    /// <summary>If no mark is set, sets a mark on the stream, and
    /// characters read before the currently set mark are no longer
    /// available, while characters read after will be available if
    /// moveBack is called. Otherwise, behaves like
    /// getMarkPosition.</summary>
    /// <returns>The return value is not documented yet.</returns>
  ///
  ///
    int setSoftMark();
}
}
