/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
*/
namespace PeterO {
using System;
using PeterO.Text;

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="T:PeterO.IMarkableCharacterInput"]/*'/>
public interface IMarkableCharacterInput : ICharacterInput {
    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.getMarkPosition"]/*'/>
   int getMarkPosition();

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.moveBack(System.Int32)"]/*'/>
   void moveBack(int count);

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.setHardMark"]/*'/>
   int setHardMark();

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.setMarkPosition(System.Int32)"]/*'/>
   void setMarkPosition(int pos);

    /// <include file='../docs.xml'
    /// path='docs/doc[@name="M:PeterO.IMarkableCharacterInput.setSoftMark"]/*'/>
    int setSoftMark();
}
}
