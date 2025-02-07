using System;
using System.Collections.Generic;
using System.Globalization;
using PeterO;
using PeterO.Text;

/*
Written in 2013 by Peter Occil.
Any copyright to this work is released to the Public Domain.
In case this is not possible, this work is also
licensed under the Unlicense: https://unlicense.org/

*/
namespace PeterO {
  /// <summary>A character input stream where additional inputs can be
  /// stacked on.</summary>
  public sealed class StackableCharacterInput : IMarkableCharacterInput {
    private class InputAndBuffer : ICharacterInput {
      private int[] buffer;
      private ICharacterInput charInput;
      private int pos = 0;

      public InputAndBuffer(
        ICharacterInput charInput,
        int[] buffer,
        int offset,
        int length) {
        this.charInput = charInput;
        if (length > 0) {
          this.buffer = new int[length];
          Array.Copy(buffer, offset, this.buffer, 0, length);
        } else {
          this.buffer = null;
        }
      }

      public int ReadChar() {
        if (this.charInput != null) {
          int c = this.charInput.ReadChar();
          if (c >= 0) {
            return c;
          }
          this.charInput = null;
        }
        if (this.buffer != null) {
          if (this.pos < this.buffer.Length) {
            return this.buffer[this.pos++];
          }
          this.buffer = null;
        }
        return -1;
      }

      public int Read(int[] buf, int offset, int unitCount) {
        if (buf == null) {
          throw new ArgumentNullException(nameof(buf));
        }
        if (offset < 0) {
          throw new ArgumentException("offset less than 0(" + offset + ")");
        }
        if (unitCount < 0) {
          throw new ArgumentException("unitCount less than 0(" + unitCount +
            ")");
        }
        if (offset + unitCount > buf.Length) {
          throw new
          ArgumentOutOfRangeException("offset+unitCount more than " +
            buf.Length + " (" + (offset + unitCount) + ")");
        }
        if (unitCount == 0) {
          return 0;
        }
        var count = 0;
        if (this.charInput != null) {
          int c = this.charInput.Read(buf, offset, unitCount);
          if (c <= 0) {
            this.charInput = null;
          } else {
            offset += c;
            unitCount -= c;
            count += c;
          }
        }
        if (this.buffer != null) {
          int c = Math.Min(unitCount, this.buffer.Length - this.pos);
          if (c > 0) {
            Array.Copy(this.buffer, this.pos, buf, offset, c);
          }
          this.pos += c;
          count += c;
          if (c == 0) {
            this.buffer = null;
          }
        }
        return count;
      }
    }

    private int pos;
    private int endpos;
    private bool haveMark;
    private int[] buffer;
    private IList<ICharacterInput> stack = new List<ICharacterInput>();

    /// <summary>Initializes a new instance of the
    /// <see cref='PeterO.StackableCharacterInput'/> class.</summary>
    /// <param name='source'>The parameter <paramref name='source'/> is an
    /// ICharacterInput object.</param>
    public StackableCharacterInput(ICharacterInput source) {
      this.stack.Add(source);
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A 32-bit signed integer.</returns>
    public int GetMarkPosition() {
      return this.pos;
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='count'>The parameter <paramref name='count'/> is a
    /// 32-bit signed integer.</param>
    public void MoveBack(int count) {
      if (count < 0) {
        throw new ArgumentException("count(" + count +
          ") is not greater or equal to 0");
      }
      if (this.haveMark && this.pos >= count) {
        this.pos -= count;
        return;
      }
      throw new InvalidOperationException();
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='input'>The parameter <paramref name='input'/> is
    /// a.Text.ICharacterInput object.</param>
    /// <exception cref='ArgumentNullException'>The parameter <paramref
    /// name='input'/> is null.</exception>
    public void PushInput(ICharacterInput input) {
      if (input == null) {
        throw new ArgumentNullException(nameof(input));
      }
      // Move unread characters in buffer, since this new
      // input sits on top of the existing input
      this.stack.Add(
        new InputAndBuffer(
          input,
          this.buffer,
          this.pos,
          this.endpos - this.pos));
      this.endpos = this.pos;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A 32-bit signed integer.</returns>
    public int ReadChar() {
      if (this.haveMark) {
        // Read from buffer
        if (this.pos < this.endpos) {
          int ch = this.buffer[this.pos++];
          // Console.WriteLine ("buffer: [" + ch + "],["+(char)ch+"]");
          return ch;
        }
        // Console.WriteLine(this);
        // End pos is smaller than buffer size, fill
        // entire buffer if possible
        if (this.endpos < this.buffer.Length) {
          int count = this.ReadInternal(
              this.buffer,
              this.endpos,
              this.buffer.Length - this.endpos);
          if (count > 0) {
            this.endpos += count;
          }
        }
        // Try reading from buffer again
        if (this.pos < this.endpos) {
          int ch = this.buffer[this.pos++];
          // Console.WriteLine ("buffer2: [" + ch + "],[" + charch + "]");
          return ch;
        }
        // Console.WriteLine(this);
        // No room, read next character and put it in buffer
        int c = this.ReadInternal();
        if (c < 0) {
          return c;
        }
        if (this.pos >= this.buffer.Length) {
          var newBuffer = new int[this.buffer.Length * 2];
          Array.Copy(this.buffer, 0, newBuffer, 0, this.buffer.Length);
          this.buffer = newBuffer;
        }
        // Console.WriteLine(this);
        this.buffer[this.pos++] = c;
        ++this.endpos;
        // Console.WriteLine ("readInt3: [" + c + "],[" + charc + "]");
        return c;
      } else {
        int c = this.ReadInternal();
        // Console.WriteLine ("readInt3: [" + c + "],[" + charc + "]");
        return c;
      }
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='buf'>The parameter <paramref name='buf'/> is a.Int32[]
    /// object.</param>
    /// <param name='offset'>The parameter <paramref name='offset'/> is a
    /// 32-bit signed integer.</param>
    /// <param name='unitCount'>The parameter <paramref name='unitCount'/>
    /// is a 32-bit signed integer.</param>
    /// <returns>A 32-bit signed integer.</returns>
    /// <exception cref='ArgumentNullException'>The parameter <paramref
    /// name='buf'/> is null.</exception>
    public int Read(int[] buf, int offset, int unitCount) {
      if (buf == null) {
        throw new ArgumentNullException(nameof(buf));
      }
      if (offset < 0) {
        throw new ArgumentException("offset(" + offset +
          ") is less than 0");
      }
      if (offset > buf.Length) {
        throw new ArgumentException("offset(" + offset +
          ") is more than " + buf.Length);
      }
      if (unitCount < 0) {
        throw new ArgumentException("unitCount(" + unitCount +
          ") is less than 0");
      }
      if (unitCount > buf.Length) {
        throw new ArgumentException("unitCount(" + unitCount +
          ") is more than " + buf.Length);
      }
      if (buf.Length - offset < unitCount) {
        throw new ArgumentException("buf's length minus " + offset + "(" +
          (buf.Length - offset) + ") is less than " + unitCount);
      }
      if (this.haveMark) {
        if (unitCount == 0) {
          return 0;
        }
        // Read from buffer
        if (this.pos + unitCount <= this.endpos) {
          Array.Copy(this.buffer, this.pos, buf, offset, unitCount);
          this.pos += unitCount;
          return unitCount;
        }
        // End pos is smaller than buffer size, fill
        // entire buffer if possible
        var count = 0;
        if (this.endpos < this.buffer.Length) {
          count = this.ReadInternal(
              this.buffer,
              this.endpos,
              this.buffer.Length - this.endpos);
          // Console.WriteLine("%s",this);
          if (count > 0) {
            this.endpos += count;
          }
        }
        var total = 0;
        // Try reading from buffer again
        if (this.pos + unitCount <= this.endpos) {
          Array.Copy(this.buffer, this.pos, buf, offset, unitCount);
          this.pos += unitCount;
          return unitCount;
        }
        // expand the buffer
        if (this.pos + unitCount > this.buffer.Length) {
          var newBuffer = new int[(this.buffer.Length * 2) + unitCount];
          Array.Copy(this.buffer, 0, newBuffer, 0, this.buffer.Length);
          this.buffer = newBuffer;
        }
        count = this.ReadInternal(
            this.buffer,
            this.endpos,
            Math.Min(unitCount, this.buffer.Length - this.endpos));
        if (count > 0) {
          this.endpos += count;
        }
        // Try reading from buffer a third time
        if (this.pos + unitCount <= this.endpos) {
          Array.Copy(this.buffer, this.pos, buf, offset, unitCount);
          this.pos += unitCount;
          total += unitCount;
        } else if (this.endpos > this.pos) {
          Array.Copy(
            this.buffer,
            this.pos,
            buf,
            offset,
            this.endpos - this.pos);
          total += this.endpos - this.pos;
          this.pos = this.endpos;
        }
        return total;
      } else {
        return this.ReadInternal(buf, offset, unitCount);
      }
    }

    private int ReadInternal() {
      if (this.stack.Count == 0) {
        return -1;
      }
      while (this.stack.Count > 0) {
        int index = this.stack.Count - 1;
        int c = this.stack[index].ReadChar();
        if (c == -1) {
          this.stack.RemoveAt(index);
          continue;
        }
        return c;
      }
      return -1;
    }

    private int ReadInternal(int[] buf, int offset, int unitCount) {
      if (this.stack.Count == 0) {
        return -1;
      }
      if (unitCount == 0) {
        return 0;
      }
      var count = 0;
      while (this.stack.Count > 0 && unitCount > 0) {
        int index = this.stack.Count - 1;
        int c = this.stack[index].Read(buf, offset, unitCount);
        if (c <= 0) {
          this.stack.RemoveAt(index);
          continue;
        }
        count += c;
        unitCount -= c;
        if (unitCount == 0) {
          break;
        }
        this.stack.RemoveAt(index);
      }
      return count;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A 32-bit signed integer.</returns>
    public int SetHardMark() {
      if (this.buffer == null) {
        this.buffer = new int[16];
        this.pos = 0;
        this.endpos = 0;
        this.haveMark = true;
      } else if (this.haveMark) {
        // Already have a mark; shift buffer to the new mark
        if (this.pos > 0 && this.pos < this.endpos) {
          Array.Copy(
            this.buffer,
            this.pos,
            this.buffer,
            0,
            this.endpos - this.pos);
        }
        this.endpos -= this.pos;
        this.pos = 0;
      } else {
        this.pos = 0;
        this.endpos = 0;
        this.haveMark = true;
      }
      return 0;
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='pos'>The parameter <paramref name='pos'/> is a 32-bit
    /// signed integer.</param>
    public void SetMarkPosition(int pos) {
      if (!this.haveMark || pos < 0 || pos > this.endpos) {
        throw new InvalidOperationException();
      }
      this.pos = pos;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A 32-bit signed integer.</returns>
    public int SetSoftMark() {
      if (!this.haveMark) {
        this.SetHardMark();
      }
      return this.GetMarkPosition();
    }
  }
}
