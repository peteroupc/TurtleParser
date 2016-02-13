# com.upokecenter.text.ICharacterEncoding

    public interface ICharacterEncoding

## Methods

* `ICharacterDecoder GetDecoder()`<br>
 Creates a decoder for this character encoding with initial state.
* `ICharacterEncoder GetEncoder()`<br>
 Creates an encoder for this character encoding with initial state.

## Method Details

### GetEncoder
    ICharacterEncoder GetEncoder()
Creates an encoder for this character encoding with initial state. If the
 encoder is stateless, multiple calls of this method can return the
 same encoder.

**Returns:**

* A character encoder object.

### GetDecoder
    ICharacterDecoder GetDecoder()
Creates a decoder for this character encoding with initial state. If the
 decoder is stateless, multiple calls of this method can return the
 same decoder.

**Returns:**

* A character decoder object.
