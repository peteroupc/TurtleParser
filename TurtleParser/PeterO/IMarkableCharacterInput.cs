using PeterO.Text;

/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/
namespace PeterO {
  /// <include file='../docs.xml'
  /// path='docs/doc[@name="T:PeterO.IMarkableCharacterInput"]/*'/>
  public interface IMarkableCharacterInput : ICharacterInput {
    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.GetMarkPosition"]/*'/>
    int GetMarkPosition();

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.MoveBack(System.Int32)"]/*'/>
    void MoveBack(int count);

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.SetHardMark"]/*'/>
    int SetHardMark();

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.SetMarkPosition(System.Int32)"]/*'/>
    void SetMarkPosition(int pos);

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.SetSoftMark"]/*'/>
    int SetSoftMark();
  }
}
