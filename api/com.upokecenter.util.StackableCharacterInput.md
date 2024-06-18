# com.upokecenter.util.StackableCharacterInput

    public final class StackableCharacterInput extends Object implements IMarkableCharacterInput

A character input stream where additional inputs can be stacked on.

## Constructors

## Methods

* `int GetMarkPosition()`<br>
 Not documented yet.

* `void MoveBack(int count)`<br>
 Not documented yet.

* `void PushInput(com.upokecenter.text.ICharacterInput input)`<br>
 Not documented yet.

* `int Read(int[] buf,
 int offset,
 int unitCount)`<br>
 Not documented yet.

* `int ReadChar()`<br>
 Not documented yet.

* `int SetHardMark()`<br>
 Not documented yet.

* `void SetMarkPosition(int pos)`<br>
 Not documented yet.

* `int SetSoftMark()`<br>
 Not documented yet.

## Method Details

### GetMarkPosition
    public int GetMarkPosition()
Not documented yet.

**Specified by:**

* <code>GetMarkPosition</code> in interface <code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### MoveBack
    public void MoveBack(int count)
Not documented yet.

**Specified by:**

* <code>MoveBack</code> in interface <code>IMarkableCharacterInput</code>

**Parameters:**

* <code>count</code> - The parameter <code>count</code> is a 32-bit signed integer.

### PushInput
    public void PushInput(com.upokecenter.text.ICharacterInput input)
Not documented yet.

**Parameters:**

* <code>input</code> - The parameter <code>input</code> is a.getText().ICharacterInput object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>input</code> is null.

### ReadChar
    public int ReadChar()
Not documented yet.

**Specified by:**

* <code>ReadChar</code> in interface <code>com.upokecenter.text.ICharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### Read
    public int Read(int[] buf, int offset, int unitCount)
Not documented yet.

**Specified by:**

* <code>Read</code> in interface <code>com.upokecenter.text.ICharacterInput</code>

**Parameters:**

* <code>buf</code> - The parameter <code>buf</code> is a.getInt32()[] object.

* <code>offset</code> - The parameter <code>offset</code> is a 32-bit signed integer.

* <code>unitCount</code> - The parameter <code>unitCount</code> is a 32-bit signed integer.

**Returns:**

* A 32-bit signed integer.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>buf</code> is null.

### SetHardMark
    public int SetHardMark()
Not documented yet.

**Specified by:**

* <code>SetHardMark</code> in interface <code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### SetMarkPosition
    public void SetMarkPosition(int pos)
Not documented yet.

**Specified by:**

* <code>SetMarkPosition</code> in interface <code>IMarkableCharacterInput</code>

**Parameters:**

* <code>pos</code> - The parameter <code>pos</code> is a 32-bit signed integer.

### SetSoftMark
    public int SetSoftMark()
Not documented yet.

**Specified by:**

* <code>SetSoftMark</code> in interface <code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.
