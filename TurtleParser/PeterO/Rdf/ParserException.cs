/*
Any copyright to this work is released to the Public Domain.
In case this is not possible, this work is also
licensed under the Unlicense: https://unlicense.org/

 */
using System;

namespace PeterO.Rdf {
  /// <summary>Exception thrown for errors that occur while parsing data.
  /// <para>This library may throw exceptions of this type in certain
  /// cases, notably when errors occur, and may supply messages to those
  /// exceptions (the message can be accessed through the <c>Message</c>
  /// property in.NET or the <c>getMessage()</c> method in Java). These
  /// messages are intended to be read by humans to help diagnose the
  /// error (or other cause of the exception); they are not intended to
  /// be parsed by computer programs, and the exact text of the messages
  /// may change at any time between versions of this
  /// library.</para></summary>
  #if NET20 || NET40
  [Serializable]
  #endif
  public sealed class ParserException : Exception {
    /// <summary>Initializes a new instance of the
    /// <see cref='PeterO.Rdf.ParserException'/> class.</summary>
    public ParserException() {
    }

    /// <summary>Initializes a new instance of the
    /// <see cref='PeterO.Rdf.ParserException'/> class.</summary>
    /// <param name='message'>The parameter <paramref name='message'/> is a
    /// text string.</param>
    public ParserException(string message) : base(message) {
    }

    /// <summary>Initializes a new instance of the
    /// <see cref='PeterO.Rdf.ParserException'/> class. Uses the specified
    /// message and inner exception.</summary>
    /// <param name='message'>The parameter <paramref name='message'/> is a
    /// text string.</param>
    /// <param name='innerException'>The parameter <paramref
    /// name='innerException'/> is an Exception object.</param>
    public ParserException(string message, Exception innerException)
      : base(message, innerException) {
    }

    #if NET20 || NET40
    private ParserException(
      System.Runtime.Serialization.SerializationInfo info,
      System.Runtime.Serialization.StreamingContext context)
      : base(info, context) {
    }
    #endif
  }
}
