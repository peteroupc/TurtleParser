# com.upokecenter.rdf.ParserException

    public final class ParserException extends java.lang.RuntimeException

Exception thrown for errors that occur while parsing data. <p>This library
 may throw exceptions of this type in certain cases, notably when
 errors occur, and may supply messages to those exceptions (the message
 can be accessed through the <code>Message</code> property in.NET or the
 <code>getMessage()</code> method in Java). These messages are intended to be
 read by humans to help diagnose the error (or other cause of the
 exception); they are not intended to be parsed by computer programs,
 and the exact text of the messages may change at any time between
 versions of this library.</p>

## Methods

* `ParserException() ParserException`<br>
 Initializes a new instance of the ParserException class.
* `ParserException​(java.lang.String message) ParserException`<br>
 Initializes a new instance of the ParserException class.
* `ParserException​(java.lang.String message,
               java.lang.Throwable innerException) ParserException`<br>
 Initializes a new instance of the ParserException class.

## Constructors

* `ParserException() ParserException`<br>
 Initializes a new instance of the ParserException class.
* `ParserException​(java.lang.String message) ParserException`<br>
 Initializes a new instance of the ParserException class.
* `ParserException​(java.lang.String message,
               java.lang.Throwable innerException) ParserException`<br>
 Initializes a new instance of the ParserException class.
