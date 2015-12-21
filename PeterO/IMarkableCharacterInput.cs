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

    /// <summary>Not documented yet.</summary>
public interface IMarkableCharacterInput : ICharacterInput {
    /// <summary>Gets the zero-based character position in the stream from
    /// the last-set mark.</summary>
    /// <returns>Not documented yet.</returns>
   int getMarkPosition();

    /// <summary>Moves the stream position back the given number of
    /// characters. No mark was set, or the position is too close to the
    /// currently set mark.</summary>
   void moveBack(int count);

    /// <summary>Sets a mark on the stream's current position. @return
    /// Always 0.</summary>
    /// <returns>Not documented yet.</returns>
   int setHardMark();

    /// <summary>Sets the stream's position from the last set mark. @param
    /// pos Zero-based character offset from the last set mark. @ No mark
    /// was set, or the position is less than 1, or the position reaches
    /// the end of the stream.</summary>
   void setMarkPosition(int pos);

    /// <summary>If no mark is set, sets a mark on the stream, and
    /// characters read before the currently set mark are no longer
    /// available, while characters read after will be available if
    /// moveBack is called. Otherwise, behaves like
    /// getMarkPosition.</summary>
    /// <returns>Not documented yet.</returns>
    int setSoftMark();
}
}
