# com.upokecenter.util.IMarkableCharacterInput

    public interface IMarkableCharacterInput extends com.upokecenter.text.ICharacterInput

Not documented yet.

## Methods

* `int GetMarkPosition()`<br>
 Gets the zero-based character position in the stream from the last-set mark.
* `void MoveBack​(int count)`<br>
 Moves the stream position back the given number of characters.
* `int SetHardMark()`<br>
 Sets a mark on the stream's current position.
* `void SetMarkPosition​(int pos)`<br>
 Sets the stream's position from the last set mark.
* `int SetSoftMark()`<br>
 If no mark is set, sets a mark on the stream, and characters read before the
 currently set mark are no longer available, while characters read
 after will be available if MoveBack is called.

## Method Details

### GetMarkPosition
    int GetMarkPosition()
Gets the zero-based character position in the stream from the last-set mark.

**Returns:**

* The return value is not documented yet.

### MoveBack
    void MoveBack​(int count)
Moves the stream position back the given number of characters.

**Parameters:**

* <code>count</code> - The parameter <code>count</code> is not documented yet.

### SetHardMark
    int SetHardMark()
Sets a mark on the stream's current position.

**Returns:**

* The return value is not documented yet.

### SetMarkPosition
    void SetMarkPosition​(int pos)
Sets the stream's position from the last set mark. <param name='pos'/>Zero-based character offset from the last set mark.

**Parameters:**

* <code>pos</code> - Zero-based character offset from the last set mark.

### SetSoftMark
    int SetSoftMark()
If no mark is set, sets a mark on the stream, and characters read before the
 currently set mark are no longer available, while characters read
 after will be available if MoveBack is called. Otherwise, behaves
 like GetMarkPosition.

**Returns:**

* The return value is not documented yet.
