using PeterO.Text;

/*
Written in 2013 by Peter Occil.
Any copyright to this work is released to the Public Domain.
In case this is not possible, this work is also
licensed under the Unlicense: https://unlicense.org/

*/
namespace PeterO {
  /// <summary>Not documented yet.</summary>
  public interface IMarkableCharacterInput : ICharacterInput {
    /// <summary>Gets the zero-based character position in the stream from
    /// the last-set mark.</summary>
    /// <returns>The return value is not documented yet.</returns>
    int GetMarkPosition();

    /// <summary>Moves the stream position back the specified number of
    /// characters.</summary>
    /// <param name='count'>The parameter <paramref name='count'/> is a
    /// 32-bit signed integer.</param>
    void MoveBack(int count);

    /// <summary>Sets a mark on the stream's current position.</summary>
    /// <returns>The return value is not documented yet.</returns>
    int SetHardMark();

    /// <summary>Sets the stream's position from the last set
    /// mark.</summary>
    /// <param name='pos'>Zero-based character offset from the last set
    /// mark.</param>
    void SetMarkPosition(int pos);

    /// <summary>If no mark is set, sets a mark on the stream, and
    /// characters read before the currently set mark are no longer
    /// available, while characters read after will be available if
    /// MoveBack is called. Otherwise, behaves like
    /// GetMarkPosition.</summary>
    /// <returns>The return value is not documented yet.</returns>
    int SetSoftMark();
  }
}
