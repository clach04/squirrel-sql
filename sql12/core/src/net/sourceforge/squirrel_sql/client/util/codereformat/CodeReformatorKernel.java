package net.sourceforge.squirrel_sql.client.util.codereformat;
/*
 * Copyright (C) 2003 Gerd Wagner
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
import java.util.ArrayList;

public class CodeReformatorKernel
{
	private CommentSpec[] _commentSpecs;
	private PieceMarkerSpec[] _pieceSpecs;

	// TODO: Diplay in GUI and example
	private boolean _lineBreakFor_AND_OR_in_FROM_clause = false;

	public CodeReformatorKernel(PieceMarkerSpec[] pieceSpecs, CommentSpec[] commentSpecs)
	{
		_commentSpecs = commentSpecs;
		_pieceSpecs = pieceSpecs;
	}

	public String[] toPieces(String in)
	{
		TopLevelPiecesIterator topLevelPiecesIterator = new TopLevelPiecesIterator(_pieceSpecs, getStatesOfPosition(in), _lineBreakFor_AND_OR_in_FROM_clause);

		ArrayList<String> ret = new ArrayList<String>();

      // toUpperCase replaces the German ß by ss.
      // This will kill reformating later.
      // Since upperIn is just for building pieces
      // it is OK to place ß here.
      String upperIn = in.replaceAll("ß", "s");


		upperIn = upperIn.toUpperCase();

		int begin = 0;


		while(begin < in.length())
		{
         Piece p = topLevelPiecesIterator.getNextToplevelPiece(begin, upperIn);

			if(null == p.spec)
			{
				ret.add(in.substring(begin).trim());
				begin = in.length();
			}
			else
			{

				int type = p.spec.getType();
				switch(type)
				{
					case PieceMarkerSpec.TYPE_PIECE_MARKER_AT_BEGIN:
						if(begin < p.beginsAt && 0 < in.substring(begin, p.beginsAt).trim().length())
						{
							ret.add(in.substring(begin, p.beginsAt).trim());
						}

						int afterPieceMarker = p.beginsAt + p.spec.getLengthRightSpaced();
						Piece nextP = topLevelPiecesIterator.getNextToplevelPiece(afterPieceMarker, upperIn);
						if(null == nextP.spec)
						{
							ret.add( in.substring(p.beginsAt).trim() );
							begin = in.length();
						}
						else
						{
                     if(PieceMarkerSpec.TYPE_PIECE_MARKER_AT_END == nextP.spec.getType())
							{
								if(nextP.beginsAt + nextP.spec.getLengthRightSpaced() < in.length())
								{
									ret.add( in.substring(p.beginsAt, nextP.beginsAt + nextP.spec.getLengthRightSpaced()).trim() );
								}
								else
								{
									ret.add( in.substring(p.beginsAt).trim() );
								}
								begin = nextP.beginsAt + nextP.spec.getLengthRightSpaced();
							}
							else
							{
								ret.add( in.substring(p.beginsAt, nextP.beginsAt).trim() );
								begin = nextP.beginsAt;
							}
						}
						break;
					case PieceMarkerSpec.TYPE_PIECE_MARKER_AT_END:
						if(p.beginsAt + p.spec.getLengthRightSpaced() < in.length())
						{
							ret.add( in.substring(begin, p.beginsAt + p.spec.getLengthRightSpaced() ).trim() );
						}
						else
						{
							ret.add( in.substring(begin).trim() );
						}
						begin = p.beginsAt + p.spec.getLengthRightSpaced();
						break;
					case PieceMarkerSpec.TYPE_PIECE_MARKER_IN_OWN_PIECE:
						if(begin < p.beginsAt && 0 < in.substring(begin, p.beginsAt).trim().length())
						{
							ret.add(in.substring(begin, p.beginsAt).trim());
						}
						if(p.beginsAt + p.spec.getLengthRightSpaced() < in.length())
						{
							ret.add( in.substring(p.beginsAt, p.beginsAt + p.spec.getLengthRightSpaced() ).trim() );
						}
						else
						{
							ret.add( in.substring(p.beginsAt).trim() );
						}
						begin = p.beginsAt + p.spec.getLengthRightSpaced();
						break;
				}

			}
		}
		return ret.toArray(new String[ret.size()]);
	}


	public StateOfPosition[] getStatesOfPosition(String in)
	{
		StateOfPosition[] ret = new StateOfPosition[in.length()];

		StateOfPosition buf = new StateOfPosition();

		for(int i=0; i < in.length(); ++i)
		{
			if('\'' == in.charAt(i))
			{
				++buf.literalSepCount;
			}

			if(0 == buf.literalSepCount % 2)
			{
				for(int j=0; j < _commentSpecs.length; ++j)
				{
					if(in.substring(i).startsWith(_commentSpecs[j].commentBegin))
					{
						if(-1 == buf.commentIndex)
						{
							buf.commentIndex = j;
						}
					}

					if(in.substring(i).startsWith(_commentSpecs[j].commentEnd))
					{
						if(j == buf.commentIndex)
						{
							buf.commentIndex = -1;
						}
					}
				}
			}

			if(0 == buf.literalSepCount % 2 && -1 == buf.commentIndex)
			{
				if('(' == in.charAt(i))
				{
					++buf.braketDepth;
				}
				if(')' == in.charAt(i))
				{
					--buf.braketDepth;
				}
			}

			if(
					-1 == buf.commentIndex
				&& 0 == buf.literalSepCount % 2
				&& 0 == buf.braketDepth
			  )
			{
				buf.isTopLevel = true;
			}
			else
			{
				buf.isTopLevel = false;
			}
			ret[i] = (StateOfPosition)buf.clone();
		}

		return ret;
	}
}
