package org.pdf.forms.widgets;

import java.util.Iterator;

import javax.xml.crypto.NodeSetData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeSetDataImpl implements NodeSetData<Node>, Iterator<Node> {

    private Node ivNode;
    private NodeFilter ivNodeFilter;
    private DocumentTraversal ivDocumentTraversal;
    private NodeIterator ivNodeIterator;
    private Node ivNextNode;

    public NodeSetDataImpl(
            final Node pNode,
            final NodeFilter pNodeFilter) {
        this.ivNode = pNode;
        this.ivNodeFilter = pNodeFilter;

        final Document ivDocument;
        if (ivNode instanceof Document) {
            ivDocument = (Document) ivNode;
        } else {
            ivDocument = ivNode.getOwnerDocument();
        }

        this.ivDocumentTraversal = (DocumentTraversal) ivDocument;
    }

    private NodeSetDataImpl(final NodeIterator pNodeIterator) {
        this.ivNodeIterator = pNodeIterator;
    }

    @Override
    public Iterator<Node> iterator() {
        final NodeIterator nodeIterator = ivDocumentTraversal.createNodeIterator(ivNode,
                NodeFilter.SHOW_ALL,
                ivNodeFilter,
                false);
        return new NodeSetDataImpl(nodeIterator);
    }

    private Node checkNextNode() {
        if (ivNextNode == null && ivNodeIterator != null) {
            this.ivNextNode = ivNodeIterator.nextNode();
            if (ivNextNode == null) {
                ivNodeIterator.detach();
                this.ivNodeIterator = null;
            }
        }
        return ivNextNode;
    }

    private Node consumeNextNode() {
        final Node nextNode = checkNextNode();
        this.ivNextNode = null;
        return nextNode;
    }

    @Override
    public boolean hasNext() {
        return checkNextNode() != null;
    }

    @Override
    public Node next() {
        return consumeNextNode();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Removing nodes is not supported.");
    }

    public static NodeFilter getRootNodeFilter() {
        return pNode -> {
            if (pNode instanceof Element && pNode.getParentNode() instanceof Document) {
                return NodeFilter.FILTER_SKIP;
            }
            return NodeFilter.FILTER_ACCEPT;
        };
    }
}
