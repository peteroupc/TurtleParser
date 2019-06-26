# com.upokecenter.util.StackableCharacterInput

    public final class StackableCharacterInput extends java.lang.Object implements IMarkableCharacterInput

A character input stream where additional inputs can be

## Methods

* `StackableCharacterInput​(com.upokecenter.text.ICharacterInput source) StackableCharacterInput`<br>
 Initializes a new instance of the StackableCharacterInput class.
* `int GetMarkPosition()`<br>
 Not documented yet.
* `void MoveBack​(int count)`<br>
 Not documented yet.
* `void PushInput​(com.upokecenter.text.ICharacterInput input)`<br>
 Not documented yet.
* `int Read​(int[] buf,
    int offset,
    int unitCount)`<br>
 Not documented yet.
* `int ReadChar()`<br>
 Not documented yet.
* `int SetHardMark()`<br>
 Not documented yet.
* `void SetMarkPosition​(int pos)`<br>
 Not documented yet.
* `int SetSoftMark()`<br>
 Not documented yet.

## Constructors

* `StackableCharacterInput​(com.upokecenter.text.ICharacterInput source) StackableCharacterInput`<br>
 Initializes a new instance of the StackableCharacterInput class.

## Method Details

### StackableCharacterInput
    public StackableCharacterInput​(com.upokecenter.text.ICharacterInput source)
Initializes a new instance of the <code>StackableCharacterInput</code> class.

**Parameters:**

* <code>source</code> - The parameter <code>source</code> is an ICharacterInput object.

### StackableCharacterInput
    public StackableCharacterInput​(com.upokecenter.text.ICharacterInput source)
Initializes a new instance of the <code>StackableCharacterInput</code> class.

**Parameters:**

* <code>source</code> - The parameter <code>source</code> is an ICharacterInput object.

### GetMarkPosition
    public int GetMarkPosition()
Not documented yet.

**Specified by:**

* <code>GetMarkPosition</code> in interface <code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### MoveBack
    public void MoveBack​(int count)
Not documented yet.

**Specified by:**

* <code>MoveBack</code> in interface <code>IMarkableCharacterInput</code>

**Parameters:**

* <code>count</code> - The parameter <code>count</code> is not documented yet.

### PushInput
    public void PushInput​(com.upokecenter.text.ICharacterInput input)
Not documented yet.

**Parameters:**

* <code>input</code> - The parameter <code>input</code> is not documented yet.

### ReadChar
    public int ReadChar()
Not documented yet.

**Specified by:**

* <code>ReadChar</code> in interface <code>com.upokecenter.text.ICharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### Read
    public int Read​(int[] buf, int offset, int unitCount)
Not documented yet.

**Specified by:**

* <code>Read</code> in interface <code>com.upokecenter.text.ICharacterInput</code>

**Parameters:**

* <code>buf</code> - The parameter <code>buf</code> is not documented yet.

* <code>offset</code> - The parameter <code>offset</code> is not documented yet.

* <code>unitCount</code> - The parameter <code>unitCount</code> is not documented yet.

**Returns:**

* A 32-bit signed integer.

**Throws:**

* <code>java.lang.NullPointerException</code> - The parameter <code>buf</code> is null.

### SetHardMark
    public int SetHardMark()
Not documented yet.

**Specified by:**

* <code>SetHardMark</code> in interface <code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### SetMarkPosition
    public void SetMarkPosition​(int pos)
Not documented yet.

**Specified by:**

* <code>SetMarkPosition</code> in interface <code>IMarkableCharacterInput</code>

**Parameters:**

* <code>pos</code> - The parameter <code>pos</code> is not documented yet.

### SetSoftMark
    public int SetSoftMark()
Not documented yet.

**Specified by:**

* <code>SetSoftMark</code> in interface <code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.
