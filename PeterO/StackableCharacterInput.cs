/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
*/
namespace PeterO {
  using System;
  using System.Collections.Generic;
  using System.Globalization;
  using PeterO;
  using PeterO.Text;

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="T:PeterO.StackableCharacterInput"]/*'/>
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
          throw new ArgumentNullException("buf");
        }
        if (offset < 0) {
          throw new ArgumentException("offset less than 0 (" +
            Convert.ToString(offset, CultureInfo.InvariantCulture) + ")");
        }
        if (unitCount < 0) {
          throw new ArgumentException("unitCount less than 0 (" +
            Convert.ToString(unitCount, CultureInfo.InvariantCulture) + ")");
        }
        if (offset + unitCount > buf.Length) {
          throw new ArgumentOutOfRangeException("offset+unitCount more than " +
            Convert.ToString(buf.Length, CultureInfo.InvariantCulture) + " (" +
   Convert.ToString(offset + unitCount, CultureInfo.InvariantCulture) +
              ")");
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

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.#ctor(PeterO.Text.ICharacterInput)"]/*'/>
    public StackableCharacterInput(ICharacterInput source) {
      this.stack.Add(source);
    }

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.getMarkPosition"]/*'/>
    /// <summary>Not documented yet.</summary>
    public int getMarkPosition() {
      return this.pos;
    }

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.moveBack(System.Int32)"]/*'/>
    /// <summary>Not documented yet.</summary>
    public void moveBack(int count) {
      if (count < 0) {
        throw new ArgumentException("count less than 0 (" +
          Convert.ToString(count, CultureInfo.InvariantCulture) + ")");
      }
      if (this.haveMark && this.pos >= count) {
        this.pos -= count;
        return;
      }
      throw new InvalidOperationException();
    }

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.pushInput(PeterO.Text.ICharacterInput)"]/*'/>
    public void pushInput(ICharacterInput input) {
      if (input == null) {
        throw new ArgumentNullException("input");
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

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.ReadChar"]/*'/>
    public int ReadChar() {
      if (this.haveMark) {
        // Read from buffer
        if (this.pos < this.endpos) {
          return this.buffer[this.pos++];
        }
        // Console.WriteLine(this);
        // End pos is smaller than buffer size, fill
        // entire buffer if possible
        if (this.endpos < this.buffer.Length) {
          int count = this.readInternal(
  this.buffer,
  this.endpos,
  this.buffer.Length - this.endpos);
          if (count > 0) {
            this.endpos += count;
          }
        }
        // Try reading from buffer again
        if (this.pos < this.endpos) {
          return this.buffer[this.pos++];
        }
        // Console.WriteLine(this);
        // No room, read next character and put it in buffer
        int c = this.readInternal();
        if (c < 0) {
          return c;
        }
        if (this.pos >= this.buffer.Length) {
          var newBuffer = new int[this.buffer.Length * 2];
          Array.Copy(this.buffer, 0, newBuffer, 0, this.buffer.Length);
          this.buffer = newBuffer;
        }
        // Console.WriteLine(this);
        this.buffer[this.pos++] = (byte)(c & 0xff);
        ++this.endpos;
        return c;
      } else {
        return this.readInternal();
      }
    }

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.Read(System.Int32[],System.Int32,System.Int32)"]/*'/>
    public int Read(int[] buf, int offset, int unitCount) {
      if (buf == null) {
        throw new ArgumentNullException("buf");
      }
      if (offset < 0) {
        throw new ArgumentException("offset (" + offset +
          ") is less than " + 0);
      }
      if (offset > buf.Length) {
        throw new ArgumentException("offset (" + offset +
          ") is more than " + buf.Length);
      }
      if (unitCount < 0) {
        throw new ArgumentException("unitCount (" + unitCount +
          ") is less than " + 0);
      }
      if (unitCount > buf.Length) {
        throw new ArgumentException("unitCount (" + unitCount +
          ") is more than " + buf.Length);
      }
      if (buf.Length - offset < unitCount) {
        throw new ArgumentException("buf's length minus " + offset + " (" +
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
          count = this.readInternal(
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
        count = this.readInternal(
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
        return this.readInternal(buf, offset, unitCount);
      }
    }

    private int readInternal() {
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

    private int readInternal(int[] buf, int offset, int unitCount) {
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

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.setHardMark"]/*'/>
    /// <summary>Not documented yet.</summary>
    public int setHardMark() {
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

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.setMarkPosition(System.Int32)"]/*'/>
    /// <summary>Not documented yet.</summary>
    public void setMarkPosition(int pos) {
      if (!this.haveMark || pos < 0 || pos > this.endpos) {
        throw new InvalidOperationException();
      }
      this.pos = pos;
    }

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.StackableCharacterInput.setSoftMark"]/*'/>
    /// <summary>Not documented yet.</summary>
    public int setSoftMark() {
      if (!this.haveMark) {
        this.setHardMark();
      }
      return this.getMarkPosition();
    }
  }
}
