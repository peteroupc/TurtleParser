# com.upokecenter.util.StackableCharacterInput

    public final class StackableCharacterInput extends Object implements IMarkableCharacterInput

A character input stream where additional inputs can be stacked on top of
 it. It supports advanced marking capabilities.

## Methods

* `StackableCharacterInput(com.upokecenter.text.ICharacterInput source) com.upokecenter.StackableCharacterInput`<br>
 Initializes a new instance of the com.upokecenter.StackableCharacterInput class.
* `int getMarkPosition()`<br>
 Not documented yet.
* `void moveBack(int count)`<br>
 Not documented yet.
* `void pushInput(com.upokecenter.text.ICharacterInput input)`<br>
 Not documented yet.
* `int Read(int[] buf,
    int offset,
    int unitCount)`<br>
 Not documented yet.
* `int ReadChar()`<br>
 Not documented yet.
* `int setHardMark()`<br>
 Not documented yet.
* `void setMarkPosition(int pos)`<br>
 Not documented yet.
* `int setSoftMark()`<br>
 Not documented yet.

## Constructors

* `StackableCharacterInput(com.upokecenter.text.ICharacterInput source) com.upokecenter.StackableCharacterInput`<br>
 Initializes a new instance of the com.upokecenter.StackableCharacterInput class.

## Method Details

### StackableCharacterInput
    public StackableCharacterInput(com.upokecenter.text.ICharacterInput source)
Initializes a new instance of the <code>com.upokecenter.StackableCharacterInput</code> class.

**Parameters:**

* <code>source</code> - An ICharacterInput object.

### StackableCharacterInput
    public StackableCharacterInput(com.upokecenter.text.ICharacterInput source)
Initializes a new instance of the <code>com.upokecenter.StackableCharacterInput</code> class.

**Parameters:**

* <code>source</code> - An ICharacterInput object.

### getMarkPosition
    public int getMarkPosition()
Not documented yet.

**Specified by:**

* <code>getMarkPosition</code>&nbsp;in interface&nbsp;<code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### moveBack
    public void moveBack(int count)
Not documented yet.

**Specified by:**

* <code>moveBack</code>&nbsp;in interface&nbsp;<code>IMarkableCharacterInput</code>

**Parameters:**

* <code>count</code> - The parameter <code>count</code> is not documented yet.

### pushInput
    public void pushInput(com.upokecenter.text.ICharacterInput input)
Not documented yet.

**Parameters:**

* <code>input</code> - The parameter <code>input</code> is not documented yet.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>input</code> is null.

### ReadChar
    public int ReadChar()
Not documented yet.

**Specified by:**

* <code>ReadChar</code>&nbsp;in interface&nbsp;<code>com.upokecenter.text.ICharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### Read
    public int Read(int[] buf, int offset, int unitCount)
Not documented yet.

**Specified by:**

* <code>Read</code>&nbsp;in interface&nbsp;<code>com.upokecenter.text.ICharacterInput</code>

**Parameters:**

* <code>buf</code> - The parameter <code>buf</code> is not documented yet.

* <code>offset</code> - The parameter <code>offset</code> is not documented yet.

* <code>unitCount</code> - The parameter <code>unitCount</code> is not documented yet.

**Returns:**

* A 32-bit signed integer.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>buf</code> is null.

### setHardMark
    public int setHardMark()
Not documented yet.

**Specified by:**

* <code>setHardMark</code>&nbsp;in interface&nbsp;<code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.

### setMarkPosition
    public void setMarkPosition(int pos)
Not documented yet.

**Specified by:**

* <code>setMarkPosition</code>&nbsp;in interface&nbsp;<code>IMarkableCharacterInput</code>

**Parameters:**

* <code>pos</code> - The parameter <code>pos</code> is not documented yet.

### setSoftMark
    public int setSoftMark()
Not documented yet.

**Specified by:**

* <code>setSoftMark</code>&nbsp;in interface&nbsp;<code>IMarkableCharacterInput</code>

**Returns:**

* A 32-bit signed integer.
