/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.html.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.AbstractTagProcessor;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.css.apply.HtmlCellCssApplier;
import com.itextpdf.tool.xml.css.apply.ParagraphCssApplier;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.HTMLUtils;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import com.itextpdf.tool.xml.html.table.TableRowElement.Place;
/**
 * @author Balder Van Camp
 *
 */
public class TableData extends AbstractTagProcessor {

    /* (non-Javadoc)
     * @see com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag, java.util.List, com.itextpdf.text.Document, java.lang.String)
     */
    @Override
	public List<Element> content(final Tag tag, final String content) {
    	String sanitized = HTMLUtils.sanitizeInline(content);
    	List<Element> l = new ArrayList<Element>(1);
    	if (sanitized.length() > 0) {
    		l.add(new ChunkCssApplier().apply(new Chunk(sanitized), tag));
    	}
    	return l;
    }

    /* (non-Javadoc)
     * @see com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag, java.util.List, com.itextpdf.text.Document)
     */
    @Override
	public List<Element> end(final Tag tag, final List<Element> currentContent) {
//    	HtmlCell cell = new HtmlCell();
//    	Paragraph content = new Paragraph();
//    	for (Element e : currentContent) {
//    		content.add(e);
//    	}
//    	content = new ParagraphCssApplier(configuration).apply(content, tag);
//    	cell.addElement(content);
    	HtmlCell cell = new HtmlCell();
    	List<Element> l = new ArrayList<Element>(1);
    	for (Element e : currentContent) {
    		if(e instanceof Paragraph) {
    			e = new ParagraphCssApplier(configuration).apply((Paragraph)e, tag);
    		}
    		cell.addElement(e);
    	}
    	cell = new HtmlCellCssApplier(configuration).apply(cell, tag);
    	if(tag.getTag().equalsIgnoreCase(HTML.Tag.CAPTION)) {
    		currentContent.clear();
    		currentContent.add(cell);
    		String captionSideValue = tag.getCSS().get(CSS.Property.CAPTION_SIDE);
    		if(captionSideValue != null && captionSideValue.equalsIgnoreCase(CSS.Value.BOTTOM)) {
    			l.add(new TableRowElement(currentContent, Place.CAPTION_BOTTOM));
    		} else {
    			l.add(new TableRowElement(currentContent, Place.CAPTION_TOP));
    		}
    		return l;
    	} else {
    		l.add(cell);
    		return l;
    	}
    }

    /* (non-Javadoc)
     * @see com.itextpdf.tool.xml.TagProcessor#isStackOwner()
     */
    @Override
	public boolean isStackOwner() {
        return true;
    }

}