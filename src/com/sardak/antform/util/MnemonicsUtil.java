/***************************************************************************\*
 *                                                                            *
 *    AntForm form-based interaction for Ant scripts                          *
 *    Copyright (C) 2005 René Ghosh                                           *
 *                                                                            *
 *   This library is free software; you can redistribute it and/or modify it  *
 *   under the terms of the GNU Lesser General Public License as published by *
 *   the Free Software Foundation; either version 2.1 of the License, or (at  *
 *   your option) any later version.                                          *
 *                                                                            *
 *   This library is distributed in the hope that it will be useful, but      *
 *   WITHOUT ANY WARRANTY; without even the implied warranty of               *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser  *
 *   General Public License for more details.                                 *
 *                                                                            *
 *   You should have received a copy of the GNU Lesser General Public License *
 *   along with this library; if not, write to the Free Software Foundation,  *
 *   Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA              *
 \****************************************************************************/
package com.sardak.antform.util;

import java.util.HashSet;

/**
 * @author René Ghosh 11 mars 2005
 */
public abstract class MnemonicsUtil {

    /**
     * get a new mnemonic for a list of used letters and a label
     */
    public static String newMnemonic(String labelText, HashSet usedLetters) {
        char toUse = 'a';
        String sToUse = null;
        for (int i = 0; i < labelText.length(); i++) {
            toUse = labelText.charAt(i);
            sToUse = ("" + toUse).toUpperCase();
            if (usedLetters.contains(sToUse) || !Character.isLetter(toUse)) {
                continue;
            }
            usedLetters.add(sToUse);
            return sToUse;
        }
        return null;
    }

}
