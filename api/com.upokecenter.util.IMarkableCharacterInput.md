# com.upokecenter.util.IMarkableCharacterInput

    public interface IMarkableCharacterInput extends com.upokecenter.text.ICharacterInput

Not documented yet.

## Methods

* `int getMarkPosition()`<br>
 Gets the zero-based character position in the stream from the last-set mark.
* `void moveBack(int count)`<br>
 Moves the stream position back the given number of characters.
* `int setHardMark()`<br>
 Sets a mark on the stream's current position.
* `void setMarkPosition(int pos)`<br>
 Sets the stream's position from the last set mark.
* `int setSoftMark()`<br>
 If no mark is set, sets a mark on the stream, and characters read before the
 currently set mark are no longer available, while characters read
 after will be available if moveBack is called.

## Method Details

### getMarkPosition
    int getMarkPosition()
Gets the zero-based character position in the stream from the last-set mark.

**Returns:**

* The return value is not documented yet.

### moveBack
    void moveBack(int count)
Moves the stream position back the given number of characters.

**Parameters:**

* <code>count</code> - The parameter <code>count</code> is not documented yet.

### setHardMark
    int setHardMark()
Sets a mark on the stream's current position.

**Returns:**

* The return value is not documented yet.

### setMarkPosition
    void setMarkPosition(int pos)
Sets the stream's position from the last set mark. <param name='pos'>Zero-based character offset from the last set
 mark.</param>

**Parameters:**

* <code>pos</code> - The parameter <code>pos</code> is not documented yet.

* <code>pos</code> - Zero-based character offset from the last set mark.

### setSoftMark
    int setSoftMark()
If no mark is set, sets a mark on the stream, and characters read before the
 currently set mark are no longer available, while characters read
 after will be available if moveBack is called. Otherwise, behaves
 like getMarkPosition.

**Returns:**

* The return value is not documented yet.
